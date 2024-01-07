package com.fileproc.spark_jobs.sampleJob;

import java.io.FileNotFoundException;
import java.util.Map.Entry;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import com.fileproc.spark_jobs.sampleJob.models.Template;
import com.fileproc.spark_jobs.sampleJob.models.TemplateFields;
import com.fileproc.spark_jobs.sampleJob.utils.Utils;

public class Test {

	private static SparkSession getSparkSession() {
		SparkSession spark = SparkSession.builder()//
				.master("local")//
				.appName("sampleJob")//
				.getOrCreate();
		spark.sql("set spark.sql.legacy.timeParserPolicy=LEGACY");
		return spark;
	}

	public static void main(String[] args) throws FileNotFoundException {

		ClassLoader classLoader = new Test().getClass().getClassLoader();

		Template template = Utils.parseTemplateYamlFile(classLoader.getResourceAsStream("template.yaml"));

		System.out.println(template);

//		for (Entry<String, TemplateFields> entry : template.getFields().entrySet()) {
//			System.out.println(entry.getValue().getOutputExpression());
//		}
		SparkSession spark = getSparkSession();

		Dataset<Row> inputDf = spark//
				.read()//
				.option("header", true)//
				.option("delimiter", template.getInputFileDelimiter())//
				.csv(classLoader.getResource("sample.csv").getPath());

		inputDf.show();

		Dataset<Row> parsedDf = inputDf;

		for (Entry<String, TemplateFields> entry : template.getFields().entrySet()) {

			String fieldName = entry.getKey();
			TemplateFields field = entry.getValue();
			String nullCheckName = "RDZ_" + fieldName + "_NULL_CHECK";
			String formatCheckName = "RDZ_" + fieldName + "_FORMAT_CHECK";

			if (field.getDefaultValue() != null) {
				parsedDf = parsedDf.withColumn(fieldName, functions.lit(field.getDefaultValue()));
			} else {

				// Handle if column itself is not present in file
				parsedDf = parsedDf.withColumn(nullCheckName, functions.when( //
						functions.col(field.getMandatory()).isNull(), //
						functions.lit(field.getMandatory() + " is having null ")));

				parsedDf = parsedDf.withColumn(formatCheckName, functions.when( //
						functions.lit(field.getFormatExpr()).isNotNull()
								.and(functions.expr(field.getFormatExpr()).isNull()),
						functions.lit(field.getFormatExpr() + "format check failed")));

				parsedDf = parsedDf.withColumn(fieldName,
						functions.when(
								functions.col(nullCheckName).isNull().and(functions.col(formatCheckName).isNull()),
								functions.expr(field.getOutputExpression())));

			}
		}

		parsedDf.show();

	}

}

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

		Test test = new Test();

//		test.getClass().getClassLoader().
		Template template = Utils
				.parseTemplateYamlFile(test.getClass().getClassLoader().getResourceAsStream("template.yaml"));

		System.out.println(template);

//		for (Entry<String, TemplateFields> entry : template.getFields().entrySet()) {
//			System.out.println(entry.getValue().getOutputExpression());
//		}
		SparkSession spark = getSparkSession();

		Dataset<Row> inputDf = spark//
				.read()//
				.option("header", true)//
				.option("delimiter", template.getInputFileDelimiter())//
				.csv("C:\\Users\\Keshore\\github\\mhnasir\\spark-jobs\\sampleJob\\src\\main\\resources\\sample.csv");

		inputDf.show();

		Dataset<Row> parsedDf = inputDf;

		for (Entry<String, TemplateFields> entry : template.getFields().entrySet()) {

			String fieldName = entry.getKey();
			TemplateFields field = entry.getValue();

			if (field.getDefaultValue() != null) {
				parsedDf = parsedDf.withColumn(fieldName, functions.lit(field.getDefaultValue()));
			} else {

				// Handle if column itself is not present in file
				parsedDf = parsedDf.withColumn("RDZ_NULL_CHECK", functions.when( //
						functions.col(field.getMandatory()).isNull(), //
						functions.lit(field.getMandatory() + " is having null ")));

				parsedDf = parsedDf.withColumn("RDZ_FORMAT_CHECK", functions.when( //
						functions.lit(field.getFormatExpr()).isNotNull()
								.and(functions.expr(field.getFormatExpr()).isNull()),
						functions.lit(field.getFormatExpr() + "format check failed")));

				parsedDf = parsedDf.withColumn(fieldName,
						functions.when(
								functions.col("RDZ_NULL_CHECK").isNull()
										.and(functions.col("RDZ_FORMAT_CHECK").isNull()),
								functions.expr(field.getOutputExpression())));

			}
		}

		parsedDf.show();

	}

}

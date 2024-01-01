package com.fileproc.spark_jobs.sampleJob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class App {
	public static void DFToCSV(Dataset<Row> df, String filename) throws IOException {
		String tmpDir = filename + ".tmp";
		df.coalesce(1)//
				.write()//
				.format("csv").mode(SaveMode.Overwrite)//
				.option("header", "true")//
				.save(tmpDir);

		String fileName = Stream.of(new File(tmpDir).listFiles())//
				.filter(file -> file.getName().startsWith("part-0"))//
				.findFirst().get().getName();

		Files.move(Paths.get(tmpDir + "/" + fileName), Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);

		FileUtils.deleteDirectory(new File(tmpDir));

	}

	public static void main(String[] args) throws Exception {
		System.out.println("Hello World!");

		SparkSession spark = SparkSession.builder()//
				.master("local")//
				.appName("sampleJob")//
				.getOrCreate();

		// RDD
		// Dataframe
		// Dataset

		Dataset<Row> employees = spark//
				.read()//
				.option("header", true)//
				.csv("/home/keshore/Desktop/employees.csv")//
				.withColumn("age", functions.col("id"))//
				.withColumn("id", functions.expr("'Hello-' || id"))//
		;

		DFToCSV(employees, "/home/keshore/Desktop/employees_out.csv");

		employees.printSchema();

		employees.show();
	}
}

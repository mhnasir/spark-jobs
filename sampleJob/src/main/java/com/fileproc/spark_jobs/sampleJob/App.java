package com.fileproc.spark_jobs.sampleJob;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		SparkSession spark = SparkSession.builder()//
				.master("local")//
				.appName("sampleJob")//
				.getOrCreate();

		Dataset<Row> employees = spark.read()//
				.option("header", true)//
				.csv("/home/keshore/Desktop/employees.csv");

		employees.printSchema();

		employees.show();
	}
}

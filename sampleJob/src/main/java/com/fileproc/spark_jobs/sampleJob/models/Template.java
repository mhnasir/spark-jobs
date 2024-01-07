package com.fileproc.spark_jobs.sampleJob.models;

import java.util.List;
import java.util.Map;

public class Template {

	private String inputFileDelimiter;
	private String outputFileDelimiter;
	private Map<String, TemplateFields> fields;

	public String getInputFileDelimiter() {
		return inputFileDelimiter;
	}

	public void setInputFileDelimiter(String inputFileDelimiter) {
		this.inputFileDelimiter = inputFileDelimiter;
	}

	public String getOutputFileDelimiter() {
		return outputFileDelimiter;
	}

	public void setOutputFileDelimiter(String outputFileDelimiter) {
		this.outputFileDelimiter = outputFileDelimiter;
	}

	public Map<String, TemplateFields> getFields() {
		return fields;
	}

	public void setFields(Map<String, TemplateFields> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "Template [inputFileDelimiter=" + inputFileDelimiter + ", outputFileDelimiter=" + outputFileDelimiter
				+ ", fields=" + fields + "]";
	}

}

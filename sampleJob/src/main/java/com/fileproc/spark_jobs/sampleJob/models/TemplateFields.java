package com.fileproc.spark_jobs.sampleJob.models;

public class TemplateFields {

	private String defaultValue;
	private String mandatory;
	private String dataType;
	private String outputExpression;
	private String formatExpr;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getOutputExpression() {
		return outputExpression;
	}

	public void setOutputExpression(String outputExpression) {
		this.outputExpression = outputExpression;
	}

	public String getFormatExpr() {
		return formatExpr;
	}

	public void setFormatExpr(String formatExpr) {
		this.formatExpr = formatExpr;
	}

	@Override
	public String toString() {
		return "TemplateFields [defaultValue=" + defaultValue + ", mandatory=" + mandatory + ", dataType=" + dataType
				+ ", outputExpression=" + outputExpression + ", formatExpr=" + formatExpr + "]";
	}

}

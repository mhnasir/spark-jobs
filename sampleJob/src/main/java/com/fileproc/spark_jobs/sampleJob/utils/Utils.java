package com.fileproc.spark_jobs.sampleJob.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import com.fileproc.spark_jobs.sampleJob.models.Template;

public class Utils {

	public static Template parseTemplateYamlFile(String inputPath) throws FileNotFoundException {

		return parseTemplateYamlFile(new FileInputStream(new File(inputPath)));
	}

	public static Template parseTemplateYamlFile(InputStream inputStream) throws FileNotFoundException {

		LoaderOptions loaderOptions = new LoaderOptions();
		DumperOptions dumperOptions = new DumperOptions();
		Representer representer = new Representer(dumperOptions);
		Constructor constructor = new Constructor(Template.class, loaderOptions);

		representer.getPropertyUtils().setSkipMissingProperties(true);

		Yaml yaml = new Yaml(constructor, representer, dumperOptions);

		Template template = yaml.load(inputStream);

		// Add code to check template is valid....

		return template;
	}

}

package com.demo_QA_API_Automation.bdd.Common_Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class ConfigReader {

	private ConfigReader() {

	}

	/**
	 * Loads properties file with multi-path fallback support (FileSystem &
	 * Classpath)
	 * 
	 * @param filePath
	 * @return Properties
	 */
	public static Properties loadPropertyFile(String filePath) {
		Properties prop = new Properties();
		File file = new File(filePath);

		// Try loading from file system path directly
		if (file.exists()) {
			try (FileInputStream fileInput = new FileInputStream(file)) {
				prop.load(fileInput);
				return prop;
			} catch (Exception e) {
				// Fallback to secondary methods below
			}
		}

		// Secondary fallback: Try classloader resource stream
		try (InputStream resourceStream = ConfigReader.class.getClassLoader()
				.getResourceAsStream(filePath.replace("src/test/resources/", ""))) {
			if (resourceStream != null) {
				prop.load(resourceStream);
			}
		} catch (Exception e) {
			// Catch silently to let caller handle empty properties
		}

		return prop;
	}

	/**
	 * Get String value from standard config file
	 * 
	 * @param key
	 * @return String value
	 */
	public static String getValue(String key) {
		Properties prop = loadPropertyFile("src/test/resources/Config/config.properties");
		if (prop.isEmpty()) {
			prop = loadPropertyFile("src/test/resources/config.properties");
		}
		String val = prop.getProperty(key);
		return val != null ? val.trim() : null;
	}

	/**
	 * Alias for getValue(key) to satisfy framework calls Supports System property
	 * overrides (-Dkey=value)
	 * 
	 * @param key
	 * @return String value
	 */
	public static String getProperty(String key) {
		String sysProp = System.getProperty(key);
		if (sysProp != null && !sysProp.trim().isEmpty()) {
			return sysProp.trim();
		}
		return getValue(key);
	}

	/**
	 * Get String value from API config file
	 * 
	 * @param key
	 * @return String value
	 */
	public static String getAPIValue(String key) {

		Properties prop = loadPropertyFile("src/test/resources/Config_file/configAPI.properties");

		if (prop.isEmpty()) {
			prop = loadPropertyFile("src/test/resources/configAPI.properties");
		}

		String val = prop.getProperty(key);

		return val != null ? val.trim() : null;
	}

	/**
	 * Alias for getAPIValue(key) supporting System property overrides (-Dkey=value)
	 * 
	 * @param key
	 * @return String value
	 */
	public static String getAPIProperty(String key) {
		String sysProp = System.getProperty(key);
		if (sysProp != null && !sysProp.trim().isEmpty()) {
			return sysProp.trim();
		}
		return getAPIValue(key);
	}

	/**
	 * Get int value from properties file
	 * 
	 * @param key
	 * @return int value
	 */
	public static int getIntValue(String key) {
		String strKey = getProperty(key);
		return strKey != null ? Integer.parseInt(strKey.trim()) : 0;
	}

	/**
	 * Get String value from custom properties path
	 * 
	 * @param path
	 * @param key
	 * @return String value
	 */
	public static String getValue2(String path, String key) {
		Properties prop = loadPropertyFile(path);
		String val = prop.getProperty(key);
		return val != null ? val.trim() : null;
	}

	/**
	 * Set String value into properties file
	 * 
	 * @param path
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public static void setStringValue(String path, String key, String value) throws IOException {
		Properties prop = loadPropertyFile(path);
		try (FileOutputStream output = new FileOutputStream(path)) {
			prop.setProperty(key, value);
			prop.store(output, null);
		}
	}

	public static void moveFile(String outFileName, String sourcePath, String destinationPath, String fileextention)
			throws IOException {
		File source = new File(sourcePath);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		String ts = sdf.format(source.lastModified());
		File destination = new File(destinationPath + outFileName + ts);
		FileUtils.copyFile(source, destination);
		System.out.println(" new file name is " + outFileName);
	}
}
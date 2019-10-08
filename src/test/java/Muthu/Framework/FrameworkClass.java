package Muthu.Framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.DataProvider;

public class FrameworkClass extends initialVariables {
	/****
	 * 
	 * Method will handle excel(.xlsx or .xls)
	 */
	public static void ExcelHandler() throws IOException {

		reader = new BufferedReader(
				new FileReader("C:\\Users\\Muthukumar\\eclipse-workspace\\Framework\\FrameWorkProperties.properties"));
		property = new Properties();
		property.load(reader);
		String fpath = property.getProperty("ORPath");
		File file = new File(fpath);
		FileInputStream inputStream = new FileInputStream(file);

		Wb = null;

		// Find the file extension by splitting file name in substring and getting only
		// extension name

		String fileExtensionName = fpath.substring(fpath.indexOf("."));

		// Check condition if the file is xlsx file

		if (fileExtensionName.equals(".xlsx")) {

			// If it is xlsx file then create object of XSSFWorkbook class

			Wb = new XSSFWorkbook(inputStream);

		}

		// Check condition if the file is xls file

		else if (fileExtensionName.equals(".xls")) {

			// If it is xls file then create object of HSSFWorkbook class

			Wb = new HSSFWorkbook(inputStream);

		}

		String fdpath = property.getProperty("testData");
		File file_data = new File(fdpath);
		FileInputStream inputStream_data = new FileInputStream(file_data);

		WbTestData = null;

		// Find the file extension by splitting file name in substring and getting only
		// extension name

		String fileExtensionName_data = fdpath.substring(fdpath.indexOf("."));

		// Check condition if the file is xlsx file

		if (fileExtensionName_data.equals(".xlsx")) {

			// If it is xlsx file then create object of XSSFWorkbook class

			WbTestData = new XSSFWorkbook(inputStream_data);

		}

		// Check condition if the file is xls file

		else if (fileExtensionName_data.equals(".xls")) {

			// If it is xls file then create object of HSSFWorkbook class

			WbTestData = new HSSFWorkbook(inputStream_data);

		}

	}

	/****
	 * 
	 * @param obj
	 * @param sheetName
	 * @return obj
	 */

	public static By getObj(String obj, String sheetName) {
		String objValue = null;
		sheet = Wb.getSheet(sheetName);
		int cCount = 0;
		By ByEle = null;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row.getCell(0).toString().equals(obj)) {
				for (int j = 1; j <= row.getLastCellNum(); j++) {
					cell_Val = row.getCell(j);
					if (cell_Val != null && cell_Val.getCellType() == Cell.CELL_TYPE_STRING) {
						objValue = cell_Val.toString();
						cCount = j;
						break;
						//
					}
				}
				break;
			}
		}

		// code to choose the By

		switch (cCount) {

		case 1:
			return ByEle.id(objValue);
		case 2:
			return ByEle.name(objValue);
		case 3:
			return ByEle.xpath(objValue);
		case 4:
			return ByEle.linkText(objValue);
		case 5:
			return ByEle.partialLinkText(objValue);
		case 6:
			return ByEle.tagName(objValue);
		case 7:
			return ByEle.className(objValue);
		default:
			return null;
		}

	}

	/***
	 * 
	 * @param tcName
	 * @return testData as HashMap
	 */
	public static HashMap testDatahandler(String tcName) {

		HashMap<String, String> hm = new HashMap<String, String>();

		sheetTestData = WbTestData.getSheetAt(0);

		int rowDatano = 0;
		for (int k = 0; k <= sheetTestData.getLastRowNum(); k++) {
			rowTestKey = sheetTestData.getRow(k);

			if (rowTestKey.getCell(0).toString().trim().equals(tcName)) {
				rowDatano = k;
				break;
			}
		}

		rowTestKey = sheetTestData.getRow(0);
		rowTestVal = sheetTestData.getRow(rowDatano);

		for (int j = 0; j < rowTestKey.getLastCellNum(); j++) {

			cell_ValTestDatakey = rowTestKey.getCell(j);
			cell_ValTestDataVal = rowTestVal.getCell(j, org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
			hm.put(cell_ValTestDatakey.toString().trim(), cell_ValTestDataVal.toString().trim());

		}

		return hm;

	}

	/**
	 * 
	 * @return Testcase Name by using the @test which is invoked
	 */
	public static String TCName() {
		TCname = Thread.currentThread().getStackTrace()[2].getMethodName().toString();
		return TCname;
	}

	public static void ValidationScreenshot() throws IOException {
		String strPath = System.getProperty("user.dir");

		strPath = strPath + "\\Screenshots";
		File fpath = new File(strPath);

		if (fpath.exists())

		{
			strPath = strPath + "\\" + TCname;
	
			if (new File(strPath).exists()) 
			{
				File fpath_sub = new File(strPath);
				for(File file: fpath_sub.listFiles()) { 
				    if (!file.isDirectory()) 
				        file.delete();
				}
				File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				File DestFile = new File(fpath_sub + "\\" + System.currentTimeMillis());
				
				FileHandler.copy(screenshotFile, DestFile);
			}
			else
			{
				new File(strPath).mkdir();
				File fpath_sub = new File(strPath);
				File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				File DestFile = new File(fpath_sub + "\\" + System.currentTimeMillis());
				FileHandler.copy(screenshotFile, DestFile);
			}
			
		
		}
		
		else
			{
				fpath.mkdir();
				strPath = strPath + "\\" + TCname;
				new File(strPath).mkdir();
				File fpath_sub = new File(strPath);
				File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				File DestFile = new File(fpath_sub + "\\" + System.currentTimeMillis());
				FileHandler.copy(screenshotFile, DestFile);

			}
		

	}

}
package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelDatareader1 {
    private static String FILE_PATH = "src/test/resources/TestData.xlsx";
    //private static String  testName = System.getProperty("sheetName");
    @DataProvider(name = "heroData")
    //@Parameters({"sheetName"})
    public static Object[][] readHeroData() {
        String testName = "APITest";
       // System.out.println("Sheetname is ........... : "+sheetName);
        List<Map<String, Object>> testDataList = readTestData(testName);

        Object[][] testDataArray = new Object[testDataList.size()][1];
        for (int i = 0; i < testDataList.size(); i++) {
            testDataArray[i][0] = testDataList.get(i);
        }

        return testDataArray;
    }
    @DataProvider(name = "heroData1withVoucher")
    //@Parameters({"sheetName"})
    public static Object[][] readHeroDataWithVoucher() {
        String testName = "APITest1";
        // System.out.println("Sheetname is ........... : "+sheetName);
        List<Map<String, Object>> testDataList = readTestData(testName);

        Object[][] testDataArray = new Object[testDataList.size()][1];
        for (int i = 0; i < testDataList.size(); i++) {
            testDataArray[i][0] = testDataList.get(i);
        }

        return testDataArray;
    }
    @DataProvider(name = "heroData2withVoucher")
       public static Object[][] readinvalidHeroDataWithVoucher() {
        String testName = "APITest2";
        // System.out.println("Sheetname is ........... : "+sheetName);
        List<Map<String, Object>> testDataList = readTestData(testName);

        Object[][] testDataArray = new Object[testDataList.size()][1];
        for (int i = 0; i < testDataList.size(); i++) {
            testDataArray[i][0] = testDataList.get(i);
        }

        return testDataArray;
    }
    private static List<Map<String, Object>> readTestData(String testName) {
        List<Map<String, Object>> testDataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(testName);
            if (sheet == null) {
                throw new RuntimeException("Test sheet not found: " + testName);
            }

            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow != null) {
                    Map<String, Object> rowData = new HashMap<>();

                    for (int j = 0; j < headerRow.getLastCellNum(); j++) {

                        Cell currentCell = currentRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                        String header = headerRow.getCell(j).getStringCellValue().trim();
                        Object cellValue;

                        switch (currentCell.getCellType()) {
                            case STRING:
                                cellValue = currentCell.getStringCellValue().trim();
                                break;
                            case NUMERIC:
                                cellValue = currentCell.getNumericCellValue();
                                break;
                            case BOOLEAN:
                                cellValue = currentCell.getBooleanCellValue();
                                break;
                            case FORMULA:
                                cellValue = currentCell.getCellFormula();
                                break;
                            default:
                                cellValue = null;
                        }

                        rowData.put(header, cellValue);
                    }

                    testDataList.add(rowData);
                }
                else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return testDataList;
    }
}
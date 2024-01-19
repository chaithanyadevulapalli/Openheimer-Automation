package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataReader {

    @DataProvider(name = "heroData")
    public static Object[][] readHeroData() {
        try {
            String filePath = "src/test/resources/TestData.xlsx";
            String sheetName = System.getProperty("sheetName"); // Get sheetName from system property
            String testName = System.getProperty("testName"); // Get testName from system property

            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            Map<String, Integer> colIndexMap = new HashMap<>();
            Row headerRow = sheet.getRow(0);

            for (int col = 0; col < colCount; col++) {
                colIndexMap.put(headerRow.getCell(col).getStringCellValue(), col);
            }

            int testNameColIndex = colIndexMap.get("TestName");

            int dataRowCount = 0;

            for (int row = 1; row < rowCount; row++) {
                if (sheet.getRow(row).getCell(testNameColIndex).getStringCellValue().equalsIgnoreCase(testName)) {
                    dataRowCount++;
                }
            }

            Object[][] data = new Object[dataRowCount][colCount];
            int dataIndex = 0;
            for (int row = 1; row < rowCount; row++) {
                if (sheet.getRow(row).getCell(testNameColIndex).getStringCellValue().equalsIgnoreCase(testName)) {
                    Row dataRow = sheet.getRow(row);
                    for (int col = 0; col < colCount; col++) {
                        Cell cell = dataRow.getCell(col);
                        data[dataIndex][col] = getCellValue(cell);
                    }
                    dataIndex++;
                }
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}


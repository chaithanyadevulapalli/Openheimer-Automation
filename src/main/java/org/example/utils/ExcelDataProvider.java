package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelDataProvider {
    private static String excelFilePath = "src/test/resources/TestExecutionData.xlsx";

    @DataProvider(name = "excelDataprovider")
    public static Object[][] getData(Method method) throws IOException {
        String methodName = method.getName();
        String sheetName = "APITestData";
        return getTestDataForTestMethod(excelFilePath, sheetName, methodName);
    }

    private static Object[][] getTestDataForTestMethod(String filePath, String sheetName, String testMethodName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheet(sheetName);

        List<Map<String, Object>> data = new ArrayList<>();

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip headers

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell testNameCell = row.getCell(0); // Assuming the test name is in the first column

            if (testNameCell != null && testNameCell.getStringCellValue().trim().equals(testMethodName)) {
                Map<String, Object> rowData = new HashMap<>();

                // Fill the data map with the row values
                for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    String colName = getColumnName(sheet, colIndex);
                    rowData.put(colName, getCellValue(cell));
                }
                data.add(rowData);
            }
        }

        return convertDataListToArray(data);
    }

    private static Object[][] convertDataListToArray(List<Map<String, Object>> data) {
        Object[][] dataArray = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i][0] = data.get(i);
        }
        return dataArray;
    }

    private static String getColumnName(Sheet sheet, int colIndex) {
        Row headerRow = sheet.getRow(0);
        Cell headerCell = headerRow.getCell(colIndex);
        return headerCell.getStringCellValue();
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }
}

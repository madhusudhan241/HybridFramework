package org.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static List<Map<String, String>> getTestData(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                Map<String, String> rowMap = new HashMap<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    Cell headerCell = headerRow.getCell(j);
                    Cell dataCell = dataRow.getCell(j);

                    String key = headerCell.getStringCellValue();
                    String value = getCellValueAsString(dataCell);

                    rowMap.put(key, value);
                }
                dataList.add(rowMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    @DataProvider(name = "excelData")
    public Object[][] getData() {
        List<Map<String, String>> dataList = ExcelUtils.getTestData("src/test/resources/testdata/LoginData.xlsx", "Sheet1");
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }
}

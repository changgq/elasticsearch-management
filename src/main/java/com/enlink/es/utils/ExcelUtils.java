package com.enlink.es.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

/**
 * Excel工具类
 *
 * @author changgq
 */
public class ExcelUtils {

    /**
     * 根据路径获取Excel的结果集合
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelData(String filePath) throws Exception {
        return getExcelData(filePath, 0, 0);
    }

    /**
     * 根据路径获取Excel的结果集合
     *
     * @param filePath
     * @param sheetNum
     * @param startRow
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelData(String filePath, int sheetNum, int startRow) throws Exception {
        return getExcelData2Map(new File(filePath), 0, 0);
    }

    /**
     * 根据File获取Excel的结果集合
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelData(File file) throws Exception {
        return getExcelData(file, 0, 0);
    }

    /**
     * 根据File获取Excel的结果集合
     *
     * @param file
     * @param sheetNum
     * @param startRow
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelData(File file, int sheetNum, int startRow) throws Exception {
        return getExcelData2Map(file, sheetNum, startRow);
    }

    /**
     * 根据输入流获取Excel的结果集合
     *
     * @param input
     * @param isXls
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelData(FileInputStream input, boolean isXls) throws Exception {
        return getExcelData(input, isXls, 0, 0);
    }

    /**
     * 根据输入流获取Excel的结果集合
     *
     * @param input
     * @param isXls
     * @param sheetNum
     * @param startRow
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> getExcelData(FileInputStream input, boolean isXls, int sheetNum,
                                                         int startRow) throws Exception {
        return getExcelData2Map(input, isXls, sheetNum, startRow);
    }

    /**
     * 获取Excel固定单元格的值
     *
     * @param input
     * @param isXls
     * @param cellNos
     * @return
     */
    public static Map<String, String> getCellValues(FileInputStream input, boolean isXls, List<String> cellNos) {
        Map<String, String> map = new HashMap<String, String>();
        Workbook wb = getWorkbook(input, isXls);
        if (!wb.isSheetHidden(0)) {
            Sheet sheet = wb.getSheetAt(0);
            for (String cn : cellNos) {
                String[] cells = cn.split("_");
                if (cells.length > 1) {
                    Row row = sheet.getRow(Integer.valueOf(cells[1]) - 1);
                    map.put(cn, getContent(row, getColumnNum(cells[0]) - 1));
                }
            }
        }
        return map;
    }

    /**
     * 从startRow开始读取Excel数据
     *
     * @param file
     * @param sheetNum
     * @param startRow
     * @return
     * @throws Exception
     */
    private static List<Map<String, String>> getExcelData2Map(File file, int sheetNum, int startRow) throws Exception {
        try (FileInputStream input = new FileInputStream(file)) {
            return getExcelData2Map(input, file.getName().toLowerCase().endsWith("xls"), sheetNum, startRow);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从startRow开始读取Excel数据
     *
     * @param input
     * @param isXls
     * @param sheetNum
     * @param startRow
     * @return
     * @throws Exception
     */
    private static List<Map<String, String>> getExcelData2Map(FileInputStream input, boolean isXls, int sheetNum,
                                                              int startRow) throws Exception {
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        Workbook wb = null;
        wb = getWorkbook(input, isXls);
        int sheetNums = wb.getNumberOfSheets();
        if (sheetNum < sheetNums) {
            Sheet sheet = wb.getSheetAt(sheetNum);
            if (!wb.isSheetHidden(sheetNum)) {
                int rows = sheet.getLastRowNum() + 1;
                // 循环遍历每一行的数据
                for (int j = startRow; j < rows; j++) {
                    Row r = sheet.getRow(j);
                    if (null != r) {
                        // 获得总列数
                        int columns = r.getPhysicalNumberOfCells();
                        Map<String, String> excelMap = new HashMap<String, String>();
                        // 遍历该行，所有的列
                        for (int k = 0; k < columns; k++) {
                            excelMap.put(findCurrentColumnChar(k).toUpperCase(), StringUtils.trimWhitespace(getContent(r, k)));
                        }
                        listMap.add(excelMap);
                    }
                }
            }
        } else {
            throw new Exception("您输入的sheet序号有误！请确认该sheet是否存在！");
        }
        return listMap;
    }

    /**
     * 根据文件输入流获取excel的Workbook对象
     *
     * @param input
     * @param isXls
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbook(FileInputStream input, boolean isXls) {
        Workbook wb = null;
        try {
            // 根据文件格式(2003或者2007)来初始化
            if (isXls) {
                wb = new HSSFWorkbook(input);
            } else {
                wb = new XSSFWorkbook(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 获得某一数字的char值
     *
     * @param column
     * @return
     */
    private static String findCurrentColumnChar(int column) {
        return (char) (column + 65) + "";
    }

    /**
     * 获取Excel的列序号
     *
     * @param columnName
     * @return
     */
    private static int getColumnNum(String columnName) {
        int columnNum = 0;
        char[] columns = columnName.toCharArray();
        for (int index = 0; index < columns.length; index++) {
            int ch = columnName.charAt(columns.length - index - 1);
            columnNum += (int) (ch - 'A' + 1) * Math.pow(26, index);
        }
        return columnNum;
    }

    /**
     * 获取单元格内容
     *
     * @param row
     * @param cellNo
     * @return
     */
    private static String getContent(Row row, int cellNo) {
        Cell cell = row.getCell(cellNo);
        String cellContent = "";
        if (null != cell) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    cellContent = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    cellContent = String.valueOf(cell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    cellContent = String.valueOf(cell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    try {
                        cellContent = cell.getStringCellValue();
                    } catch (IllegalStateException e) {
                        try {
                            cellContent = String.valueOf(cell.getNumericCellValue());
                        } catch (IllegalStateException le) {
                            cellContent = "";
                        }
                    }
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    cellContent = "";
                    break;
                default:
                    cellContent = "";
                    break;
            }
        }
        return cellContent;
    }
}

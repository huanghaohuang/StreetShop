package com.street.shop.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.SimpleDateFormat;

public class ExcelUtil {


    /**
     * 是否是合并区域
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getMergedRegionValue(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
        return null;
    }

    public static String getCellValue(Cell cell) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String cellValue = "";
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_STRING: // 文本
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC: // 数字、日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = fmt.format(cell.getDateCellValue()); // 日期型
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = cell.getStringCellValue(); // 数字
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK: // 空白
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_ERROR: // 错误
                cellValue = "错误";
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                //获取公式的值
                Workbook wb = cell.getSheet().getWorkbook();
                if(wb != null) {
                    FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
                    if(evaluator != null){
                        evaluator.evaluateFormulaCell(cell);
                        cellValue = cell.getNumericCellValue() + ""; // 数字, 浮点数
                    }
                }
                break;
            default:
                cellValue = "错误";
        }
        return cellValue;
    }

    public static boolean isNumber(String s){//合法数字返回true
        //这个正则表达式能够过滤0.0.0、8-99这种不合法的数字
        String reg="^[-\\\\+]?([0-9]+\\\\.?)?[0-9]+$";
        //String reg="^(\\-|\\+)?\\d+(\\.\\d+)?$";   这个也可以
        return  s.matches(reg);
    }
}

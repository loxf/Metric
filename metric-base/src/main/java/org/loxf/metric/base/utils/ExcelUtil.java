package org.loxf.metric.base.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/7/22.
 */
public class ExcelUtil {

    public static List<String> loadTitle(InputStream is, String fileName) throws IOException {
        ArrayList set = new ArrayList();

        ArrayList wb;
        try {
            if(null != is) {
                wb = null;
                String ext = getExtensionName(fileName);
                Object var13;
                if("xls".equals(ext)) {
                    var13 = new HSSFWorkbook(is);
                } else {
                    if(!"xlsx".equals(ext)) {
                        throw new RuntimeException("必须为xls,xlsx类型");
                    }

                    var13 = new XSSFWorkbook(is);
                }

                Sheet sheet = ((Workbook)var13).getSheetAt(0);
                int rowNumb = sheet.getFirstRowNum();
                Row row = sheet.getRow(rowNumb);
                short cellNumb = row.getLastCellNum();

                for(int i = 0; i < cellNumb; ++i) {
                    set.add(row.getCell(i).toString().trim());
                }

                return set;
            }

            wb = set;
        } finally {
            if(null != is) {
                is.close();
            }

        }

        return wb;
    }

    public static List<List<String>> loadData(InputStream is, String fileName) throws IOException {
        ArrayList dataList = new ArrayList();

        ArrayList wb;
        try {
            if(null != is) {
                wb = null;
                String ext = getExtensionName(fileName);
                Object var19;
                if("xls".equals(ext)) {
                    var19 = new HSSFWorkbook(is);
                } else {
                    if(!"xlsx".equals(ext)) {
                        throw new RuntimeException("必须为xls,xlsx类型");
                    }

                    var19 = new XSSFWorkbook(is);
                }

                Sheet sheet = ((Workbook)var19).getSheetAt(0);
                int rowNumb = sheet.getFirstRowNum();
                int lastRowNumb = sheet.getLastRowNum();

                for(int i = rowNumb + 1; i <= lastRowNumb; ++i) {
                    Row row = sheet.getRow(i);
                    short cellNumb = row.getLastCellNum();
                    ArrayList data = new ArrayList();

                    for(int j = 0; j < cellNumb; ++j) {
                        Cell cell = row.getCell(j);
                        if(null == cell) {
                            data.add("");
                        } else if(cell.getCellType() == 0) {
                            if(HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date d = cell.getDateCellValue();
                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                data.add(formater.format(d));
                            } else {
                                cell.setCellType(1);
                                data.add(cell.getStringCellValue());
                            }
                        } else {
                            data.add(cell.getStringCellValue());
                        }
                    }

                    dataList.add(data);
                }

                return dataList;
            }

            wb = dataList;
        } finally {
            is.close();
        }

        return wb;
    }

    private static String getExtensionName(String filename) {
        if(filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if(dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1).toLowerCase();
            }
        }

        return filename;
    }
}

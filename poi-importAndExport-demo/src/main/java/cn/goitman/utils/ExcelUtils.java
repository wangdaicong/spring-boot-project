package cn.goitman.utils;

import cn.goitman.annotation.EnableExport;
import cn.goitman.annotation.EnableExportField;
import cn.goitman.annotation.EnableSelectList;
import cn.goitman.annotation.ImportIndex;
import cn.goitman.enums.ColorEnum;
import com.alibaba.fastjson.util.TypeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nicky
 * @version 1.0
 * @className ExcelUtils
 * @blog goitman.cn | blog.csdn.net/minkeyto
 * @description 导入导出工具类
 * @date 2021/9/22 15:40
 */
public class ExcelUtils {

    /*
     * 存储下拉列表数据，key对应Excel列序号（从0开始），value为下拉列表值
     */
    public static final Map<Integer, Map<String, String>> ALL_SELECT_LIST_MAP = new HashMap<Integer, Map<String, String>>();

    /**
     * @param [excel 文件, clazz pojo类型]
     * @return java.util.List<?>
     * @method parseExcelToList
     * @description 导入，将Excel数据转换为集合对象
     */
    public static List<?> parseExcelToList(File excel, Class clazz) {
        List<Object> res = new ArrayList<>();
        // 创建输入流
        InputStream is = null;
        // 创建工作表
        Sheet sheet = null;
        try {
            is = new FileInputStream(excel.getAbsolutePath());
            if (is != null) {
                // 创建工作簿
                Workbook workbook = WorkbookFactory.create(is);
                // 获取第一个工作表
                sheet = workbook.getSheetAt(0);
                if (sheet != null) {
                    int i = 1;
                    String values[];
                    // 获取第二行数据，第一行为标题
                    Row row = sheet.getRow(i);
                    while (row != null) {
                        // 获取总列数
                        int cellNum = row.getPhysicalNumberOfCells();
                        values = new String[cellNum];
                        for (int j = 0; j <= cellNum; j++) {
                            // 获取单元格数据
                            Cell cell = row.getCell(j);
                            String value = null;
                            if (cell != null) {
                                // 将单元格数据类型设置为字符串
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                value = cell.getStringCellValue() == null ? null : cell.getStringCellValue();
                                values[j] = value;
                            }
                        }
                        // 反射获取类中所有声明字段
                        Field[] fields = clazz.getDeclaredFields();
                        Object obj = clazz.newInstance();
                        for (Field f : fields) {
                            // 判断ImportIndex注解是否在此字段上，true为存在
                            if (f.isAnnotationPresent(ImportIndex.class)) {
                                ImportIndex annotation = f.getAnnotation(ImportIndex.class);
                                // 获取索引值
                                int index = annotation.index();
                                // 获取方法名
                                String useSetMethodName = annotation.useSetMethodName();
                                if (!"".equals(useSetMethodName)) {
                                    // fastjson TypeUtils工具类，实现常用数据类型和对象间的相互转换
                                    Object val = TypeUtils.cast(values[index], f.getType(), null);
                                    // 取消Java语言访问检查
                                    f.setAccessible(true);
                                    // 参数一：方法名，参数二：方法参数数组
                                    Method method = clazz.getMethod(useSetMethodName, new Class[]{f.getType()});
                                    // 暴力访问
                                    method.setAccessible(true);
                                    // 返回值是Object接收，参数一：对象是谁，参数二：调用该方法的实际参数
                                    method.invoke(obj, new Object[]{val});
                                } else {
                                    f.setAccessible(true);
                                    Object val = TypeUtils.cast(values[index], f.getType(), null);
                                    // 将指定对象上此 Field字段设置为新值。参数一：指定对象，参数二：新值
                                    f.set(obj, val);
                                }
                            }
                        }
                        // 将对象数据保存至集合
                        res.add(obj);
                        i++;
                        // 遍历下一行数据
                        row = sheet.getRow(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param [excel 文件输入流, clazz pojo类型]
     * @return java.util.List<?>
     * @method parseExcelToList
     * @description 导入，将Excel数据转换为集合对象，另校验EnableSelectList注解或ImportIndex注解useSetMethodName属性
     */
    public static List<?> parseExcelToList(InputStream excel, Class clazz) {
        List<Object> res = new ArrayList<>();
        InputStream is = null;
        Sheet sheet = null;
        try {
            is = excel;
            if (is != null) {
                Workbook workbook = WorkbookFactory.create(is);
                sheet = workbook.getSheetAt(0);
                if (sheet != null) {
                    int i = 1;
                    String values[];
                    Row row = sheet.getRow(i);
                    while (row != null) {
                        int cellNum = row.getPhysicalNumberOfCells();
                        values = new String[cellNum];
                        for (int j = 0; j < cellNum; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                String value = cell.getStringCellValue() == null ? null : cell.getStringCellValue();
                                values[j] = value;
                            }
                        }
                        Field[] fields = clazz.getDeclaredFields();
                        Object obj = clazz.newInstance();
                        for (Field f : fields) {
                            if (f.isAnnotationPresent((ImportIndex.class))) {
                                ImportIndex annotation = f.getAnnotation(ImportIndex.class);
                                int index = annotation.index();
                                Object value = values[index];
                                // 判断EnableSelectList注解是否在此字段上，true为存在
                                if (f.isAnnotationPresent(EnableSelectList.class)) {
                                    // 根据索引获取下拉列表值
                                    value = getKeyByValue(ALL_SELECT_LIST_MAP.get(index), String.valueOf(value));
                                }
                                String useSetMethodName = annotation.useSetMethodName();
                                if (!"".equals(useSetMethodName)) {
                                    Object val = TypeUtils.cast(value, f.getType(), null);
                                    f.setAccessible(true);
                                    Method method = clazz.getMethod(useSetMethodName, new Class[]{f.getType()});
                                    method.setAccessible(true);
                                    method.invoke(obj, new Object[]{val});
                                } else {
                                    f.setAccessible(true);
                                    Object val = TypeUtils.cast(value, f.getType(), null);
                                    f.set(obj, val);
                                }
                            }
                        }
                        res.add(obj);
                        i++;
                        row = sheet.getRow(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param [selectMap, value]
     * @return java.lang.String
     * @method getKeyByValue
     * @description 通过value获取key值
     */
    private static String getKeyByValue(Map<String, String> selectMap, String value) {
        if (selectMap != null) {
            for (Map.Entry<String, String> entry : selectMap.entrySet()) {
                if (value != null && value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
        } else {
            return value;
        }
        return null;
    }

    /**
     * @param [outputStream 输出流, dataList 导出的数据, clazz 导出数据的pojo类型, selectMap 下拉列表的列, exportTitle 标题]
     * @return void
     * @method exportExcel
     * @description 导出Excel
     */
    public static void exportExcel(HttpServletResponse response, List dataList, Class clazz, Map<Integer, Map<String, String>> selectMap, String exportTitle) {
        // 创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet();
        // 设置工作表行的默认高度
        sheet.setDefaultRowHeight((short) (20 * 20));
        // 判断当前类是否允许导出
        if (clazz.isAnnotationPresent(EnableExport.class)) {
            EnableExport export = (EnableExport) clazz.getAnnotation(EnableExport.class);
            // 所有列标题名称
            List<String> colNames = new ArrayList<>();
            // 所有列标题背景颜色
            List<ColorEnum> colors = new ArrayList<>();
            // 允许导出的字段
            List<Field> fieldList = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(EnableExportField.class)) {
                    EnableExportField enableExportField = field.getAnnotation(EnableExportField.class);
                    colNames.add(enableExportField.colName());
                    colors.add(enableExportField.cellColor());
                    fieldList.add(field);
                }
            }
            // 设置每列的宽度
            for (int i = 0; i < fieldList.size(); i++) {
                Field field = fieldList.get(i);
                sheet.setColumnWidth(i, field.getAnnotation(EnableExportField.class).colWidth() * 20);
            }

            HSSFRow hssfRow = null; // 表行
            HSSFCell hssfCell = null; // 单元格

            // 设置列标题
            String fileName = export.fileName();
            if (exportTitle != null) {
                fileName = exportTitle;
            }
            // 绘制标题，可选
            createTitle(workbook, hssfRow, hssfCell, sheet, colNames.size() - 1, fileName, export.cellColor());
            // 创建表头列名
            createHeadRow(workbook, hssfRow, hssfCell, sheet, colNames, colors);

            try {
                // 绘制单元格样式
                HSSFCellStyle cellStyle = getBasicCellStyle(workbook);
                // 插入数据
                int i = 0;
                for (Object obj : dataList) {
                    // 表头标题和列名已创建，所以从第三行开始
                    hssfRow = sheet.createRow(i + 2);
                    for (int j = 0; j < fieldList.size(); j++) {
                        Field field = fieldList.get(j);
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        EnableExportField enableExportField = field.getAnnotation(EnableExportField.class);
                        String getMethodName = enableExportField.useGetMethod();
                        if (!"".equals(getMethodName)) {
                            Method method = clazz.getMethod(getMethodName, new Class[]{field.getType()});
                            method.setAccessible(true);
                            method.invoke(obj, new Object[]{value});
                        }
                        if (field.isAnnotationPresent(EnableSelectList.class)) {
                            if (selectMap != null && selectMap.get(j) != null) {
                                value = selectMap.get(j).get(value);
                            }
                        }
                        setCellValue(value, hssfCell, hssfRow, cellStyle, j);
                    }
                    i++;
                }

                // 提供下载框，并设置文件名
                response.setContentType("octets/stream");
                // 防止中文文件名称乱码，需encode，并设置字符集
                response.setHeader("Content-Disposition", "attachment;filename=" +  java.net.URLEncoder.encode(fileName, "UTF-8") + ".xls");
                OutputStream outputStream = null;
                // 获取响应流
                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param [value 字段值, hssfCell 单元格, hssfRow 表行, cellStyle 单元格样式, cellIndex 单元格索引]
     * @return void
     * @method setCellValue
     * @description 设置单元格的值
     */
    private static void setCellValue(Object value, HSSFCell hssfCell, HSSFRow hssfRow, HSSFCellStyle cellStyle, int cellIndex) {
        String valuesStr = String.valueOf(value);
        hssfCell = hssfRow.createCell(cellIndex);
        if (isNumeric(valuesStr)) {
            // 数字数据
            hssfCell.setCellStyle(cellStyle);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            hssfCell.setCellValue(Double.valueOf(valuesStr));
        } else {
            // 字符串数据
            hssfCell.setCellStyle(cellStyle);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue(valuesStr);
        }
    }

    /**
     * @param [valuesStr 字段值]
     * @return boolean
     * @method isNumeric
     * @description 判断字符串是否为数字
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        if (str != null && !"".equals(str.trim())) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()) {
                if (!str.contains(".") && str.startsWith("0")) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * @param [workbook 工作簿, hssfRow 行, hssfCell 单元格, sheet 工作表, colNames 列名集合, colors 单元格颜色集合]
     * @return void
     * @method createHeadRow
     * @description 设置表头标题和高度
     */
    private static void createHeadRow(HSSFWorkbook workbook, HSSFRow hssfRow, HSSFCell hssfCell, HSSFSheet sheet, List<String> colNames, List<ColorEnum> colors) {
        hssfRow = sheet.createRow(1);
        for (int i = 0; i < colNames.size(); i++) {
            hssfCell = hssfRow.createCell(i);
            hssfCell.setCellStyle(getTitleCellStyle(workbook, colors.get(i)));
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue(colNames.get(i));
        }
    }

    /**
     * @param [workbook 工作簿, hssfRow 行, hssfCell 单元格, sheet 工作表, i 跨列数, fileName 标题名, cellColor 单元格颜色]
     * @return void
     * @method createTitle
     * @description 绘制一个跨列的标题行
     */
    private static void createTitle(HSSFWorkbook workbook, HSSFRow hssfRow, HSSFCell hssfCell, HSSFSheet sheet, int i, String fileName, ColorEnum cellColor) {
        // 合并单元格，参数依次是：起始行号，终止行号， 起始列号，终止列号（索引从0开始）
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, i);
        sheet.addMergedRegion(cra);
        // 为合并单元格绘制边框
        RegionUtil.setBorderBottom(1, cra, sheet, workbook);
        RegionUtil.setBorderLeft(1, cra, sheet, workbook);
        RegionUtil.setBorderRight(1, cra, sheet, workbook);
        RegionUtil.setBorderTop(1, cra, sheet, workbook);

        // 设置表头
        hssfRow = sheet.getRow(0);
        hssfCell = hssfRow.getCell(0);
        // 设置单元格风格
        hssfCell.setCellStyle(getTitleCellStyle(workbook, cellColor));
        // 设置单元格类型为字符串
        hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        // 设置单元格的值
        hssfCell.setCellValue(fileName);
    }

    /**
     * @param [workbook, cellColor]
     * @return org.apache.poi.hssf.usermodel.HSSFCellStyle
     * @method getTitleCellStyle
     * @description 获取带有背景色的标题单元格
     */
    private static HSSFCellStyle getTitleCellStyle(HSSFWorkbook workbook, ColorEnum cellColor) {
        HSSFCellStyle cellStyle = getBasicCellStyle(workbook);
        // 设置背景色
        cellStyle.setFillForegroundColor(cellColor.getIndex());
        // 设置图案样式
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }

    /**
     * @param [workbook]
     * @return HSSFCellStyle
     * @method getBasicCellStyle
     * @description 绘制单元格
     */
    private static HSSFCellStyle getBasicCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 绘制单元格边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置水平居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 设置自动换行
        cellStyle.setWrapText(true);
        return cellStyle;
    }
}

package com.example.teacherhelper;

import com.example.teacherhelper.annotations.ExcelContent;
import com.example.teacherhelper.annotations.ExcelContentCellFormat;
import com.example.teacherhelper.annotations.ExcelSheet;
import com.example.teacherhelper.annotations.ExcelTitleCellFormat;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

@ExcelSheet(sheetName = "学生表")
public class StudentExcelBen {
    @ExcelContent(titleName = "学号")
    private String stuId;

    @ExcelContent(titleName = "姓名")
    private String stuName;

    @ExcelContent(titleName = "班级")
    private String stuClass;

    public StudentExcelBen(String studentID, String studentName, String studentClass) {
        this.stuId = studentID;
        this.stuName = studentName;
        this.stuClass = studentClass;
    }


    @ExcelTitleCellFormat(titleName = "姓名")
    private static WritableCellFormat getTitleFormat() {
        WritableCellFormat format = new WritableCellFormat();
        try {
            // 单元格格式
            // 背景颜色
            // format.setBackground(Colour.PINK);
            // 边框线
            format.setBorder(Border.BOTTOM, BorderLineStyle.THIN, Colour.RED);
            // 设置文字居中对齐方式;
            format.setAlignment(Alignment.CENTRE);
            // 设置垂直居中;
            format.setVerticalAlignment(VerticalAlignment.CENTRE);
            // 设置自动换行
            format.setWrap(false);

            // 字体格式
            WritableFont font = new WritableFont(WritableFont.ARIAL);
            // 字体颜色
            font.setColour(Colour.BLUE2);
            // 字体加粗
            font.setBoldStyle(WritableFont.BOLD);
            // 字体加下划线
            font.setUnderlineStyle(UnderlineStyle.SINGLE);
            // 字体大小
            font.setPointSize(20);
            format.setFont(font);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    private static int f1flag = 0;
    private static int f2flag = 0;
    private static int f3flag = 0;

    @ExcelContentCellFormat(titleName = "学号")
    private WritableCellFormat f1() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f1flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }

            f1flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    @ExcelContentCellFormat(titleName = "姓名")
    private WritableCellFormat f2() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f2flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }
            if (stuName.contains("4")) {
                format.setBackground(Colour.RED);
            }
            f2flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    @ExcelContentCellFormat(titleName = "班级")
    private WritableCellFormat f3() {
        WritableCellFormat format = null;
        try {
            format = new WritableCellFormat();
            if ((f3flag & 1) != 0) {
                format.setBackground(Colour.GRAY_25);
            }
            f3flag++;
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    public StudentExcelBen(){

    }
    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }


}

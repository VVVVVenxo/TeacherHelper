/*
 * Created by Engine100 on 2016-11-30 11:09:14.
 *
 *      https://github.com/engine100
 *
 */
package com.example.teacherhelper.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * format the title content,
 * like ExcelContentCellFormat,it is used by method which return WritableCellFormat
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface ExcelTitleCellFormat {
    String titleName();
}
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
 * map to sheet name in excel
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface ExcelSheet {
    String sheetName();
}
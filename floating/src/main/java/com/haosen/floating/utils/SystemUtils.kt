package com.haosen.floating.utils

import android.content.Context
import java.lang.reflect.Field

/**
 * FileName: SystemUtils
 * Author: haosen
 * Date: 2024/11/8 20:30
 * Description:
 */
object SystemUtils {
    fun getResId(variableName: String?, c: Class<*>): Int {
        try {
            val idField: Field = c.getDeclaredField(variableName)
            return idField.getInt(idField)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return -1
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getScreenWidth(context: Context): Int {
        var screenWith = -1
        try {
            screenWith = context.resources.displayMetrics.widthPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return screenWith
    }

    fun getScreenHeight(context: Context): Int {
        var screenHeight = -1
        try {
            screenHeight = context.resources.displayMetrics.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return screenHeight
    }
}

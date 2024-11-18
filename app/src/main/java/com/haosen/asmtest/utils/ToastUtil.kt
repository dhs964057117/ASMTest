package com.haosen.asmtest.utils

import android.content.Context
import android.widget.Toast

object ToastUtil {

    fun toastShort(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
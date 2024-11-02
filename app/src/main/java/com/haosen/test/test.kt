package com.haosen.test

import android.util.Log
import androidx.appcompat.widget.SwitchCompat
import com.haosen.asmtest.App.Companion.application
import com.haosen.asmtest.utils.DataReportHelper

class test {
    init {
        SwitchCompat(application).setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("埋点ceshi", "$buttonView , $isChecked")
        }
        SwitchCompat(application).setOnClickListener {

        }
    }
}
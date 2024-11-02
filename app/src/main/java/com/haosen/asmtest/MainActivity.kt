package com.haosen.asmtest

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import com.haosen.asmtest.App.Companion
import com.haosen.test.TestActivity
import com.haosen.asmtest.exceptionHandler.JavaAirBagConfig
import com.haosen.asmtest.exceptionHandler.setUpJavaAirBag
import com.haosen.asmtest.databinding.ActivityMainBinding
import com.haosen.asmtest.test.Formula
import com.haosen.asmtest.test.KotlinStaticInnerSingle
import com.haosen.asmtest.test.Operation
import com.haosen.asmtest.utils.DataReportHelper

class MainActivity : AppCompatActivity(), OnClickListener {
    companion object {
        const val TAG = "MainActivity"
        val configList = mutableListOf(
            JavaAirBagConfig(
                "java.lang.NullPointerException",
                "test java exception",
                "com.zj.android.performance.StabilityActivity",
                "initView\$lambda\$2"
            ),
            JavaAirBagConfig(
                "java.lang.NullPointerException",
                "test child thread java exception",
                "com.zj.android.performance.StabilityActivity",
                "initView\$lambda\$4\$lambda\$3"
            )
        )
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater, null, false) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setUpJavaAirBag(configList)
    }

    private fun initView() {
        binding.buttonCalculate.setOnClickListener(this)
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG, "beforeTextChanged:$s,$start,$count,$after")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "beforeTextChanged:$s,$start,$count")
            }

            override fun afterTextChanged(s: Editable?) {
                resolveInputState(s)
            }
        })
        binding.checkbox.setOnClickListener(this)
        binding.switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("埋点ceshi", "$buttonView , $isChecked")
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.buttonCalculate -> {
                parseFormula(binding.inputEditText.text)
                startActivity(Intent(this, TestActivity::class.java))
            }

            binding.checkbox -> {
                Log.d("埋点ceshi", "$v")
            }

            else -> {}
        }
    }

    private fun resolveInputState(s: Editable?) {
        val inputText: String? = s?.toString()
        if (inputText.isNullOrEmpty()) {
            updateInputEditTextUI()
            return
        }
        val stripText = inputText.trim()
        if (inputIsMatchRule(stripText, Formula.getMatchRegex())) {
            updateInputEditTextUI()
        } else if (!resultIsMatchRule(stripText)) {
            updateInputEditTextUI(getString(R.string.input_invalidate))
        } else {
            updateInputEditTextUI()
            binding.buttonCalculate.isEnabled = false
        }
    }

    private fun inputIsMatchRule(input: String, operation: Regex): Boolean {
        return operation.matches(input)
    }

    private fun resultIsMatchRule(input: String): Boolean {
        val pattern = """(-?\d+)\s*([+\-*/])(-?\d+)\s*(=)(-?\d+)""".toRegex()
        return pattern.matches(input)
    }

    private fun parseFormula(inputEditable: Editable?) {
        val input = inputEditable?.toString() ?: return
        val pattern = Formula.getMatchRegex()
        val matchResult = pattern.matchEntire(input) ?: return

        val (num1Str, op, num2Str) = matchResult.destructured
        val num1 = num1Str.toInt()
        val num2 = num2Str.toInt()
        Log.d(TAG, "num1Str:$num1Str, op:$op, num2Str:$num2Str")
        val operator = Operation.entries.first { TextUtils.equals(it.operator, op) }
        val result = Formula(num1, operator, num2).calculate()
        result?.let {
            binding.inputEditText.apply {
                val formulaAndResult = "$num1Str$op$num2Str=$result"
                setText(formulaAndResult)
                setSelection(formulaAndResult.length)
            }
        } ?: run {
            updateInputEditTextUI("input invalid")
        }
    }

    private fun updateInputEditTextUI(tips: String = "") {
        val isValid = tips.isBlank()
        if (isValid) {
            binding.inputEditText.setBackgroundResource(R.drawable.edittext_normal_border)
        } else {
            binding.inputEditText.setBackgroundResource(R.drawable.edittext_error_border)
        }
        binding.tips.isVisible = !isValid
        binding.buttonCalculate.isEnabled = isValid
        KotlinStaticInnerSingle
    }
}
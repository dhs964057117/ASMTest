package com.haosen.test

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.haosen.asmtest.R
import com.haosen.asmtest.exceptionHandler.JavaAirBagConfig
import com.haosen.asmtest.databinding.ActivityTextBinding
import com.haosen.asmtest.test.Formula
import com.haosen.asmtest.test.KotlinStaticInnerSingle
import com.haosen.asmtest.test.Operation


class TestActivity : AppCompatActivity(), OnClickListener {

    private lateinit var handler: Handler

    companion object {
        const val TAG = "TestActivity"
        val configList = mutableListOf(
            JavaAirBagConfig(
                "java.lang.NullPointerException",
                "test java exception",
                "com.zj.android.performance.StabilityActivity",
                "initView\$lambda\$2"
            ), JavaAirBagConfig(
                "java.lang.NullPointerException",
                "test child thread java exception",
                "com.zj.android.performance.StabilityActivity",
                "initView\$lambda\$4\$lambda\$3"
            )
        )
    }

    private val adapter = TextViewAdapter()
    private val binding by lazy { ActivityTextBinding.inflate(layoutInflater, null, false) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        handler = Handler {
            Log.d(TAG, "${it.what}")
            true
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TestActivity)
            adapter = this@TestActivity.adapter
            addItemDecoration(object : ItemDecoration() {
                private var divider: Drawable = ColorDrawable(Color.RED);
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    val left = parent.getPaddingLeft()
                    val right = parent.width - parent.getPaddingRight()
                    for (i in 0 until parent.childCount) {
                        val child = parent.getChildAt(i)
                        val params = child.layoutParams as RecyclerView.LayoutParams
                        val top = child.bottom + params.bottomMargin
                        val bottom: Int = top + 20
                        divider.setBounds(left, top, right, bottom)
                        divider.draw(c)
                    }
                }

                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect[0, 0, 0] = divider.getIntrinsicHeight()
                }
            })
        }
        val list = mutableListOf<String>()
        for (i in 0..50) {
            list.add(i.toString())
        }
        adapter.notifyDataList(list.toList() as ArrayList<String>)
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
    }

    override fun onClick(v: View?) {
        when (v) {
            is Button -> {
                finish()
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

    override fun onDestroy() {
        super.onDestroy()
        handler.sendEmptyMessageDelayed(1, 1000)
        handler.sendEmptyMessageDelayed(2, 2000)
        handler.sendEmptyMessageDelayed(3, 3000)
        handler.sendEmptyMessageDelayed(4, 4000)
        handler.sendEmptyMessageDelayed(5, 5000)
        handler.sendEmptyMessageDelayed(6, 6000)
        handler.sendEmptyMessageDelayed(7, 7000)
        handler.sendEmptyMessageDelayed(8, 8000)
        handler.sendEmptyMessageDelayed(9, 9000)
        handler.sendEmptyMessageDelayed(10, 10000)
        handler.sendEmptyMessageDelayed(11, 11000)
        handler.sendEmptyMessageDelayed(12, 12000)
        handler.sendEmptyMessageDelayed(13, 13000)
        handler.sendEmptyMessageDelayed(14, 14000)
        handler.sendEmptyMessageDelayed(15, 15000)
    }

    class MyViewHolder(itemView: TextView) : ViewHolder(itemView) {
        fun setTextView(text: String) {
            (itemView as TextView).text = text
        }
    }

    class TextViewAdapter : RecyclerView.Adapter<ViewHolder>() {
        private val dataList = ArrayList<String>()
        fun notifyDataList(data: ArrayList<String>) {
            dataList.clear()
            dataList.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return MyViewHolder(TextView(parent.context).apply {
                gravity = Gravity.CENTER
                setLayoutParams(
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100)
                )
                setBackgroundColor(Color.BLUE)
                setOnClickListener {
                    Toast.makeText(parent.context, "点击${this.text}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder as MyViewHolder
            holder.setTextView(dataList[position])
        }
    }
}
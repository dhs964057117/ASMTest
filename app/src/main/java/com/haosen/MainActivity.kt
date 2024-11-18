package com.haosen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.haosen.asmtest.R
import com.haosen.asmtest.utils.DataHelper
import com.haosen.theme.AppTheme

class MainActivity : AppCompatActivity() {

    private var exitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Tools)
        setContentView(parseScheme(intent.data))
        //设置显示模式
        DataHelper.getUiMode { mode ->
            if (mode != AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
        //双击返回键回退桌面
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    exitTime = System.currentTimeMillis()
                    val msg = getString(R.string.one_more_press_2_back)
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                } else {
                    moveTaskToBack(true)
                }
            }
        })
//        //WebView预创建
//        WebViewManager.prepare(applicationContext)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setContentView(parseScheme(intent.data))
    }

    override fun onDestroy() {
        super.onDestroy()
//        WanHelper.close()
//        WebViewManager.destroy()
    }

    /**
     * 解析 url scheme（如果有的话）导航到指定页面
     * path 为指定页面导航 route，详情参考 WanNavGraph，示例如下：
     * wan://com.fragment.project/rank_route
     * wan://com.fragment.project/search_route/动画
     * wan://com.fragment.project/web_route/https://wanandroid.com
     */
    private fun parseScheme(uri: Uri?): String? {
        return when {
            uri != null && uri.scheme == "wan" && uri.host == "com.fragment.project" ->
                uri.path?.substring(1)

            else -> null
        }
    }

    private fun setContentView(route: String?) {
        setContent {
            AppTheme {
                AppNavGraph(route)
            }
        }
    }

}
package com.haosen.floating.manager

import androidx.annotation.LayoutRes


/**
 * FileName: Condig
 * Author: haosen
 * Date: 2024/11/30 13:16
 * Description:
 **/
class Config(
    val canDrag: Boolean = true,
    val dragDelete: Boolean = true,
    @LayoutRes val layoutRes: Int = -1,
    val viewClass: Class<*>? = null
)
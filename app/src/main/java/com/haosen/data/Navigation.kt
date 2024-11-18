package com.haosen.data

import com.haosen.data.Article
import com.haosen.tools.base.http.HttpResponse

data class NavigationList(
    val data: MutableList<Navigation>? = null
) : HttpResponse()

data class Navigation(
    val articles: MutableList<Article>? = null,
    val cid: String = "",
    val name: String = ""
)

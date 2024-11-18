package com.haosen.data

import com.haosen.tools.base.http.HttpResponse


data class ProjectTreeList(
    val data: List<ProjectTree>? = null,
) : HttpResponse()

data class ProjectTree(
    val courseId: String = "",
    val id: String = "",
    val name: String = "",
    val order: String = "",
    val parentChapterId: String = "",
    val userControlSetTop: String = "",
    val visible: String = ""
)
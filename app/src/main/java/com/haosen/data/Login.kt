package com.haosen.data

import com.haosen.database.user.User
import com.haosen.tools.base.http.HttpResponse

data class Login(
    val data: User? = null
) : HttpResponse()

data class Register(
    val data: User? = null
) : HttpResponse()
package com.haosen.login

import androidx.lifecycle.viewModelScope
import com.haosen.asmtest.utils.DataHelper
import com.haosen.data.Login
import com.haosen.tools.base.http.post
import com.haosen.tools.base.vm.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    var isLoading: Boolean = false,
    var isLogin: Boolean = false,
    var message: String = "",
)

class LoginViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun resetMessage() {
        _uiState.update {
            it.copy(message = "")
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank()) {
            _uiState.update {
                it.copy(message = "用户名不能为空")
            }
            return
        }
        if (password.isBlank()) {
            _uiState.update {
                it.copy(message = "密码不能为空")
            }
            return
        }
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val response = post<Login> {
                setUrl("user/login")
                putParam("username", username)
                putParam("password", password)
            }
            _uiState.update {
                response.data?.let { user ->
                    DataHelper.setUser(user)
                }
                it.copy(
                    isLoading = false,
                    isLogin = response.errorCode == "0",
                    message = response.errorMsg
                )
            }
        }
    }

}
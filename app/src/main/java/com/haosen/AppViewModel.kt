package com.haosen

import androidx.lifecycle.viewModelScope
import com.haosen.asmtest.utils.DataHelper
import com.haosen.data.HotKey
import com.haosen.data.HotKeyList
import com.haosen.data.Tree
import com.haosen.data.TreeList
import com.haosen.database.user.User
import com.haosen.tools.base.http.get
import com.haosen.tools.base.vm.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WanUiState(
    var hotKeyResult: List<HotKey> = ArrayList(),
    var treeResult: List<Tree> = ArrayList(),
    var user: User? = null,
    var updateTime: Long = 0
)

class AppViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(WanUiState())

    val uiState: StateFlow<WanUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hotKeyList = async { getHotKeyList() }
            val treeList = async { getTreeList() }
            DataHelper.getUser().collect { user ->
                _uiState.update { state ->
                    hotKeyList.await().data?.let { data ->
                        state.hotKeyResult = data
                    }
                    treeList.await().data?.let { data ->
                        state.treeResult = data
                    }
                    state.copy(user = user, updateTime = System.nanoTime())
                }
            }
        }
    }

    /**
     * 获取搜索热词
     */
    private suspend fun getHotKeyList(): HotKeyList {
        return coroutineScope {
            get {
                setUrl("hotkey/json")
            }
        }
    }

    /**
     * 获取项目分类
     */
    private suspend fun getTreeList(): TreeList {
        return coroutineScope {
            get {
                setUrl("tree/json")
            }
        }
    }

}
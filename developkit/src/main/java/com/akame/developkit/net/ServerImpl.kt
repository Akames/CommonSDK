package com.akame.developkit.net

import kotlinx.coroutines.CoroutineScope

interface ServerImpl {

    fun launchService(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: (String) -> Unit = {},
        finalBlock: () -> Unit = {}
    )
}
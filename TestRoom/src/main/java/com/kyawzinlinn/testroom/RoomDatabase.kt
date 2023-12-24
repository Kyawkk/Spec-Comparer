package com.kyawzinlinn.testroom

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class Test {
    fun testCoroutine() = flow {
        val DELAY = 200L
        for (i in 1..10) {
            emit(i)
            delay(DELAY)
        }
    }
}

fun main() {
    CoroutineScope(Dispatchers.IO).launch {
        Test().testCoroutine().onEach {
            println(it)
        }
    }
}

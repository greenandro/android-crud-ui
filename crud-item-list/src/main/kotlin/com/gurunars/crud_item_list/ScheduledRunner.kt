package com.gurunars.crud_item_list

import android.os.Handler

import java.util.Random

/* Executes the runnable every N seconds till it is stopped. */
internal class ScheduledRunner {
    private val handler = Handler()
    private var currentCallback: (() -> Unit)? = null
    private var pause = INITIAL_PAUSE
    private val random = Random()
    private var executionId: Int = 0

    fun stop() {
        handler.removeCallbacks(currentCallback)
        pause = INITIAL_PAUSE
        currentCallback = null
    }

    private fun runIteration(currentExecutionId: Int) {
        if (currentCallback == null || currentExecutionId != executionId) {
            return
        }
        currentCallback?.invoke()
        handler.postDelayed({ runIteration(currentExecutionId) }, pause.toLong())
        pause = PAUSE
    }

    fun start(runnable: () -> Unit) {
        currentCallback = runnable
        executionId = random.nextInt()
        runIteration(executionId)
    }

    companion object {
        private val INITIAL_PAUSE = 500
        private val PAUSE = 200
    }

}

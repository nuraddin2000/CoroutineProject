package com.test.coroutineproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

class MainActivity : AppCompatActivity() {
    val state = MutableStateFlow("empty") // flow to update UI (in our case just print to logcat)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //runAsync()
        runSync()

        lifecycleScope.launchWhenStarted {
            state.collectLatest {
                Log.d("TAG", "doWork: " + state.value)
            }
        }

    }

    private fun runSync() {
        println("runSync method.")
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..1000) {
                withContext(Dispatchers.IO) {
                    doWork(i.toString())
                }
            }
        }
        //  launch 1000 coroutines. Invoke doWork(index/number of coroutine) one after another. Example 1, 2, 3, 4, 5, etc.
    }

    private fun runAsync() {
        println("runAsync method.")
        for (i in 1..1000) {
            CoroutineScope(Dispatchers.IO).launch {
                doWork(i.toString())
            }
        }
        //  launch 1000 coroutines. Invoke doWork(index/number of coroutine) in async way. Example 1, 2, 5, 3, 4, 8, etc.
    }

    private suspend fun doWork(name: String) {
        delay(500)
        state.update { "$name completed." }
    }
}
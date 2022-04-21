package br.com.tick.teira.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineTestRule : TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        super.starting(description)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
        super.finished(description)
    }
}
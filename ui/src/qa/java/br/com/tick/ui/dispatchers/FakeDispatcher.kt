package br.com.tick.ui.dispatchers

import br.com.tick.ui.di.DispatcherProvider
import kotlinx.coroutines.Dispatchers

class FakeDispatcher: DispatcherProvider {

    override fun main() = Dispatchers.Main

    override fun io() = Dispatchers.Main

    override fun unconfined() = Dispatchers.Main
}
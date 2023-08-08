package br.com.tick.utils

import br.com.tick.sdk.dispatchers.DispatcherProvider
import kotlinx.coroutines.Dispatchers

class FakeDispatcher: DispatcherProvider {

    override fun main() = Dispatchers.Main

    override fun io() = Dispatchers.Main

    override fun unconfined() = Dispatchers.Main
}
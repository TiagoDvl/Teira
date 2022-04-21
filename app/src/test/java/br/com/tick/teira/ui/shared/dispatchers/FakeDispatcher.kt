package br.com.tick.teira.ui.shared.dispatchers

import br.com.tick.teira.shared.dispatchers.DispatcherProvider
import kotlinx.coroutines.Dispatchers

class FakeDispatcher: DispatcherProvider {

    override fun main() = Dispatchers.Main

    override fun io() = Dispatchers.Main

    override fun unconfined() = Dispatchers.Main
}
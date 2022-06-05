package br.com.tick.ui.di

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun main(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}

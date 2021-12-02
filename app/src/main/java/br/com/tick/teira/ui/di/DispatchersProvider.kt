package br.com.tick.teira.ui.di

import br.com.tick.teira.shared.dispatchers.DefaultDispatcherProvider
import br.com.tick.teira.shared.dispatchers.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatchersProvider {

    @Binds
    abstract fun bindDispatcherProvider(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider
}

package br.com.tick.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.notifications.NotificationRegister
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TeiraScaffoldViewModel @Inject constructor(
    private val localDataRepository: LocalDataRepository,
    private val notificationRegister: NotificationRegister,
    dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _initialPeriodicNotificationRegistration = MutableSharedFlow<NotificationPeriodicity>(replay = 1)
    val initialPeriodicNotificationRegistration = _initialPeriodicNotificationRegistration.asSharedFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io()) {
            getPeriodicNotificationRegistrationState()
        }
    }

    private suspend fun getPeriodicNotificationRegistrationState() {
        when (localDataRepository.getNotificationPeriodicity().first()) {
            null -> {
                val newNotificationPeriodicity = NotificationPeriodicity.DAILY
                localDataRepository.setNotificationPeriodicity(newNotificationPeriodicity)
                _initialPeriodicNotificationRegistration.emit(newNotificationPeriodicity)
            }
            else -> Unit
        }
    }

    fun setupPeriodicNotification(name: String, descriptionText: String, channelId: String) {
        notificationRegister.register(name, descriptionText, channelId)
    }
}

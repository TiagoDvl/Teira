package br.com.tick.sdk.domain

enum class NotificationPeriodicity {
    DAILY {
        override fun getDays() = 1
    },
    WEEKLY {
        override fun getDays() = 7
    },
    NONE {
        override fun getDays() = 0
    };

    abstract fun getDays(): Int
}

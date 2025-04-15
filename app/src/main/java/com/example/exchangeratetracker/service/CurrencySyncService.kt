package com.example.exchangeratetracker.service

import androidx.lifecycle.LifecycleService
import com.example.exchangeratetracker.domain.usecase.SyncPinnedRatesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrencySyncService : LifecycleService() {

    @Inject
    lateinit var syncPinnedRatesUseCase: SyncPinnedRatesUseCase

    private var syncJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        syncJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                syncPinnedRatesUseCase()
                delay(5000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        syncJob?.cancel()
    }
}
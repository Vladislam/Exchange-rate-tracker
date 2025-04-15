package com.example.exchangeratetracker.manager

import android.content.Context
import android.content.Intent
import com.example.exchangeratetracker.service.CurrencySyncService

object CurrencySyncStarter {
    fun start(context: Context) {
        val intent = Intent(context, CurrencySyncService::class.java)
        context.startService(intent)
    }

    fun stop(context: Context) {
        val intent = Intent(context, CurrencySyncService::class.java)
        context.stopService(intent)
    }
}
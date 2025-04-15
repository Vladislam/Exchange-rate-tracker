package com.example.exchangeratetracker.data.local.mapper

import com.example.exchangeratetracker.data.local.entities.CurrencyInfoEntity
import com.example.exchangeratetracker.domain.model.CurrencyInfo

fun CurrencyInfo.toEntity() = CurrencyInfoEntity(code, name)
fun CurrencyInfoEntity.toDomain() = CurrencyInfo(code, name)
package com.example.exchangeratetracker.di

import android.content.Context
import androidx.room.Room
import com.example.exchangeratetracker.data.local.CurrencyDatabase
import com.example.exchangeratetracker.data.remote.api.OpenExchangeApi
import com.example.exchangeratetracker.data.repository.CurrencyRepositoryImpl
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.domain.usecase.GetSavedCurrenciesUseCase
import com.example.exchangeratetracker.domain.usecase.RemoveCurrencyUseCase
import com.example.exchangeratetracker.domain.usecase.SyncLatestRatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExchangeApi(): OpenExchangeApi {
        return Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenExchangeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyDatabase(@ApplicationContext context: Context): CurrencyDatabase {
        return Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "currency_db"
        ).build()
    }

    @Provides
    fun provideCurrencyDao(db: CurrencyDatabase) = db.currencyDao()

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        api: OpenExchangeApi,
        dao: CurrencyDatabase
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(api, dao.currencyDao())
    }

    @Provides
    fun provideGetSavedCurrenciesUseCase(repository: CurrencyRepository): GetSavedCurrenciesUseCase =
        GetSavedCurrenciesUseCase(repository)

    @Provides
    fun provideSyncLatestRatesUseCase(repository: CurrencyRepository): SyncLatestRatesUseCase =
        SyncLatestRatesUseCase(repository)

    @Provides
    fun provideRemoveCurrencyUseCase(repository: CurrencyRepository): RemoveCurrencyUseCase =
        RemoveCurrencyUseCase(repository)
}
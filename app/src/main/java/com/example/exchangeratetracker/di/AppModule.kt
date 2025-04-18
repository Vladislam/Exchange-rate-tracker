package com.example.exchangeratetracker.di

import android.content.Context
import androidx.room.Room
import com.example.exchangeratetracker.data.local.CurrencyDatabase
import com.example.exchangeratetracker.data.local.dao.CurrencyInfoDao
import com.example.exchangeratetracker.data.local.preferences.BaseCurrencyPreferences
import com.example.exchangeratetracker.data.remote.api.OpenExchangeApi
import com.example.exchangeratetracker.data.repository.CurrencyRepositoryImpl
import com.example.exchangeratetracker.data.repository.RecentSearchRepositoryImpl
import com.example.exchangeratetracker.domain.repository.CurrencyRepository
import com.example.exchangeratetracker.domain.repository.RecentSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExchangeApi(): OpenExchangeApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
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
    fun provideCurrencyInfoDao(db: CurrencyDatabase): CurrencyInfoDao = db.currencyInfoDao()

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        api: OpenExchangeApi,
        db: CurrencyDatabase,
        basePrefs: BaseCurrencyPreferences,
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(
            api,
            db.currencyInfoDao(),
            db.currencyRateDao(),
            basePrefs
        )
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        db: CurrencyDatabase,
    ): RecentSearchRepository {
        return RecentSearchRepositoryImpl(db.recentSearchDao())
    }

    @Provides
    @Singleton
    fun provideBaseCurrencyPreferences(
        @ApplicationContext context: Context
    ): BaseCurrencyPreferences = BaseCurrencyPreferences(context)
}
package ru.skillbox.dependency_injection.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.create
import ru.skillbox.dependency_injection.data.Api
import ru.skillbox.dependency_injection.data.AppVersionInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @AppVersionInterceptorOkHttpClient
    fun provideAppVersionInterceptor(): Interceptor {
        return AppVersionInterceptor()
    }

    @Provides
    @LoggingInterceptorOkHttpClient
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun providesOkHttpClient(
        @LoggingInterceptorOkHttpClient loggingInterceptor: Interceptor,
        @AppVersionInterceptorOkHttpClient appVersionInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(
                loggingInterceptor
            )
            .addNetworkInterceptor(
                appVersionInterceptor
            )
            .followRedirects(true)
            .build()
    }

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://google.com")
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): Api {
        return retrofit.create()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppVersionInterceptorOkHttpClient
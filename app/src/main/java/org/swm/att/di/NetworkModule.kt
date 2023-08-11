package org.swm.att.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.swm.att.BuildConfig
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val AUTHORIZATION = "Authorization"

    @Provides
    @Singleton
    fun provideAttInterceptor(
        attEncryptedPrefDataSource: AttEncryptedPrefDataSource
    ): Interceptor = Interceptor { chain ->
//        val accessToken = attEncryptedPrefDataSource.getStrValue(
//            PreferenceKey.ACCESS_TOKEN
//        )
//      로그인 프로세스 생길 떄까지 임시 access token 사용
        val accessToken = BuildConfig.TMP_ACCESS_TOKEN
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, accessToken)
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideATTOkhttpClient(
        addingTokenInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(addingTokenInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    @Provides
    @Singleton
    fun providePingRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.ATT_BASE_URL)
            .build()
    }

}
package com.endiar.movieverse.core.di

import android.content.Context
import com.endiar.movieverse.core.R
import com.endiar.movieverse.core.data.source.remote.network.TMDBApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val httpUrl = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", appContext.getString(R.string.TMDB_API_KEY))
                .build()
            val request = chain.request()
                .newBuilder()
                .url(httpUrl)
                .build()
            return@Interceptor chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    fun provideTMDBApiService(client: OkHttpClient): TMDBApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
          /*  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/
            .client(client)
            .build()
        return retrofit.create(TMDBApiService::class.java)
    }

}
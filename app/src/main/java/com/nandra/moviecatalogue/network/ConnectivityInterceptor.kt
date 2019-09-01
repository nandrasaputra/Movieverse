package com.nandra.moviecatalogue.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.nandra.moviecatalogue.util.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(val app: Application) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isConnectedToInternet()) throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

    private fun isConnectedToInternet() : Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
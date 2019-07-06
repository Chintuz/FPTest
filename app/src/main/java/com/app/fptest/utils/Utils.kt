package com.app.fptest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log


class NetworkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}

class Utils {

    companion object {
        fun log(s: String) {
            Log.d("FP_test", s)
        }

        fun isDayExpired(timeStamp: Long): Boolean {
            log("day - ${System.currentTimeMillis()} " + ((System.currentTimeMillis() - timeStamp) / (24 * 60 * 60 * 1000)).toString())
            return (System.currentTimeMillis() - timeStamp) / (24 * 60 * 60 * 1000) > 1
        }
    }

}


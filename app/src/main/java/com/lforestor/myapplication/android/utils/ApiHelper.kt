package com.lforestor.myapplication.android.utils

import android.content.Context
import android.util.Log
import com.lforestor.myapplication.android.R
import com.lforestor.myapplication.android.repo.FieldEnums
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException


class ApiHelper {
    companion object {
        fun callWordDetailApi(context: Context, params: JSONParam, resolve: (JSONParam, Boolean) -> Unit) {
            if (params.getFieldSafely(FieldEnums.word) === null) {
                val res = JSONParam()
                res.addField(FieldEnums.responseDesc, "invalid input")
                resolve(res, false)
                return
            }
            val client: OkHttpClient = OkHttpClient()
            val url: String = context.getString(R.string.BASE_API_URL) + params.getFieldSafely("word")
            val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", context.getString(R.string.API_KEY))
                    .build()

            client.newCall(request).enqueue(object : com.squareup.okhttp.Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    val res = JSONParam()
                    res.addField(FieldEnums.responseDesc, context.getString(R.string.Network_error))
                    resolve(res, false)
                }

                override fun onResponse(response: Response?) {
                    val res = JSONParam(response?.body()?.string() ?: "{}")
                    val isFailed = res.isEmpty() || res.getFieldSafely(FieldEnums.word) === null || res.getFieldSafely(FieldEnums.frequency) === null
                    if (isFailed) {
                        res.addField(FieldEnums.responseDesc, context.getString(R.string.Not_found))
                    }
                    resolve(res, !isFailed)
                }
            })
        }
    }
}

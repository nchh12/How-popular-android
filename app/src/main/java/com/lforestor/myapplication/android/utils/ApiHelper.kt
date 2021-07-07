package com.lforestor.myapplication.android.utils

import android.content.Context
import android.util.Log
import com.lforestor.myapplication.android.R
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class ApiHelper {
    companion object {
        fun requestWordDetail(context: Context, params: JSONParam, resolve: (data: JSONParam, status: Boolean) -> Unit) {
            if (params.getFieldSafely("word") === null) {
                val res = JSONParam()
                res.addField("msg", "invalid input")
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
                    resolve(JSONParam("{}"), false)
                }

                override fun onResponse(response: Response?) {
                    val res = JSONParam(response?.body()?.string() ?: "{}")
                    resolve(res, !res.isEmpty())
                }
            })
        }
    }
}
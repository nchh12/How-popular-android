package com.lforestor.myapplication.android.repo

import android.content.Context
import com.lforestor.myapplication.android.model.SearchedWords
import com.lforestor.myapplication.android.utils.ApiHelper
import com.lforestor.myapplication.android.utils.JSONParam

class WordsRepo {
    companion object {

        const val MAX_FREQUENCY_POINT = 7

        fun requestWordDetail(context: Context, params: JSONParam, callback: (JSONParam, Boolean) -> Unit) {
            //query from database
            //else
            ApiHelper.callWordDetailApi(context, params) { data: JSONParam, status: Boolean ->
                //add to history
                if (status) {
                    SearchedWords.getSharedValue(context).appendAndSave(data.toString(), context)
                }
                callback(data, status)
            }
        }
    }
}
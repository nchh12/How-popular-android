package com.lforestor.myapplication.android.viewmodel

import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.lforestor.myapplication.android.R
import com.lforestor.myapplication.android.model.ResultPageStatus
import com.lforestor.myapplication.android.repo.WordsRepo
import com.lforestor.myapplication.android.utils.JSONParam
import com.lforestor.myapplication.android.view.BottomSheetFragment

class ResultViewModel(val activity: AppCompatActivity) {

    private var pageState: MutableLiveData<ResultPageStatus>? = null

    init {
        val searchingWord = activity.intent.getStringExtra("word")
        updateSearchingWord(searchingWord)
    }

    fun updateSearchingWord(word: String) {
        hideBottomSheet()
        activity.findViewById<Button>(R.id.labelWord).text = word
        val requestParams = JSONParam()
        requestParams.addField("word", word)
        requestParams.addField("responseDesc", activity.getString(R.string.Searching))
        setPageStatus(requestParams, false)

        //getApi
        WordsRepo.requestWordDetail(activity, requestParams) { data: JSONParam, status: Boolean ->
            setPageStatus(data, status)
        }

    }

    fun hideBottomSheet(){

    }

    private fun setPageStatus(data: JSONParam, status: Boolean) {
        activity.runOnUiThread {
            getPageStatus().value = ResultPageStatus(data, status)
        }
    }

    fun getPageStatus(): MutableLiveData<ResultPageStatus> {
        if (pageState === null) {
            pageState = MutableLiveData<ResultPageStatus>()
        }
        return pageState as MutableLiveData<ResultPageStatus>
    }


}
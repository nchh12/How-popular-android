package com.lforestor.myapplication.android.utils

import android.util.Log
import com.lforestor.myapplication.android.repo.FieldEnums
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONParam(string: String?) {
    private var intance: JSONObject? = null

    constructor() : this("{}") {}

    init {
        try {
            intance = JSONObject(string)
        } catch (e: JSONException) {
            Log.d("@@@", "Cannot to parse " + string)
        }
    }

    fun getFieldSafely(field: String): String? {
        return if (intance?.has(field) == true)
            intance?.getString(field) else
            null
    }

    fun addField(field: String, value: Any) {
        if (intance === null) {
            intance = JSONObject("{}")
        }
        intance?.put(field, value)
    }

    fun isEmpty(): Boolean {
        return intance === null
    }

    fun getFieldResultsArray(field: String, index: Int): ArrayList<String> {
        val results = JSONArray(getFieldSafely(FieldEnums.results) ?: "[]")
        val jsonObj: JSONObject? = results.getJSONObject(index)
        val fieldsNames = ArrayList<String>()
        if (jsonObj?.has(field) == true) {
            val ja: JSONArray? = jsonObj.getJSONArray(field)
            val len: Int? = ja?.length()

            for (i in 0 until len!!) {
                fieldsNames.add(ja.getString(i)!!)
            }
        }
        return fieldsNames
    }

    fun getFieldResults(field: String, index: Int): String? {
        val results = JSONArray(getFieldSafely(FieldEnums.results) ?: "[]")
        val jsonObj: JSONObject? = results.getJSONObject(index)
        return if (jsonObj?.has(field) == true)
            jsonObj.getString(field) else
            null
    }

    fun getFieldResultsCount(): Int {
        val results = JSONArray(getFieldSafely(FieldEnums.results) ?: "[]")
        return results.length()
    }

    fun getPronunciation(field: String): String {
        val value = getFieldSafely(field)
        if (value === null) {
            return ""
        }
        val obj = JSONParam(value)
        return obj.getFieldSafely("all") ?: value
    }


    override fun toString(): String {
        return if (intance !== null) intance!!.toString() else ""
    }
}
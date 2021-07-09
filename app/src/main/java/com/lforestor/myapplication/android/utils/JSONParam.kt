package com.lforestor.myapplication.android.utils

import android.util.Log
import com.lforestor.myapplication.android.repo.FieldEnums
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONParam(string: String) {
    private var intance: JSONObject? = null


    constructor() : this("") {}
    init {
        try {
            intance = JSONObject(string)
            // string = "{a: 1}"
            // intance.get("a")   -> 1

        } catch (e: JSONException) {
            Log.d("@@@", "Cannot to parse " + string)
        }
    }

    fun getFieldSafely(field: String): String? {
        return if (intance?.has(field) == true)
            intance?.getString(field) else
            null
    }

    fun getFieldResultsArray(field: String, index: Int): ArrayList<String> {
        var results: JSONArray? = intance?.getJSONArray(FieldEnums.results)
//        var length: Int? = intance?.length()
        var jsonObj: JSONObject? = results?.getJSONObject(index)
        var FieldsNames = ArrayList<String>()
        if (jsonObj?.has(field) == true) {
            var ja: JSONArray? = jsonObj?.getJSONArray(field)
            var len: Int? = ja?.length()

            for (i in 0 until len!!) {
                FieldsNames.add(ja?.getString(i)!!)
            }
        }
        return FieldsNames
    }

    fun getFieldResults(field: String, index: Int): String? {
        var results: JSONArray? = intance?.getJSONArray(FieldEnums.results)
//        var length: Int? = intance?.length()
        var jsonObj: JSONObject? = results?.getJSONObject(index)
        return if (jsonObj?.has(field) == true)
            jsonObj?.getString(field) else
            null
    }

    fun getFieldResultsCount(): Int? {
        var results: JSONArray? = intance?.getJSONArray(FieldEnums.results)
        Log.d("@@@", results?.length().toString())
        return results?.length()
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

    override fun toString(): String {
        return if (intance !== null) intance!!.toString() else ""
    }
}
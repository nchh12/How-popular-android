package com.lforestor.myapplication.android.utils

import android.util.Log
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
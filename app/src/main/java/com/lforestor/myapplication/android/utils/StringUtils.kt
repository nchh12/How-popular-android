package com.lforestor.myapplication.android.utils

class StringUtils {
    companion object {
        fun checkValidWord(str: String): Boolean {
            if (str == "") return false
            if (str.length > 25) {
                return false
            }
            val s = str.toUpperCase()
            for (index in 0 until s.length) {
                if (s[index].toInt() < 65 || 90 < s[index].toInt()) {
                    return false
                }
            }
            return true
        }

        fun trimExtraSpace(str: String): String{
            var word = str
            while (word.length != 0 && word.get(word.length - 1) == ' ') {
                word = word.substring(0, word.length - 1)
            }
            return word;
        }
    }
}
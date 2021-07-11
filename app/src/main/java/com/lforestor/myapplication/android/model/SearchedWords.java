package com.lforestor.myapplication.android.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.lforestor.myapplication.android.repo.FieldEnums;
import com.lforestor.myapplication.android.utils.JSONParam;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchedWords {
    public List<String> arrayWordString;

    private static final String SIZE_KEY_V1 = "SIZE_KEY_V2";
    private static final String WORD_KEY_V1 = "WORD_KEY_V2";

    private static SearchedWords sharedValue;

    private SearchedWords() {
    }

    ; //block accessing constructor

    public static SearchedWords getSharedValue(Context context) {
        if (sharedValue == null) {
            sharedValue = new SearchedWords();
            sharedValue.arrayWordString = new ArrayList<String>();
            //retrieve value
            SharedPreferences sharedPreferences = context.getSharedPreferences("Storage", MODE_PRIVATE);
            int tmpSize = sharedPreferences.getInt(SIZE_KEY_V1, 0);
            for (int i = 0; i < tmpSize; i++) {
                sharedValue.arrayWordString.add(sharedPreferences.getString(WORD_KEY_V1 + i, ""));
            }
        }
        return sharedValue;
    }

    public void appendAndSave(JSONParam wordDetail, Context context) {
        if (sharedValue.arrayWordString.size() >= 10) {
            sharedValue.arrayWordString.remove(0);
        }
        sharedValue.arrayWordString.add(wordDetail.toString());
        //save
        SharedPreferences sharedPreferences = context.getSharedPreferences("Storage", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SIZE_KEY_V1, sharedValue.arrayWordString.size());
        for (int i = 0; i < sharedValue.arrayWordString.size(); i++) {
            editor.putString(WORD_KEY_V1 + i, sharedValue.arrayWordString.get(i));
        }
        editor.putString("WORD_"+wordDetail.getFieldSafely(FieldEnums.word), wordDetail.toString());
        editor.commit();
    }


}

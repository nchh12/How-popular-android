package com.lforestor.myapplication.android.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchedWords {
    public List<Integer> arrayWordPoint;
    public List<String> arrayWordString;

    private static SearchedWords sharedValue;

    private SearchedWords() {
    }

    ; //block accessing constructor

    public static SearchedWords getSharedValue(Context context) {
        if (sharedValue == null) {
            sharedValue = new SearchedWords();
            sharedValue.arrayWordPoint = new ArrayList<Integer>();
            sharedValue.arrayWordString = new ArrayList<String>();
            //retrieve value
            SharedPreferences sharedPreferences = context.getSharedPreferences("Storage", MODE_PRIVATE);
            int tmpSize = sharedPreferences.getInt("SIZE_KEY", 0);
            for (int i = 0; i < tmpSize; i++) {
                sharedValue.arrayWordString.add(sharedPreferences.getString("WORD_KEY_" + i, ""));
                sharedValue.arrayWordPoint.add(sharedPreferences.getInt("POINT_KEY_" + i, 0));
            }
        }
        return sharedValue;
    }

    public void appendAndSave(String word, int point, Activity activity) {
        if (sharedValue.arrayWordString.size() >= 10) {
            sharedValue.arrayWordString.remove(0);
            sharedValue.arrayWordPoint.remove(0);
        }
        sharedValue.arrayWordString.add(word);
        sharedValue.arrayWordPoint.add((point));
        //save
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Storage", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SIZE_KEY", sharedValue.arrayWordString.size());
        for (int i = 0; i < sharedValue.arrayWordString.size(); i++) {
            editor.putInt("POINT_KEY_" + i, sharedValue.arrayWordPoint.get(i));
            editor.putString("WORD_KEY_" + i, sharedValue.arrayWordString.get(i));
        }
        editor.commit();
    }


}

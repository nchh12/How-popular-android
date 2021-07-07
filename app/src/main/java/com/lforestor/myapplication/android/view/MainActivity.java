package com.lforestor.myapplication.android.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.lforestor.myapplication.android.adapter.AdapterListView;
import com.lforestor.myapplication.android.R;
import com.lforestor.myapplication.android.model.SearchedWords;
import com.lforestor.myapplication.android.utils.ApiHelper;
import com.lforestor.myapplication.android.utils.JSONParam;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static maes.tech.intentanim.CustomIntent.customType;

public class MainActivity extends Activity {
    Button bt;
    EditText editText;
    ListView listSearchedWords;
    AdapterListView customAdapter;

    Boolean check(String s) {
        if (s.equals("")) return false;
        if (s.length() > 25) {
            return false;
        }
        s = s.toUpperCase();
        for (int index = 0; index < s.length(); index++) {
            if (s.charAt(index) < 65 || 90 < s.charAt(index)) {
                return false;
            }
        }
        return true;
    }

    void setUpListView() {
        int[] backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        SearchedWords searchedWords = SearchedWords.getSharedValue(this);
        customAdapter = new AdapterListView(this,
                searchedWords.arrayWordString,
                searchedWords.arrayWordPoint,
                backgroundColors);
        listSearchedWords.setAdapter(customAdapter);
    }

    void mapping() {
        bt = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        listSearchedWords = findViewById(R.id.listView);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.chalkboard);
        editText.setTypeface(typeface);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        //set up listView
        setUpListView();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editText.getText().toString();
                while (word.length() != 0 && word.charAt(word.length() - 1) == ' ') {
                    word = word.substring(0, word.length() - 1);
                }
                if (check(word)) {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("word", word);
                    startActivity(intent);
                    customType(MainActivity.this, "left-to-right");
                }


//                JSONParam tmp = new JSONParam("test");
//                tmp.addField("word",word);
//
//                ApiHelper.Companion.requestWordDetail(MainActivity.this, tmp, new Function2<JSONParam, Boolean, Unit>() {
//                    @Override
//                    public Unit invoke(final JSONParam data, final Boolean status) {
//                        Log.d("@@@", status.toString() + " " + data.toString());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (status) {
//                                    ((TextView) findViewById(R.id.test)).setText(data.getFieldSafely("frequency"));
//                                }
//                            }
//                        });
//
//                        return Unit.INSTANCE;
//                    }
//                });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
//        SearchedWords tmp = SearchedWords.getSharedValue(this);
//        for(int i=0; i<tmp.arrayWordString.size(); i++){
//            Log.d("@@@", tmp.arrayWordString.get(i)+" "+tmp.arrayWordPoint.get(i));
//        }

    }
}

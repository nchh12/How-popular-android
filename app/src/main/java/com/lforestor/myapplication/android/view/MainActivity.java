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
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.lforestor.myapplication.android.adapter.AdapterListView;
import com.lforestor.myapplication.android.R;
import com.lforestor.myapplication.android.model.SearchedWords;
import com.lforestor.myapplication.android.repo.FieldEnums;
import com.lforestor.myapplication.android.utils.ApiHelper;
import com.lforestor.myapplication.android.utils.JSONParam;
import com.lforestor.myapplication.android.utils.StringUtils;

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

    void setUpListView() {
        int[] backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        SearchedWords searchedWords = SearchedWords.getSharedValue(this);
        customAdapter = new AdapterListView(this,
                searchedWords.arrayWordString,
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
                String word = StringUtils.Companion.trimExtraSpace(editText.getText().toString());

                if (StringUtils.Companion.checkValidWord(word)) {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("word", word);
                    startActivity(intent);
//                    customType(MainActivity.this, "left-to-right");
                } else {
                    Toast.makeText(MainActivity.this, "Please type a single word!", Toast.LENGTH_SHORT).show();
                }
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

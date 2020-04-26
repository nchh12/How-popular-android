package com.example.myapplication.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends Activity {
    TextView label;
    Button bt;
    EditText editText;
    Boolean check(String s){
        if (s == "") return false;
        if (s.length() > 25){
            return false;
        }
        s = s.toUpperCase();
        for(int index = 0; index < s.length(); index++){
            if (s.charAt(index) < 65 || 90 < s.charAt(index)){
                return false;
            }
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.chalkboard);
        editText.setTypeface(typeface);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editText.getText().toString();
                while (word.length() != 0 && word.charAt(word.length()-1) ==' '){
                    word = word.substring(0, word.length()-1);
                }
                if (check(word)){
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("word",word);
                    startActivity(intent);
                }
            }
        });



    }
}

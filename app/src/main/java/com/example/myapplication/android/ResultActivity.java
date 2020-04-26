package com.example.myapplication.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ResultActivity extends Activity {
    Button btBack, btCloseDialog, labelWord;
    TextView  labelResult, labelTmp, labelDetail;
    View slidingView, background;
    Point screenSize = new Point();
    int markHeight, curHeight, delta = 12, targetColor;
    int tick = 30; //ms
    int [] backgroundColors;
    final double MAX_RATE = 7;
    Dialog popUpView;
    String resultString = "";
    RelativeLayout.LayoutParams paramOfView;
    void getData(String word){
        OkHttpClient client = new OkHttpClient();
        String url = "https://wordsapiv1.p.rapidapi.com/words/"+word;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "09d7da2befmshf42539be7194045p1e6538jsn55ec41f98a93")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //error in internet connection
                ResultActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        errorCatching(1);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        JSONObject json = new JSONObject(response.body().string());
//                        Log.d("@@@", response+"");
                        double val = json.getDouble("frequency");
                        Log.d("@@@", val+"");
                        val = (val > MAX_RATE) ? MAX_RATE : val;
                        final double finalVal = val;
                        ResultActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeUI(finalVal /MAX_RATE);
                            }
                        });

                        JSONArray detailsArray = json.getJSONArray("results");
                        for(int index = 0; index < detailsArray.length(); index++){
                            JSONObject detail = detailsArray.getJSONObject(index);
                            resultString += "_"+ Integer.toString(index+1).toString() + "_\n";
                            //get definition & part of speech
                            String definition = detail.getString("definition");
                            String partOfSpeech = detail.getString("partOfSpeech");
                            resultString += "("+partOfSpeech+")\n";
                            resultString += "Definition: " + definition + "\n";
                            //get synonyms
                            if (detail.has("synonyms") && !detail.isNull("synonyms")){
                                JSONArray synonyms = detail.getJSONArray("synonyms");
                                resultString += "Syn:\n";
                                for(int i = 0; i < synonyms.length(); i++){
                                    String oneSynonym = synonyms.getString(i);
                                    resultString += "   = " + oneSynonym + "\n";
                                }
                            }
                            //get examples
                            if (detail.has("examples") && !detail.isNull("examples")){
                                JSONArray examples = detail.getJSONArray("examples");
                                resultString += "Ex:\n";
                                for(int i = 0; i < examples.length(); i++){
                                    String oneExample = examples.getString(i);
                                    resultString += "   _ " + oneExample + "\n";
                                }
                            }

                            resultString += "\n\n";
                        }

                    } catch (JSONException e) {
                        //error in casting
                        ResultActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorCatching(2);
                            }
                        });
                    }
                }else{
                    //not found
                    ResultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorCatching(3);
                        }
                    });
                }
            }
        });
    }
    Handler handler = new Handler();
    Runnable  update = new Runnable() {
        @Override
        public void run() {
            curHeight -= delta;
            if (markHeight/screenSize.y < 0.45 && curHeight < screenSize.y*0.55){

            }else{
                //update height of labelResult
                labelResult.setY(curHeight-labelResult.getLineHeight());
            }
            paramOfView.height = curHeight;
            labelResult.setText((Math.round((1.0-(double)curHeight/screenSize.y)*100))+"%");
            slidingView.setLayoutParams(paramOfView);
            if (curHeight > markHeight) handler.postDelayed(update, tick);
            else{
                labelWord.setTextColor(targetColor);
                labelResult.setText((Math.round((1.0-(double)markHeight/screenSize.y)*100))+"%");
                showPopUpView();
            }
        }
    };
    void errorCatching(int caseError){
        switch (caseError){
            case 3: labelTmp.setText("Not found :(");
                break;
            case 1: labelTmp.setText("Something wrong with internet :((");
                break;
            default: labelTmp.setText("Try later :(((");
        }
    }
    void changeUI(double percent){
        if (percent >= 0.8) {
            targetColor = backgroundColors[5];
        }else if (percent >= 0.7){
            targetColor = backgroundColors[4];
        }else if (percent >= 0.6){
            targetColor = backgroundColors[3];
        }else if (percent >= 0.5){
            targetColor = backgroundColors[2];
        }else if (percent >= 0.3){
            targetColor = backgroundColors[1];
        }else{
            targetColor = backgroundColors[0];
        }
        labelTmp.setText("");
        markHeight = (int) (screenSize.y * (1-percent));
        curHeight = screenSize.y;
        handler.post(update);
        //change color animation
        ValueAnimator colorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                backgroundColors[0],
                targetColor);
        colorAnimation.setDuration(Math.round (((double)screenSize.y*percent)*((double)tick/delta))); // milliseconds
        colorAnimation.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                background.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

    }
    void setUpPopUpView(){
        popUpView = new Dialog(this);
        popUpView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popUpView.setCanceledOnTouchOutside(true);
        popUpView.setContentView(R.layout.activity_pop_up);
        btCloseDialog = popUpView.findViewById(R.id.btClose);
        labelDetail = popUpView.findViewById(R.id.textResult);
        Typeface typeface1 = ResourcesCompat.getFont(this, R.font.math_tapping);
        Typeface typeface2 = ResourcesCompat.getFont(this, R.font.chalkboard);
        btCloseDialog.setTypeface(typeface1);
        labelDetail.setTypeface(typeface2);
    }
    void showPopUpView(){
        labelDetail.setText(resultString);
        popUpView.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //init UI
        btBack = findViewById(R.id.btBack);
        labelResult =  findViewById(R.id.labelResult);
        labelWord =  findViewById(R.id.labelWord);
        slidingView = findViewById(R.id.slidingView);
        labelTmp = findViewById(R.id.labelTmp);
        background = this.getWindow().getDecorView();
        Typeface typeface = ResourcesCompat.getFont(this, R.font.math_tapping);
        btBack.setTypeface(typeface);
        labelResult.setTypeface(typeface);
        labelWord.setTypeface(typeface);
        labelTmp.setTypeface(typeface);
        setUpPopUpView();
        //
        //get device's height
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        paramOfView = (RelativeLayout.LayoutParams) slidingView.getLayoutParams();
        paramOfView.height = screenSize.y;
        slidingView.setLayoutParams(paramOfView);
        //import background color
        backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        background.setBackgroundColor(backgroundColors[0]);

        //get word
        String word = getIntent().getStringExtra("word");
        labelWord.setText(word);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData(word);
        btCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpView.cancel();
            }
        });
        labelWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpView.show();
            }
        });
    }
}

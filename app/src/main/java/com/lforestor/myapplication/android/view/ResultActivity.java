package com.lforestor.myapplication.android.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LifecycleOwner;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.lforestor.myapplication.android.R;
import com.lforestor.myapplication.android.model.ResultPageStatus;
import com.lforestor.myapplication.android.repo.FieldEnums;
import com.lforestor.myapplication.android.repo.WordsRepo;
import com.lforestor.myapplication.android.utils.JSONParam;
import com.lforestor.myapplication.android.viewmodel.ResultViewModel;

import static maes.tech.intentanim.CustomIntent.customType;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    Button btBack, labelWord;
    TextView labelResult, labelTmp;
    View slidingView, background;
    Point screenSize = new Point();
    int markHeight, curHeight, delta = 12, targetColor;
    int tick = 30; //ms
    int[] backgroundColors;

    RelativeLayout.LayoutParams paramOfView;
    private InterstitialAd mInterstitialAd;

    ResultViewModel resultViewModel;

    ResultPageStatus currentPageStatus;
    BottomSheetFragment bottomSheetFragment = null;

    Handler handler = new Handler();
    Runnable update = new Runnable() {
        @Override
        public void run() {
            curHeight -= delta;
            if (markHeight / screenSize.y < 0.45 && curHeight < screenSize.y * 0.55) {

            } else {
                //update height of labelResult
                labelResult.setY(curHeight - labelResult.getLineHeight());
            }
            paramOfView.height = curHeight;
            labelResult.setText((Math.round((1.0 - (double) curHeight / screenSize.y) * 100)) + "%");
            slidingView.setLayoutParams(paramOfView);
            if (curHeight > markHeight) handler.postDelayed(update, tick);
            else {
                labelWord.setTextColor(targetColor);
                labelResult.setText((Math.round((1.0 - (double) markHeight / screenSize.y) * 100)) + "%");
            }
        }
    };

    void changeUI(double percent) {
        labelTmp.setText("");

        if (percent >= 0.8) {
            targetColor = backgroundColors[5];
        } else if (percent >= 0.7) {
            targetColor = backgroundColors[4];
        } else if (percent >= 0.6) {
            targetColor = backgroundColors[3];
        } else if (percent >= 0.5) {
            targetColor = backgroundColors[2];
        } else if (percent >= 0.3) {
            targetColor = backgroundColors[1];
        } else {
            targetColor = backgroundColors[0];
        }
        markHeight = (int) (screenSize.y * (1 - percent));
        curHeight = screenSize.y;
        handler.post(update);
        //change color animation
        ValueAnimator colorAnimation = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                backgroundColors[0],
                targetColor);
        colorAnimation.setDuration(Math.round(((double) screenSize.y * percent) * ((double) tick / delta))); // milliseconds
        colorAnimation.setInterpolator(new AccelerateDecelerateInterpolator()); // increase the speed first and
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                background.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

    }

    void setUpAdmob() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.Ad_unit_ID));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initUI();

        resultViewModel = new ResultViewModel(this);

        resultViewModel.getPageStatus().observe((LifecycleOwner) this, resultPageStatus -> {
            currentPageStatus = resultPageStatus;
            if (resultPageStatus.getStatus()) {
                Double rate = Double.parseDouble(resultPageStatus.getData().getFieldSafely(FieldEnums.frequency));
                changeUI(Math.min(1, rate / WordsRepo.MAX_FREQUENCY_POINT));
            } else {
                resetPageUI();
                labelTmp.setText(resultPageStatus.getData().getFieldSafely(FieldEnums.responseDesc));
            }
        });

    }

    private void initUI() {
        getSupportActionBar().hide();

        btBack = findViewById(R.id.btBack);
        labelResult = findViewById(R.id.labelResult);
        labelWord = findViewById(R.id.labelWord);
        slidingView = findViewById(R.id.slidingView);
        labelTmp = findViewById(R.id.labelTmp);

        //set typo
        background = this.getWindow().getDecorView();
        Typeface typeface = ResourcesCompat.getFont(this, R.font.math_tapping);
        btBack.setTypeface(typeface);
        labelResult.setTypeface(typeface);
        labelWord.setTypeface(typeface);
        labelTmp.setTypeface(typeface);

        setUpAdmob();
        resetPageUI();

        //bind click
        btBack.setOnClickListener(this);
        labelWord.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    private void resetPageUI() {
        //get device's height
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        paramOfView = (RelativeLayout.LayoutParams) slidingView.getLayoutParams();
        paramOfView.height = screenSize.y;
        slidingView.setLayoutParams(paramOfView);

        //import background color
        backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        background.setBackgroundColor(backgroundColors[0]);
        labelWord.setTextColor(R.color.gray);

        labelResult.setText("");

        if (bottomSheetFragment != null) {
            bottomSheetFragment.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.labelWord:
//                Log.d("@@@@", currentPageStatus.getStatus() + " " + currentPageStatus.getData().toString());
                if (currentPageStatus.getStatus()) {
                    bottomSheetFragment = new BottomSheetFragment(this, resultViewModel, currentPageStatus.getData());
                    bottomSheetFragment.show(getSupportFragmentManager(), "ModalBottomSheet");
                }
                break;
            case R.id.btBack:
                if (mInterstitialAd.isLoaded()) {
                    Log.d("@@@", "The interstitial loaded.");
                    mInterstitialAd.show();
                } else {
                    Log.d("@@@", "The interstitial wasn't loaded yet.");
                }
                finish();
                customType(ResultActivity.this, "right-to-left");
                break;
            default:
                return;
        }
    }
}

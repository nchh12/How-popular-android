package com.lforestor.myapplication.android.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.lforestor.myapplication.android.R;
import com.lforestor.myapplication.android.adapter.AdapterListView;
import com.lforestor.myapplication.android.adapter.MeaningAdapterListView;
import com.lforestor.myapplication.android.model.SearchedWords;
import com.lforestor.myapplication.android.utils.JSONParam;

public class DictionaryActivity extends AppCompatActivity {
    ListView listMeanings;
    MeaningAdapterListView customAdapter;
    TextView word;
    TextView phonetic;

    void setUpListView() {

        int[] backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        SearchedWords searchedWords = SearchedWords.getSharedValue(this);
        String string = "{\"word\":\"haw\",\"results\":[{\"definition\":\"a spring-flowering shrub or small tree of the genus Crataegus\",\"partOfSpeech\":\"noun\",\"synonyms\":[\"hawthorn\"],\"typeOf\":[\"shrub\",\"bush\"],\"hasTypes\":[\"cockspur thorn\",\"crataegus aestivalis\",\"crataegus apiifolia\",\"crataegus biltmoreana\",\"crataegus calpodendron\",\"crataegus coccinea\",\"blackthorn\",\"crataegus crus-galli\",\"crataegus laevigata\",\"crataegus marshallii\",\"crataegus mollis\",\"crataegus monogyna\",\"crataegus oxyacantha\",\"crataegus oxycantha\",\"crataegus pedicellata\",\"crataegus tomentosa\",\"downy haw\",\"english hawthorn\",\"evergreen thorn\",\"crataegus coccinea mollis\",\"cockspur hawthorn\",\"parsley haw\",\"pear haw\",\"pear hawthorn\",\"red haw\",\"scarlet haw\",\"summer haw\",\"whitethorn\",\"may\",\"mayhaw\",\"parsley-leaved thorn\"],\"memberOf\":[\"crataegus\",\"genus crataegus\"]},{\"definition\":\"the nictitating membrane of a horse\",\"partOfSpeech\":\"noun\",\"typeOf\":[\"third eyelid\",\"nictitating membrane\"]},{\"definition\":\"utter `haw'\",\"partOfSpeech\":\"verb\",\"typeOf\":[\"let loose\",\"emit\",\"let out\",\"utter\"],\"examples\":[\"he hemmed and hawed\"]}],\"syllables\":{\"count\":1,\"list\":[\"haw\"]},\"pronunciation\":{\"all\":\"hɔ\"},\"frequency\":3.3}";
        JSONParam data = new JSONParam(string);
        int num = data.getFieldResultsCount();
        customAdapter = new MeaningAdapterListView(this,
                string,
                backgroundColors,
                num);
        listMeanings.setAdapter(customAdapter);
    }

    void mapping(){
        listMeanings = findViewById(R.id.listViewMeanings);
        word = findViewById(R.id.dictWord);
        phonetic = findViewById(R.id.dictPhonetic);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        mapping();

        setUpListView();
    }
}

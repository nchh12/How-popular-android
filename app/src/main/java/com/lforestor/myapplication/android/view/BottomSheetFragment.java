package com.lforestor.myapplication.android.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lforestor.myapplication.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lforestor.myapplication.android.adapter.MeaningAdapterListView;
import com.lforestor.myapplication.android.repo.FieldEnums;
import com.lforestor.myapplication.android.utils.JSONParam;
import com.lforestor.myapplication.android.viewmodel.ResultViewModel;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    public BottomSheetFragment(Context context, ResultViewModel resultViewModel, JSONParam currentWord) {
        this.context = context;
        this.currentWord = currentWord;
        this.resultViewModel = resultViewModel;
    }

    ListView listMeanings;
    Context context;
    JSONParam currentWord;
    MeaningAdapterListView customAdapter;
    TextView word;
    TextView pronunciation;
    ResultViewModel resultViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void setUpListView() {
        int[] backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        int num = currentWord.getFieldResultsCount();
        customAdapter = new MeaningAdapterListView(this.context,
                resultViewModel,
                currentWord,
                backgroundColors,
                num);
        listMeanings.setAdapter(customAdapter);
    }

    void mapping(View view){
        listMeanings = view.findViewById(R.id.listViewMeanings);
        word = view.findViewById(R.id.dictWord);
        pronunciation = view.findViewById(R.id.dictPhonetic);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_dictionary, container, false);
        mapping(view);
        setUpListView();

        String curWord = currentWord.getFieldSafely(FieldEnums.word);
        String curWordPronunciation = currentWord.getPronunciation(FieldEnums.pronunciation);
        word.setText(curWord);
        if (curWordPronunciation != "")
            pronunciation.setText("\\" + curWordPronunciation + "\\");
        return view;
    }

}

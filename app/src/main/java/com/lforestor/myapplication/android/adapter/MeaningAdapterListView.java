package com.lforestor.myapplication.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.lforestor.myapplication.android.R;
import com.lforestor.myapplication.android.repo.FieldEnums;
import com.lforestor.myapplication.android.repo.WordsRepo;
import com.lforestor.myapplication.android.utils.JSONParam;
import com.lforestor.myapplication.android.utils.StringUtils;
import com.lforestor.myapplication.android.viewmodel.ResultViewModel;

import java.util.ArrayList;
import java.util.List;

public class MeaningAdapterListView extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int[] backgroundColors;
    JSONParam currentWord;
    int num;
    ResultViewModel resultViewModel;

    public MeaningAdapterListView(Context context, ResultViewModel resultViewModel, JSONParam currentWord, int[] backgroundColors, int num) {
        this.context = context;
        this.currentWord = currentWord;
        this.layoutInflater = LayoutInflater.from(context);
        this.backgroundColors = backgroundColors;
        this.num = num;
        this.resultViewModel = resultViewModel;
    }

    @Override
    public int getCount() {
        return num;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    int getColor(int percent) {
        if (percent >= 80) {
            return backgroundColors[5];
        } else if (percent >= 70) {
            return backgroundColors[4];
        } else if (percent >= 60) {
            return backgroundColors[3];
        } else if (percent >= 50) {
            return backgroundColors[2];
        } else if (percent >= 30) {
            return backgroundColors[1];
        } else {
            return backgroundColors[0];
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MeaningAdapterListView.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_listview_dictionary, null);
            viewHolder = new MeaningAdapterListView.ViewHolder();
            //define
            mapping(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MeaningAdapterListView.ViewHolder) convertView.getTag();
        }

        //get string
        String type = currentWord.getFieldResults(FieldEnums.partOfSpeech, position);
        String definition = currentWord.getFieldResults(FieldEnums.definition, position);
        ArrayList<String> examples = currentWord.getFieldResultsArray(FieldEnums.examples, position);
        ArrayList<String> synonyms = currentWord.getFieldResultsArray(FieldEnums.synonyms, position);
        Double rate = Double.parseDouble(currentWord.getFieldSafely(FieldEnums.frequency));
        int frequencyPoint = (int) (rate / WordsRepo.MAX_FREQUENCY_POINT * 100);

        //set content
        viewHolder.wordNumber.setText(position + 1 + ".");
        viewHolder.wordType.setText(type);
        viewHolder.wordDefinition.setText(definition);

        String exp = "";
        for (int i = 0; !examples.isEmpty() && i < examples.size(); i++) {
            if (i < examples.size() - 1)
                exp += "\"" + examples.get(i) + "\"" + "\n";
            else
                exp += "\"" + examples.get(i) + "\"";
        }
        if (exp != "")
            viewHolder.wordExample.setText(exp);
        else {
            viewHolder.lineExample.setVisibility(View.GONE);
        }

        if (!synonyms.isEmpty()) {
            SpannableStringBuilder syn = new SpannableStringBuilder("");
            for (int i = 0; !synonyms.isEmpty() && i < synonyms.size(); i++) {
                syn.append(synonyms.get(i));
                int pos = i;
                syn.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        String word = synonyms.get(pos);
                        if (StringUtils.Companion.checkValidWord(word)) {
                            Toast.makeText(context, "Search for " + word,
                                    Toast.LENGTH_SHORT).show();
                            resultViewModel.updateSearchingWord(word);
                        } else {
                            Toast.makeText(context, word + " is invalid to search",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, syn.length() - synonyms.get(i).length(), syn.length(), 0);
                if (i < synonyms.size() - 1)
                    syn.append(", ");
            }
            viewHolder.wordSynonym.setMovementMethod(LinkMovementMethod.getInstance());
            viewHolder.wordSynonym.setText(syn, TextView.BufferType.SPANNABLE);
        } else
            viewHolder.lineSynonym.setVisibility(View.GONE);

        //color
        int colorHash = getColor(frequencyPoint);
        viewHolder.labelExample.setTextColor(colorHash);
        viewHolder.labelSynonym.setTextColor(colorHash);
        return convertView;
    }

    void mapping(ViewHolder viewHolder, View convertView) {
        viewHolder.wordNumber = convertView.findViewById(R.id.meaningNumber);
        viewHolder.wordType = convertView.findViewById(R.id.meaningWordType);
        viewHolder.wordDefinition = convertView.findViewById(R.id.meaningDefinition);
        viewHolder.wordExample = convertView.findViewById(R.id.meaningExample);
        viewHolder.wordSynonym = convertView.findViewById(R.id.meaningSynonym);
        viewHolder.labelExample = convertView.findViewById(R.id.meaningExampleLabel);
        viewHolder.labelSynonym = convertView.findViewById(R.id.meaningSynonymLabel);
        viewHolder.lineExample = convertView.findViewById(R.id.meaningExampleLine);
        viewHolder.lineSynonym = convertView.findViewById(R.id.meaningSynonymLine);

        Typeface typeface = ResourcesCompat.getFont(context, R.font.math_tapping);
        viewHolder.wordNumber.setTypeface(typeface);
        viewHolder.wordType.setTypeface(typeface);
        viewHolder.labelExample.setTypeface(typeface);
        viewHolder.labelSynonym.setTypeface(typeface);
    }

    static class ViewHolder {
        TextView wordNumber;
        TextView wordType;
        TextView wordDefinition;
        TextView wordExample;
        TextView labelExample;
        TextView labelSynonym;
        RelativeLayout lineExample;
        RelativeLayout lineSynonym;
        TextView wordSynonym;
    }
}

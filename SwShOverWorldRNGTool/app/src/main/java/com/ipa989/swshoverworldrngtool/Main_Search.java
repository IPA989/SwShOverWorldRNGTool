package com.ipa989.swshoverworldrngtool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Main_Search extends Fragment {
    private SharedPreferences dataStore;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_search,container,false);

        dataStore = getActivity().getSharedPreferences("DataStore", Activity.MODE_PRIVATE);
        String state0 = dataStore.getString("state0", "Nothing");
        EditText s0 = (EditText) view.findViewById(R.id.s0);
        if(!state0.equals("Nothing")) {
            s0.setText(state0);
        }
        String state1 = dataStore.getString("state1", "Nothing");
        if(!state1.equals("Nothing")) {
            EditText s1 = (EditText) view.findViewById(R.id.s1);
            s1.setText(state0);
        }
        String tsv = dataStore.getString("tsv", "Nothing");
        if(!tsv.equals("Nothing")) {
            EditText TSV = (EditText) view.findViewById(R.id.tsv);
            TSV.setText(tsv);
        }
        String trv = dataStore.getString("trv", "Nothing");
        if(!trv.equals("Nothing")) {
            EditText TRV = (EditText) view.findViewById(R.id.trv);
            TRV.setText(trv);
        }
        boolean shinyCharm = dataStore.getBoolean("shinyCharm", false);
        if(shinyCharm){
            CheckBox shiny = (CheckBox) view.findViewById(R.id.shinycharm);
            shiny.setChecked(true);
        }
        boolean markCharm = dataStore.getBoolean("markCharm", false);
        if(markCharm){
            CheckBox mark = (CheckBox) view.findViewById(R.id.markcharm);
            mark.setChecked(true);
        }

        String[] jaMarks = {
                "あばれんぼう", "のうてんき", "ドキドキ", "ワクワク", "オーラ", "クール", "アグレッシブ",
                "ボーっと", "しあわせ", "ぷんぷん", "ニコニコ", "メソメソ", "ごきげん", "ふきげん", "ちてき", "あれくるう", "スキをねらう",
                "いかつい", "やさしげ", "あわてんぼう", "やるきまんまん", "やるきゼロ", "ふんぞりかえった", "じしんのない",
                "そぼくな", "きどっている", "げんきいっぱい", "どこかくたびれた", "ひとをしらない", "よくみる", "天気", "時間", "釣り"
        };
        String[] enMarks = {
                "Rowdy",     "AbsentMinded", "Jittery",  "Excited", "Charismatic",
                "Calmness",  "Intense",      "ZonedOut", "Joyful",  "Angry",
                "Smiley",    "Teary",        "Upbeat",   "Peeved",  "Intellectual",
                "Ferocious", "Crafty",       "Scowling", "Kindly",  "Flustered",
                "PumpedUp",  "ZeroEnergy",   "Prideful", "Unsure",  "Humble",
                "Thorny",    "Vigor",        "Slump", "Rare", "Uncommon", "Weather", "Time", "Fish"};

        String[]                   jaNatures = {
                "がんばりや", "さみしがり", "ゆうかん", "いじっぱり", "やんちゃ", "ずぶとい", "すなお", "のんき", "わんぱく", "のうてんき", "おくびょう", "せっかち", "まじめ", "ようき", "むじゃき", "ひかえめ", "おっとり", "れいせい", "てれや", "うっかりや", "おだやか", "おとなしい", "なまいき", "しんちょう", "きまぐれ"
        };
        String[]                   enNatures = {
                "Hardy",  "Lonely", "Brave",   "Adamant", "Naughty",
                "Bold",   "Docile", "Relaxed", "Impish",  "Lax",
                "Timid",  "Hasty",  "Serious", "Jolly",   "Naive",
                "Modest", "Mild",   "Quiet",   "Bashful", "Rash",
                "Calm",   "Gentle", "Sassy",   "Careful", "Quirky"};
        String[] natures;
        String[] mark;

        Locale locale = Locale.getDefault();
        if (locale.equals(Locale.JAPAN)) {
            // 日本語
            mark = jaMarks;
            natures = jaNatures;
        } else {
            // 英語
            mark = enMarks;
            natures = enNatures;
        }


        // 証
        String[] Marks = {getString(R.string.Ignore), getString(R.string.none),
                getString(R.string.any_mark), getString(R.string.any_personality)};
        Context context = view.getContext();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(Marks);
        adapter.addAll(mark);
        Spinner markSpinner = (Spinner) view.findViewById(R.id.markList);
        markSpinner.setAdapter(adapter);

        // 色
        String[] shiny = {getString(R.string.Ignore), "◇", "★", "★/◇"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.addAll(shiny);
        Spinner shinySpinner = (Spinner) view.findViewById(R.id.shinyList);
        shinySpinner.setAdapter(adapter2);

        // 性格
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.add(getString(R.string.Ignore));
        adapter3.addAll(natures);
        Spinner natureSpinner = (Spinner) view.findViewById(R.id.natureList);
        natureSpinner.setAdapter(adapter3);

        return view;
    }



}

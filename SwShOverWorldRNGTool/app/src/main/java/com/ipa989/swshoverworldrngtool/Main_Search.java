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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Main_Search extends Fragment {
    private SharedPreferences dataStore;
    private TextView advmin;
    private TextView advmax;
    private CheckBox weather;
    private CheckBox isStatic;
    private CheckBox fishing;
    private CheckBox helditem;
    private CheckBox cutecharm;
    private CheckBox tsvsearch;
    private CheckBox ability;
    private TextView Lvmin;
    private TextView Lvmax;
    private TextView slotmin;
    private TextView slotmax;
    private TextView kos;

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

        advmin = view.findViewById(R.id.advmin);
        advmax = view.findViewById(R.id.advmax);
        weather = view.findViewById(R.id.weather);
        isStatic = view.findViewById(R.id.staticCheck);
        fishing = view.findViewById(R.id.fishing);
        helditem = view.findViewById(R.id.randomItem);
        cutecharm = view.findViewById(R.id.cutecharm);
        tsvsearch = view.findViewById(R.id.TSVSearch);
        ability = view.findViewById(R.id.isAbilityLocked);
        Lvmin = view.findViewById(R.id.LvMin);
        Lvmax = view.findViewById(R.id.LvMax);
        slotmin = view.findViewById(R.id.SlotMin);
        slotmax = view.findViewById(R.id.SlotMax);
        kos = view.findViewById(R.id.KOs);

        if (savedInstanceState != null){
            advmin.setText(savedInstanceState.getString("advmin"));
            advmax.setText(savedInstanceState.getString("advmax"));
            weather.setChecked(savedInstanceState.getBoolean("weather"));
            isStatic.setChecked(savedInstanceState.getBoolean("isStatic"));
            fishing.setChecked(savedInstanceState.getBoolean("fishing"));
            helditem.setChecked(savedInstanceState.getBoolean("helditem"));
            cutecharm.setChecked(savedInstanceState.getBoolean("cutecharm"));
            tsvsearch.setChecked(savedInstanceState.getBoolean("tsvsearch"));
            ability.setChecked(savedInstanceState.getBoolean("ability"));
            Lvmin.setText(savedInstanceState.getString("Lvmin"));
            Lvmax.setText(savedInstanceState.getString("Lvmax"));
            slotmin.setText(savedInstanceState.getString("slotmin"));
            slotmax.setText(savedInstanceState.getString("slotmin"));
            kos.setText(savedInstanceState.getString("kos"));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        long  MinAdv = Long.parseLong(advmin.getText().toString());
        long MaxAdv = Long.parseLong(advmax.getText().toString());
        boolean Weather    = weather.isChecked();
        boolean Static     = isStatic.isChecked();
        boolean   Fishing       = fishing.isChecked();
        boolean   HeldItem      = helditem.isChecked();
        boolean IsCuteCharm     = cutecharm.isChecked();
        boolean TSVSearch       = tsvsearch.isChecked();
        boolean IsAbilityLocked = ability.isChecked();
        int LevelMin = Integer.parseInt(Lvmin.getText().toString());
        int LevelMax = Integer.parseInt(Lvmax.getText().toString());
        int SlotMin = Integer.parseInt(slotmin.getText().toString());
        int SlotMax = Integer.parseInt(slotmax.getText().toString());
        int KOs             = Integer.parseInt(kos.getText().toString());

        outState.putLong("advmin", MinAdv);
        outState.putLong("advmax", MaxAdv);
        outState.putBoolean("weather", Weather);
        outState.putBoolean("isStatic", Static);
        outState.putBoolean("fishing", Fishing);
        outState.putBoolean("helditem", HeldItem);
        outState.putBoolean("cutecharm", IsCuteCharm);
        outState.putBoolean("tsvsearch", TSVSearch);
        outState.putBoolean("ability", IsAbilityLocked);
        outState.putInt("Lvmin", LevelMin);
        outState.putInt("Lvmax", LevelMax);
        outState.putInt("slotmin", SlotMin);
        outState.putInt("slotmin", SlotMax);
        outState.putInt("kos", KOs);
//        int[] MinIVs = new int[6];
//        int[] MaxIVs = new int[6];
//        int EggMoveCount    = 0;
//        boolean IsShinyLocked   = false;

    }

}

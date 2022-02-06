package com.ipa989.swshoverworldrngtool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ipa989.swshoverworldrngtool.ui.main.SectionsPagerAdapter;

import com.ipa989.swshoverworldrngtool.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'swshoverworldrngtool' library on application startup.
    static {
        System.loadLibrary("swshoverworldrngtool");
    }

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    public static final String EXTRA_MESSAGE =
            "com.example.android.swshoverworldrngtool.extra.MESSAGE";
    public static final String EXTRA_MESSAGE1 =
            "com.example.android.swshoverworldrngtool.extra.MESSAGE1";
    public static final int TEXT_REQUEST = 1;
    private EditText mMessages0;
    private EditText mMessages1;
    private SharedPreferences dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    public void onclickState(View view) {
        Intent intent = new Intent(this, State_Search.class);
        mMessages0 = findViewById(R.id.s0);
        mMessages1 = findViewById(R.id.s1);
        String message0 = mMessages0.getText().toString();
        String message1 = mMessages1.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message0)
        .putExtra(EXTRA_MESSAGE1, message1);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    public void onclickSetting(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply0 = data.getStringExtra(State_Search.EXTRA_REPLY);
                String reply1 = data.getStringExtra(State_Search.EXTRA_REPLY1);
                mMessages0 = findViewById(R.id.s0);
                mMessages1 = findViewById(R.id.s1);
                mMessages0.setText(reply0);
                mMessages1.setText(reply1);
            }
        }
    }

    public void onclickMainSearch(View view) {

        // 端末にデータ保存 state, tsv, trv, おまもり
        dataStore = getSharedPreferences("DataStore", MODE_PRIVATE);
        SharedPreferences.Editor editor = dataStore.edit();
        EditText s0 = findViewById(R.id.s0);
        EditText s1 = findViewById(R.id.s1);
        EditText tsv = findViewById(R.id.tsv);
        EditText trv = findViewById(R.id.trv);
        CheckBox shiny = findViewById(R.id.shinycharm);
        CheckBox mark = findViewById(R.id.markcharm);
        editor.putString("state0", (s0.getText().toString()));
        editor.putString("state1", (s1.getText().toString()));
        editor.putString("tsv", (tsv.getText().toString()));
        editor.putString("trv", (trv.getText().toString()));
        editor.putBoolean("shinyCharm", (shiny.isChecked()));
        editor.putBoolean("markCharm", (mark.isChecked()));
        //editor.commit();
        editor.apply();

        // スレッド起動
        new AsyncAppTask().execute();
    }

    class AsyncAppTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            // ここに前処理を記述します
            Button button = findViewById(R.id.search);
            String word = getString(R.string.searching);
            button.setText(word);
        }
        @Override
        protected String doInBackground(Void... arg0) {
            // バックグランド処理をここに記述します
            Locale locale = Locale.getDefault();
            String lo;
            if (locale.equals(Locale.JAPAN)) {
                // 日本語環境
                lo = "JA";
            } else {
                // 英語環境
                lo = "EN";
            }
            boolean search = true;

            Button searchButton = findViewById(R.id.search);
            EditText st0 = findViewById(R.id.s0);
            EditText st1 = findViewById(R.id.s1);

            String s0 = st0.getText().toString();
            String s1 = st1.getText().toString();

            EditText advmin = findViewById(R.id.advmin);
            EditText advmax = findViewById(R.id.advmax);
            long  MinAdv = Long.parseLong(advmin.getText().toString());
            long MaxAdv = Long.parseLong(advmax.getText().toString());
            EditText tsv = findViewById(R.id.tsv);
            EditText trv = findViewById(R.id.trv);
            CheckBox shiny = findViewById(R.id.shinycharm);
            CheckBox mark = findViewById(R.id.markcharm);
            CheckBox weather = findViewById(R.id.weather);
            String tsvs = tsv.getText().toString();
            int TSV = Integer.parseInt(tsvs);
            String trvs =trv.getText().toString();
            TSV *= 16;
            int TRV=0;
            try{
                TRV = Integer.parseInt(trvs, 16); // 16進数
            }catch(Exception e){
                search = false;
            }
            boolean ShinyCharm = shiny.isChecked();
            boolean MarkCharm  = mark.isChecked();
            boolean Weather    = weather.isChecked();
            CheckBox isStatic = findViewById(R.id.staticCheck);
            boolean Static     = isStatic.isChecked();

            CheckBox fishing = findViewById(R.id.fishing);
            CheckBox helditem = findViewById(R.id.randomItem);
            boolean   Fishing       = fishing.isChecked();
            boolean   HeldItem      = helditem.isChecked();

            EditText Lvmin = findViewById(R.id.LvMin);
            EditText Lvmax = findViewById(R.id.LvMax);
            EditText slotmin = findViewById(R.id.SlotMin);
            EditText slotmax = findViewById(R.id.SlotMax);

            int LevelMin = Integer.parseInt(Lvmin.getText().toString());
            int LevelMax = Integer.parseInt(Lvmax.getText().toString());
            int SlotMin = Integer.parseInt(slotmin.getText().toString());
            int SlotMax = Integer.parseInt(slotmax.getText().toString());

            EditText hmin = findViewById(R.id.hmin);
            EditText hmax = findViewById(R.id.hmax);
            EditText amin = findViewById(R.id.amin);
            EditText amax = findViewById(R.id.amax);
            EditText bmin = findViewById(R.id.bmin);
            EditText bmax = findViewById(R.id.bmax);
            EditText cmin = findViewById(R.id.cmin);
            EditText cmax = findViewById(R.id.cmax);
            EditText dmin = findViewById(R.id.dmin);
            EditText dmax = findViewById(R.id.dmax);
            EditText smin = findViewById(R.id.smin);
            EditText smax = findViewById(R.id.smax);

            int[] MinIVs = new int[6];
            int[] MaxIVs = new int[6];

            MinIVs[0] = Integer.parseInt(hmin.getText().toString());
            MaxIVs[0] = Integer.parseInt(hmax.getText().toString());
            MinIVs[1] = Integer.parseInt(amin.getText().toString());
            MaxIVs[1] = Integer.parseInt(amax.getText().toString());
            MinIVs[2] = Integer.parseInt(bmin.getText().toString());
            MaxIVs[2] = Integer.parseInt(bmax.getText().toString());
            MinIVs[3] = Integer.parseInt(cmin.getText().toString());
            MaxIVs[3] = Integer.parseInt(cmax.getText().toString());
            MinIVs[4] = Integer.parseInt(dmin.getText().toString());
            MaxIVs[4] = Integer.parseInt(dmax.getText().toString());
            MinIVs[5] = Integer.parseInt(smin.getText().toString());
            MaxIVs[5] = Integer.parseInt(smax.getText().toString());

            CheckBox ability = findViewById(R.id.isAbilityLocked);
//        EditText EggMoveCount = findViewById(R.id.EggMoveCount);
            EditText kos = findViewById(R.id.KOs);

            boolean IsAbilityLocked = ability.isChecked();
            int EggMoveCount    = 0;
            int KOs             = Integer.parseInt(kos.getText().toString());

            CheckBox legendary = findViewById(R.id.legendary);
            int FlawlessIVs;
            if(legendary.isChecked()){
                FlawlessIVs     = 3;
            }else{
                FlawlessIVs     = 0;
            }
//        CheckBox shinylocked = findViewById(R.id.shinylocked);
            boolean IsShinyLocked   = false;
            CheckBox cutecharm = findViewById(R.id.cutecharm);
            boolean IsCuteCharm     = cutecharm.isChecked();
            CheckBox tsvsearch = findViewById(R.id.TSVSearch);
            boolean TSVSearch       = tsvsearch.isChecked();

            String Ignore;
            if(lo == "JA"){
                Ignore = "絞り込み無し";
            }else {
                Ignore = "Ignore";
            }

            String DesiredNature = Ignore;
            RadioGroup desiredmark = findViewById(R.id.MarkRadio);
            RadioGroup desiredshiny = findViewById(R.id.ShinyRadio);

            int checkedId   = desiredmark.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            String DesiredMark = radioButton.getText().toString();

            checkedId  = desiredshiny.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(checkedId);
            String DesiredShiny = radioButton.getText().toString();

            if(
                    LevelMax > 100 ||
                            SlotMax > 100 ||
                            LevelMax < LevelMin ||
                            SlotMax < SlotMin ||
                            TSV > 65536
            ){
                search = false;
            }

            for(int i=0; i<6; i++){
                if(MaxIVs[i] < MinIVs[i]){
                    search = false;
                }
            }

            if(!search){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // ③
                        Context context = getApplicationContext();
                        String word = getString(R.string.inputError);
                        Toast toast = Toast.makeText(context, word,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                return "Thread Samples";
            }


            String Result;
            try{
                Result = resultFromJNI(
                        lo, s0, s1, MinAdv, MaxAdv, TSV, TRV, ShinyCharm, MarkCharm,
                        Weather, Static, Fishing, HeldItem, DesiredMark, DesiredShiny,
                        DesiredNature, LevelMin, LevelMax, SlotMin, SlotMax, MinIVs, MaxIVs,
                        IsAbilityLocked, EggMoveCount, KOs, FlawlessIVs, IsCuteCharm,
                        IsShinyLocked, TSVSearch);
            }
            catch (Exception e){
                Result = e.toString();
            }

            String finalResult = Result;
//            tv.post(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    setContentView(R.layout.main_result);
                    TextView tv = findViewById(R.id.resultView);
                    if(finalResult != null) {
                        tv.setText(finalResult);
                    }
                    Context context = getApplicationContext();
                    String word = getString(R.string.searchEnd);
                    Toast toast = Toast.makeText(context, word,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            return "Thread Samples";
        }
        @Override
        protected void onPostExecute(String result) {
            // バックグランド処理終了後の処理をここに記述します
            Button button = findViewById(R.id.search);
            String word = getString(R.string.searchButton);
            button.setText(word);
        }
    }



    public native String resultFromJNI(String locale, String state0, String state1
            , long AdvMin, long AdvMax, int TSV, int TRV, boolean ShinyCharm
            , boolean MarkCharm, boolean Weather, boolean Static, boolean Fishing
            , boolean HeldItem, String DesiredMark, String DesiredShiny, String DesiredNature
            , int LevelMin, int LevelMax, int SlotMin, int SlotMax, int[] MinIVs, int[] MaxIVs
            , boolean IsAbilityLocked, int EggMoveCount, int KOs, int FlawlessIVs
            , boolean IsCuteCharm, boolean IsShinyLocked, boolean TSVSearch);

}
package com.ipa989.swshoverworldrngtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ipa989.swshoverworldrngtool.xoroshiro.SeedSolver;
import com.ipa989.swshoverworldrngtool.xoroshiro.SeedSolverConig;

import java.util.List;
import java.util.Locale;

public class State_Search extends AppCompatActivity {

    private static final String LOG_TAG = State_Search.class.getSimpleName();

    private TextView mMessages0;
    private TextView mMessages1;
    public static final String EXTRA_REPLY =
            "com.example.android.swshoverworldrngtool.extra.REPLY";
    public static final String EXTRA_REPLY1 =
            "com.example.android.swshoverworldrngtool.extra.REPLY1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_search);
        mMessages0 = findViewById(R.id.s0_search0);
        mMessages1 = findViewById(R.id.s1_search0);
        Intent intent = getIntent();
        String message0 = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String message1 = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);
        mMessages0.setText(message0);
        mMessages1.setText(message1);
    }

    public void onclickSearch0(View view) {
//        01110010101110111010010010000011000010000101110010000101111101101000100110110101111001010110101011111001000100011000111100011001
        EditText mo = findViewById(R.id.motionSpace0);

        String motion = mo.getText().toString();
        int n = motion.length();
        String count = Integer.valueOf(n).toString();
        TextView motioncount = findViewById(R.id.motioncount);

        if(n < 128){
            motioncount.setText(count);
            return;
        }

        byte[] motions = new byte[n];
        for(int i=0; i<n; i++) {
            char r = motion.charAt(i);
            if(r == '0') {
                motions[i] = 0;
            }else{
                motions[i] = 1;
            }
        }
        motioncount.setText(count);

        SeedSolverConig config = new SeedSolverConig();
        config.setMotions(motions);

        if(n == 128) {
            // 初期state検索
            config.setS0(null);
            config.setS1(null);
            List<String> state = SeedSolver.list(config);

            if(state != null){
                String s0Result = state.get(1);
                TextView s0_search = findViewById(R.id.s0_search0);
                s0_search.setText(s0Result);
                String s1Result = state.get(2);
                TextView s1_search = findViewById(R.id.s1_search0);
                s1_search.setText(s1Result);
            }

        }
    }

    public void onclickSearch1(View view) {
        SeedSolverConig config = new SeedSolverConig();

        // 基準stateから検索
        EditText mo = findViewById(R.id.motionSpace1);
        String motion = mo.getText().toString();
        int n = motion.length();

        byte[] motions = new byte[motion.length()];
        config.setMotions(motions);


        for(int i=0; i<motion.length(); i++) {
            char r = motion.charAt(i);
            if(r == '0') {
                motions[i] = 0;
            }else {
                motions[i] = 1;
            }
        }


        TextView s0text = findViewById(R.id.s0_search0);
        String s0 = s0text.getText().toString();
        config.setS0(s0);

        TextView s1text = findViewById(R.id.s1_search0);
        String s1 = s1text.getText().toString();
        config.setS1(s1);

        EditText mintext = findViewById(R.id.stateSearchMin);
        int min = Integer.parseInt(mintext.getText().toString());
        config.setStartInclusive(min);

        EditText maxtext = findViewById(R.id.stateSearchMax);
        int max = Integer.parseInt(maxtext.getText().toString());
        config.setEndExclusive(max);

        if(n > 0 && min < max) {

            List<String> state = SeedSolver.list(config);

            String resultcount;
            Locale locale = Locale.getDefault();
            if (locale.equals(Locale.JAPAN)) {
                // 日本語環境
                resultcount = "結果数";
            } else {
                // 英語環境
                resultcount = "resultCount";
            }

            if(state != null && state.get(0).equals("1")){

                String s0Result = state.get(1);
                TextView s0_search = findViewById(R.id.s0_search1);
                s0_search.setText(s0Result);

                String s1Result = state.get(2);
                TextView s1_search = findViewById(R.id.s1_search1);
                s1_search.setText(s1Result);

                TextView f = findViewById(R.id.frameResult);
                String frame = state.get(3);


                String text = "[F]: " + frame + ", " + resultcount  + ": " + state.get(0);
                f.setText(text);
            }else{
                TextView f = findViewById(R.id.frameResult);
                String text = "[F]: 0, " + resultcount + ": " + state.get(0);
                f.setText(text);
            }


        }
    }

    public void onclickOK0(View view) {
        Intent replyIntent = new Intent();
        mMessages0 = findViewById(R.id.s0_search0);
        mMessages1 = findViewById(R.id.s1_search0);
        String reply0 = mMessages0.getText().toString();
        String reply1 = mMessages1.getText().toString();
        replyIntent.putExtra(EXTRA_REPLY, reply0)
                .putExtra(EXTRA_REPLY1, reply1);
        setResult(RESULT_OK,replyIntent);
        finish();
    }

    public void onclickOK1(View view) {
        Intent replyIntent = new Intent();
        mMessages0 = findViewById(R.id.s0_search1);
        mMessages1 = findViewById(R.id.s1_search1);
        String reply0 = mMessages0.getText().toString();
        String reply1 = mMessages1.getText().toString();
        replyIntent.putExtra(EXTRA_REPLY, reply0)
                .putExtra(EXTRA_REPLY1, reply1);
        setResult(RESULT_OK,replyIntent);
        finish();
    }


}
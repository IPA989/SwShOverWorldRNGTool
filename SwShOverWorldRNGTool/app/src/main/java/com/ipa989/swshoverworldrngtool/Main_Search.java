package com.ipa989.swshoverworldrngtool;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        return view;
    }



}

package com.ipa989.swshoverworldrngtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Main_Result extends Fragment {

    TextView result;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_result,container,false);
        result = (TextView) view.findViewById(R.id.resultView);
        if (savedInstanceState != null) {
            String r = savedInstanceState.getString("result");
            if (r != null) {
                result.setText(savedInstanceState
                        .getString("result"));
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String Result = result.getText().toString();
        if(Result!=null){
            outState.putString("result", Result);
        }
    }

}

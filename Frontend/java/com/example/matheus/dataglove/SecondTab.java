package com.example.matheus.dataglove;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondTab extends Fragment {

    public SecondTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout mLayout = (LinearLayout) inflater.inflate(R.layout.second_tab, container, false);
        TextView[] tabela = new TextView[15];
        tabela[0] = mLayout.findViewById(R.id.textView1);
        tabela[1] = mLayout.findViewById(R.id.textView2);
        tabela[2] = mLayout.findViewById(R.id.textView3);
        tabela[3] = mLayout.findViewById(R.id.textView4);
        tabela[4] = mLayout.findViewById(R.id.textView5);
        tabela[5] = mLayout.findViewById(R.id.textView6);
        tabela[6] = mLayout.findViewById(R.id.textView7);
        tabela[7] = mLayout.findViewById(R.id.textView8);
        tabela[8] = mLayout.findViewById(R.id.textView9);
        tabela[9] = mLayout.findViewById(R.id.textView10);
        tabela[10] = mLayout.findViewById(R.id.textView11);
        tabela[11] = mLayout.findViewById(R.id.textView12);
        tabela[12] = mLayout.findViewById(R.id.textView13);
        tabela[13] = mLayout.findViewById(R.id.textView14);
        tabela[14] = mLayout.findViewById(R.id.textView15);
        ((MainActivity) getActivity()).setTabela(tabela);
        ((MainActivity) getActivity()).start();
        return mLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
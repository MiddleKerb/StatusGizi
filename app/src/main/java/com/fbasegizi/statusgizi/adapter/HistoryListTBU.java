package com.fbasegizi.statusgizi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.TinggiBadanUmur;

import java.util.List;

public class HistoryListTBU extends ArrayAdapter<TinggiBadanUmur> {
    private Activity context;
    private List<TinggiBadanUmur> tinggiBadanUmurs;

    public HistoryListTBU(Activity context, List<TinggiBadanUmur> tinggiBadanUmurs) {
        super(context, R.layout.layout_history_list_tbu, tinggiBadanUmurs);
        this.context = context;
        this.tinggiBadanUmurs = tinggiBadanUmurs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_history_list_tbu, null, true);

        TextView historyListTBUAge = listViewItem.findViewById(R.id.HistoryListTBUAge);
        TextView historyListTBUDate = listViewItem.findViewById(R.id.HistoryListTBUDate);
        TextView historyListTBUHeight = listViewItem.findViewById(R.id.HistoryListTBUHeight);
        TextView historyListTBUScore = listViewItem.findViewById(R.id.HistoryListTBUScore);
        TextView historyListTBUStatus = listViewItem.findViewById(R.id.HistoryListTBUStatus);

        TinggiBadanUmur tinggiBadanUmur = tinggiBadanUmurs.get(position);
        historyListTBUAge.setText(String.format("%s Bulan", tinggiBadanUmur.getTbuAge()));
        historyListTBUDate.setText(tinggiBadanUmur.getTbuDateSave());
        historyListTBUHeight.setText(String.format("%s Cm", tinggiBadanUmur.getTbuHeight()));
        historyListTBUScore.setText(tinggiBadanUmur.getTbuScore());
        historyListTBUStatus.setText(tinggiBadanUmur.getTbuStatus());

        return listViewItem;
    }
}

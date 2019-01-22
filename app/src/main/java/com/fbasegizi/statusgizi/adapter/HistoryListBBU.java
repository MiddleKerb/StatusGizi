package com.fbasegizi.statusgizi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.BeratBadanUmur;

import java.util.List;

public class HistoryListBBU extends ArrayAdapter<BeratBadanUmur> {
    private Activity context;
    private List<BeratBadanUmur> beratBadanUmurs;

    public HistoryListBBU(Activity context, List<BeratBadanUmur> beratBadanUmurs) {
        super(context, R.layout.layout_history_list_bbu, beratBadanUmurs);
        this.context = context;
        this.beratBadanUmurs = beratBadanUmurs;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_history_list_bbu, null, true);

        TextView historyListBBUAge = listViewItem.findViewById(R.id.HistoryListBBUAge);
        TextView historyListBBUDate = listViewItem.findViewById(R.id.HistoryListBBUDate);
        TextView historyListBBUWeight = listViewItem.findViewById(R.id.HistoryListBBUWeight);
        TextView historyListBBUScore = listViewItem.findViewById(R.id.HistoryListBBUScore);
        TextView historyListBBUStatus = listViewItem.findViewById(R.id.HistoryListBBUStatus);

        BeratBadanUmur beratBadanUmur = beratBadanUmurs.get(position);
        historyListBBUAge.setText(String.format("%s Bulan", beratBadanUmur.getBbuAge()));
        historyListBBUDate.setText(beratBadanUmur.getBbuDateSave());
        historyListBBUWeight.setText(String.format("%s Kg", beratBadanUmur.getBbuWeight()));
        historyListBBUScore.setText(beratBadanUmur.getBbuScore());
        historyListBBUStatus.setText(beratBadanUmur.getBbuStatus());

        return listViewItem;
    }
}

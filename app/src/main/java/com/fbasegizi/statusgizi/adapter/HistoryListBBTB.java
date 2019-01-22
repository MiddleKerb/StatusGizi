package com.fbasegizi.statusgizi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.BeratBadanTinggiBadan;

import java.util.List;

public class HistoryListBBTB extends ArrayAdapter<BeratBadanTinggiBadan> {
    private Activity context;
    private List<BeratBadanTinggiBadan> beratBadanTinggiBadans;

    public HistoryListBBTB(Activity context, List<BeratBadanTinggiBadan> beratBadanTinggiBadans) {
        super(context, R.layout.layout_history_list_bbtb, beratBadanTinggiBadans);
        this.context = context;
        this.beratBadanTinggiBadans = beratBadanTinggiBadans;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_history_list_bbtb, null, true);

        TextView historyListBBTBAge = listViewItem.findViewById(R.id.HistoryListBBTBAge);
        TextView historyListBBTBDate = listViewItem.findViewById(R.id.HistoryListBBTBDate);
        TextView historyListBBTBHeight = listViewItem.findViewById(R.id.HistoryListBBTBHeight);
        TextView historyListBBTBWeight = listViewItem.findViewById(R.id.HistoryListBBTBWeight);
        TextView historyListBBTBScore = listViewItem.findViewById(R.id.HistoryListBBTBScore);
        TextView historyListBBTBStatus = listViewItem.findViewById(R.id.HistoryListBBTBStatus);

        BeratBadanTinggiBadan beratBadanTinggiBadan = beratBadanTinggiBadans.get(position);
        historyListBBTBAge.setText(String.format("%s Bulan", beratBadanTinggiBadan.getBbtbAge()));
        historyListBBTBDate.setText(beratBadanTinggiBadan.getBbtbDateSave());
        historyListBBTBHeight.setText(String.format("%s Cm", beratBadanTinggiBadan.getBbtbHeight()));
        historyListBBTBWeight.setText(String.format("%s Kg", beratBadanTinggiBadan.getBbtbWeight()));
        historyListBBTBScore.setText(beratBadanTinggiBadan.getBbtbScore());
        historyListBBTBStatus.setText(beratBadanTinggiBadan.getBbtbStatus());

        return listViewItem;
    }
}

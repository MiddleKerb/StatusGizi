package com.fbasegizi.statusgizi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.IndeksMassaTubuhUmur;

import java.util.List;

public class HistoryListIMTU extends ArrayAdapter<IndeksMassaTubuhUmur> {
    private Activity context;
    private List<IndeksMassaTubuhUmur> indeksMassaTubuhUmurs;

    public HistoryListIMTU(Activity context, List<IndeksMassaTubuhUmur> indeksMassaTubuhUmurs) {
        super(context, R.layout.layout_history_list_imtu, indeksMassaTubuhUmurs);
        this.context = context;
        this.indeksMassaTubuhUmurs = indeksMassaTubuhUmurs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_history_list_imtu, null, true);

        TextView historyListIMTUAge = listViewItem.findViewById(R.id.HistoryListIMTUAge);
        TextView historyListIMTUDate = listViewItem.findViewById(R.id.HistoryListIMTUDate);
        TextView historyListIMTUHeight = listViewItem.findViewById(R.id.HistoryListIMTUHeight);
        TextView historyListIMTUWeight = listViewItem.findViewById(R.id.HistoryListIMTUWeight);
        TextView historyListIMTUScore = listViewItem.findViewById(R.id.HistoryListIMTUScore);
        TextView historyListIMTUStatus = listViewItem.findViewById(R.id.HistoryListIMTUStatus);

        IndeksMassaTubuhUmur indeksMassaTubuhUmur = indeksMassaTubuhUmurs.get(position);
        historyListIMTUAge.setText(String.format("%s Bulan", indeksMassaTubuhUmur.getImtuAge()));
        historyListIMTUDate.setText(indeksMassaTubuhUmur.getImtuDateSave());
        historyListIMTUHeight.setText(String.format("%s Cm", indeksMassaTubuhUmur.getImtuHeight()));
        historyListIMTUWeight.setText(String.format("%s Kg", indeksMassaTubuhUmur.getImtuWeight()));
        historyListIMTUScore.setText(indeksMassaTubuhUmur.getImtuScore());
        historyListIMTUStatus.setText(indeksMassaTubuhUmur.getImtuStatus());

        return listViewItem;
    }
}

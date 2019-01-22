package com.fbasegizi.statusgizi.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.Anak;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnakList extends ArrayAdapter<Anak> {
    private Activity context;
    List<Anak> anaks;

    public AnakList(Activity context, List<Anak> anaks) {
        super(context, R.layout.layout_child_list, anaks);
        this.context = context;
        this.anaks = anaks;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_child_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewDate = listViewItem.findViewById(R.id.textViewDate);
        TextView textViewGender = listViewItem.findViewById(R.id.textViewGender);

        Anak anak = anaks.get(position);
        textViewName.setText(capitalize(anak.getChildname()));
        textViewDate.setText(anak.getDateborn());
        textViewGender.setText(anak.getGender());

        return listViewItem;
    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}

package com.fbasegizi.statusgizi.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.ParentDetail;

import java.util.List;

public class ParentDetailList extends ArrayAdapter<ParentDetail> {
    List<ParentDetail> parentDetails;
    private Activity context;

    public ParentDetailList(Activity context, List<ParentDetail> parentDetails) {
        super(context, R.layout.activity_parent_detail_list, parentDetails);
        this.context = context;
        this.parentDetails = parentDetails;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_parent_detail_list, null, true);

        TextView textViewAge = listViewItem.findViewById(R.id.AgeParentFragment);
        TextView textViewFamily = listViewItem.findViewById(R.id.FamilyParentFragment);
        TextView textViewWeight = listViewItem.findViewById(R.id.WeightParentFragment);
        TextView textViewHeight = listViewItem.findViewById(R.id.HeightParentFragment);

        ParentDetail parentDetail = parentDetails.get(position);
        textViewAge.setText(String.format("%s Tahun", parentDetail.getParentAge()));
        textViewFamily.setText(String.format("%s Anggota", parentDetail.getParentFamily()));
        textViewHeight.setText(String.format("%s Cm", parentDetail.getParentHeight()));
        textViewWeight.setText(String.format("%s Kg", parentDetail.getParentWeight()));

        return listViewItem;
    }
}

package com.fbasegizi.statusgizi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.makan.RekomendasiDetail;
import com.fbasegizi.statusgizi.model.MenuMakan;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecyclerViewMenuMakan extends RecyclerView.Adapter<RecyclerViewMenuMakan.ViewHolder> {

    private Context context;
    private List<MenuMakan> menuMakans;

    public RecyclerViewMenuMakan(Context context, List<MenuMakan> menuMakans) {
        this.context = context;
        this.menuMakans = menuMakans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_menu_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final MenuMakan menuMakan = menuMakans.get(i);
        viewHolder.textViewJudul.setText(capitalize(menuMakan.getMenuJudul()));

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RekomendasiDetail.class);
                intent.putExtra("Judul", capitalize(menuMakan.getMenuJudul()));
                intent.putExtra("Bahan", capitalize(menuMakan.getMenuBahan()));
                intent.putExtra("Proses", menuMakan.getMenuProses());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuMakans.size();
    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewJudul;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJudul = itemView.findViewById(R.id.textViewJudul);
            root = itemView.findViewById(R.id.root_menu_list);
        }
    }
}

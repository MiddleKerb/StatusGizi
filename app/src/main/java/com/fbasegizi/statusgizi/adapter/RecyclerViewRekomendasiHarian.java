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
import com.fbasegizi.statusgizi.makan.RekomendasiMakan;
import com.fbasegizi.statusgizi.model.Rekomendasi;

import java.util.List;

public class RecyclerViewRekomendasiHarian extends RecyclerView.Adapter<RecyclerViewRekomendasiHarian.ViewHolder> {

    private Context context;
    private List<Rekomendasi> rekomendasis;

    public RecyclerViewRekomendasiHarian(Context context, List<Rekomendasi> rekomendasis) {
        this.context = context;
        this.rekomendasis = rekomendasis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_harian_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Rekomendasi rekomendasi = rekomendasis.get(i);
        viewHolder.textViewTime.setText(String.format("%s Hari", rekomendasi.getRekomendasiWaktu()));
        viewHolder.textViewDate.setText(rekomendasi.getRekomendasiTanggal());
        viewHolder.textViewBahan.setText(rekomendasi.getRekomendasiBahan());

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RekomendasiMakan.class);
                intent.putExtra("bahan", rekomendasi.getRekomendasiBahan());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rekomendasis.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime, textViewDate, textViewBahan;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.textViewWaktu);
            textViewDate = itemView.findViewById(R.id.textViewTanggal);
            textViewBahan = itemView.findViewById(R.id.textViewBahan);
            root = itemView.findViewById(R.id.root_harian_list);
        }
    }
}

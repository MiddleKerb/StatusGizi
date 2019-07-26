package com.fbasegizi.statusgizi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.makan.MenuHarian;
import com.fbasegizi.statusgizi.model.Anak;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecyclerViewMenuAnak extends RecyclerView.Adapter<RecyclerViewMenuAnak.ViewHolder> {

    private Context context;
    private List<Anak> anaks;

    public RecyclerViewMenuAnak(Context context, List<Anak> anaks) {
        this.context = context;
        this.anaks = anaks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_child_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Anak anak = anaks.get(i);
        viewHolder.textViewName.setText(capitalize(anak.getChildname()));
        viewHolder.textViewDate.setText(anak.getDateborn());
        viewHolder.textViewGender.setText(anak.getGender());

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuHarian.class);
                intent.putExtra("id", anak.getChildId());
                intent.putExtra("nama", capitalize(anak.getChildname()));
                intent.putExtra("tanggal", anak.getDateborn());
                intent.putExtra("gender", anak.getGender());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return anaks.size();
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
        TextView textViewName, textViewDate, textViewGender;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewGender = itemView.findViewById(R.id.textViewGender);
            root = itemView.findViewById(R.id.root_child_list);
        }
    }
}

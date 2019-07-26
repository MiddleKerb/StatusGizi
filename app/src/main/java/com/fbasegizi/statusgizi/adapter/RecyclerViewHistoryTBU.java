package com.fbasegizi.statusgizi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.TinggiBadanUmur;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerViewHistoryTBU extends RecyclerView.Adapter<RecyclerViewHistoryTBU.ViewHolder> {

    private Context context;
    private List<TinggiBadanUmur> tinggiBadanUmurs;

    public RecyclerViewHistoryTBU(Context context, List<TinggiBadanUmur> tinggiBadanUmurs) {
        this.context = context;
        this.tinggiBadanUmurs = tinggiBadanUmurs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_tbu_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final TinggiBadanUmur tinggiBadanUmur = tinggiBadanUmurs.get(i);

        viewHolder.textViewDate.setText(tinggiBadanUmur.getTbuDateSave());
        viewHolder.textViewStatus.setText(tinggiBadanUmur.getTbuStatus());
        viewHolder.textViewScore.setText(String.format("Z-Score %s", tinggiBadanUmur.getTbuScore()));
        viewHolder.textViewAge.setText(String.format("%s Bulan", String.valueOf(tinggiBadanUmur.getTbuAge())));
        viewHolder.textViewHeight.setText(String.format("%s Cm", tinggiBadanUmur.getTbuHeight()));

        if (tinggiBadanUmur.getTbuStatus().equals("Sangat Pendek") || tinggiBadanUmur.getTbuStatus().equals("Tinggi")) {
            viewHolder.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.kuning));
        }

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Peringatan");
                alertDialogBuilder
                        .setMessage("Apakah Anda yakin ingin mengapus data ini?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteChild(tinggiBadanUmur.getTbuId());
                                dialog.cancel();
                                Snackbar.make(v, "Data hitung berhasil dihapus", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                .setTextColor(ContextCompat.getColor(context, R.color.primary_text));
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void deleteChild(final String id) {
        FirebaseDatabase.getInstance().getReference().child("TinggiBadanUmur")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String requiredId = snapshot.getKey();
                            for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                                String secondId = postsnapshot.getKey();
                                if ((requiredId != null) & (secondId != null)) {
                                    FirebaseDatabase.getInstance().getReference().child("TinggiBadanUmur")
                                            .child(requiredId).child(secondId).child(id).removeValue();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return tinggiBadanUmurs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewStatus, textViewScore, textViewAge, textViewHeight;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.TextViewTbuDate);
            textViewStatus = itemView.findViewById(R.id.TextViewTbuStatus);
            textViewScore = itemView.findViewById(R.id.TextViewTbuScore);
            textViewAge = itemView.findViewById(R.id.TextViewTbuAge);
            textViewHeight = itemView.findViewById(R.id.TextViewTbuHeight);
            root = itemView.findViewById(R.id.root_tbu_list);
        }
    }
}

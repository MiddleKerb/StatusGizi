package com.fbasegizi.statusgizi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.BeratBadanUmur;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerViewHistoryBBU extends RecyclerView.Adapter<RecyclerViewHistoryBBU.ViewHolder> {

    private Context context;
    private List<BeratBadanUmur> beratBadanUmurs;

    public RecyclerViewHistoryBBU(Context context, List<BeratBadanUmur> beratBadanUmurs) {
        this.context = context;
        this.beratBadanUmurs = beratBadanUmurs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_bbu_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final BeratBadanUmur beratBadanUmur = beratBadanUmurs.get(i);

        viewHolder.textViewDate.setText(beratBadanUmur.getBbuDateSave());
        viewHolder.textViewStatus.setText(beratBadanUmur.getBbuStatus());
        viewHolder.textViewScore.setText(String.format("Z-Score %s", beratBadanUmur.getBbuScore()));
        viewHolder.textViewAge.setText(String.format("%s Bulan", String.valueOf(beratBadanUmur.getBbuAge())));
        viewHolder.textViewWeight.setText(String.format("%s Kg", beratBadanUmur.getBbuWeight()));

        if (beratBadanUmur.getBbuStatus().equals("Gizi Buruk") || beratBadanUmur.getBbuStatus().equals("Gizi Lebih")) {
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
                                deleteChild(beratBadanUmur.getBbuId());
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
        FirebaseDatabase.getInstance().getReference().child("BeratBadanUmur")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String requiredId = snapshot.getKey();
                            for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                                String secondId = postsnapshot.getKey();
                                if ((requiredId != null) & (secondId != null)) {
                                    FirebaseDatabase.getInstance().getReference().child("BeratBadanUmur")
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
        return beratBadanUmurs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewStatus, textViewScore, textViewAge, textViewWeight;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.TextViewBbuDate);
            textViewStatus = itemView.findViewById(R.id.TextViewBbuStatus);
            textViewScore = itemView.findViewById(R.id.TextViewBbuScore);
            textViewAge = itemView.findViewById(R.id.TextViewBbuAge);
            textViewWeight = itemView.findViewById(R.id.TextViewBbuWeight);
            root = itemView.findViewById(R.id.root_bbu_list);
        }
    }
}

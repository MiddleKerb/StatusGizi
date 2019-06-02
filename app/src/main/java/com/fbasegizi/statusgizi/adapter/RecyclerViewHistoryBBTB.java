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
import com.fbasegizi.statusgizi.model.BeratBadanTinggiBadan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerViewHistoryBBTB extends RecyclerView.Adapter<RecyclerViewHistoryBBTB.ViewHolder> {

    private Context context;
    private List<BeratBadanTinggiBadan> beratBadanTinggiBadans;

    public RecyclerViewHistoryBBTB(Context context, List<BeratBadanTinggiBadan> beratBadanTinggiBadans) {
        this.context = context;
        this.beratBadanTinggiBadans = beratBadanTinggiBadans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_bbtb_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final BeratBadanTinggiBadan beratBadanTinggiBadan = beratBadanTinggiBadans.get(i);

        viewHolder.textViewDate.setText(beratBadanTinggiBadan.getBbtbDateSave());
        viewHolder.textViewStatus.setText(beratBadanTinggiBadan.getBbtbStatus());
        viewHolder.textViewScore.setText(String.format("Z-Score %s", beratBadanTinggiBadan.getBbtbScore()));
        viewHolder.textViewAge.setText(String.format("%s Bulan", String.valueOf(beratBadanTinggiBadan.getBbtbAge())));
        viewHolder.textViewWeight.setText(String.format("%s Kg", beratBadanTinggiBadan.getBbtbWeight()));
        viewHolder.textViewHeight.setText(String.format("%s Cm", beratBadanTinggiBadan.getBbtbHeight()));

        if (beratBadanTinggiBadan.getBbtbStatus().equals("Sangat Kurus") || beratBadanTinggiBadan.getBbtbStatus().equals("Gemuk")) {
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
                                deleteChild(beratBadanTinggiBadan.getBbtbId());
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
        FirebaseDatabase.getInstance().getReference().child("BeratBadanTinggiBadan")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String requiredId = snapshot.getKey();
                            for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                                String secondId = postsnapshot.getKey();
                                if ((requiredId != null) & (secondId != null)) {
                                    FirebaseDatabase.getInstance().getReference().child("BeratBadanTinggiBadan")
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
        return beratBadanTinggiBadans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewStatus, textViewScore, textViewAge, textViewWeight, textViewHeight;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.TextViewBbtbDate);
            textViewStatus = itemView.findViewById(R.id.TextViewBbtbStatus);
            textViewScore = itemView.findViewById(R.id.TextViewBbtbScore);
            textViewAge = itemView.findViewById(R.id.TextViewBbtbAge);
            textViewWeight = itemView.findViewById(R.id.TextViewBbtbWeight);
            textViewHeight = itemView.findViewById(R.id.TextViewBbtbHeight);
            root = itemView.findViewById(R.id.root_bbtb_list);
        }
    }
}

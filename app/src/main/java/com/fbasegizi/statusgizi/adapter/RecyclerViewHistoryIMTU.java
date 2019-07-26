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
import com.fbasegizi.statusgizi.model.IndeksMassaTubuhUmur;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerViewHistoryIMTU extends RecyclerView.Adapter<RecyclerViewHistoryIMTU.ViewHolder> {

    private Context context;
    private List<IndeksMassaTubuhUmur> indeksMassaTubuhUmurs;

    public RecyclerViewHistoryIMTU(Context context, List<IndeksMassaTubuhUmur> indeksMassaTubuhUmurs) {
        this.context = context;
        this.indeksMassaTubuhUmurs = indeksMassaTubuhUmurs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_imtu_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final IndeksMassaTubuhUmur indeksMassaTubuhUmur = indeksMassaTubuhUmurs.get(i);

        viewHolder.textViewDate.setText(indeksMassaTubuhUmur.getImtuDateSave());
        viewHolder.textViewStatus.setText(indeksMassaTubuhUmur.getImtuStatus());
        viewHolder.textViewScore.setText(String.format("Z-Score %s", indeksMassaTubuhUmur.getImtuScore()));
        viewHolder.textViewAge.setText(String.format("%s Bulan", String.valueOf(indeksMassaTubuhUmur.getImtuAge())));
        viewHolder.textViewWeight.setText(String.format("%s Kg", indeksMassaTubuhUmur.getImtuWeight()));
        viewHolder.textViewHeight.setText(String.format("%s Cm", indeksMassaTubuhUmur.getImtuHeight()));

        if (indeksMassaTubuhUmur.getImtuStatus().equals("Sangat Kurus") || indeksMassaTubuhUmur.getImtuStatus().equals("Gemuk")) {
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
                                deleteChild(indeksMassaTubuhUmur.getImtuId());
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
        FirebaseDatabase.getInstance().getReference().child("IndeksMassaTubuhUmur")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String requiredId = snapshot.getKey();
                            for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                                String secondId = postsnapshot.getKey();
                                if ((requiredId != null) & (secondId != null)) {
                                    FirebaseDatabase.getInstance().getReference().child("IndeksMassaTubuhUmur")
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
        return indeksMassaTubuhUmurs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewStatus, textViewScore, textViewAge, textViewWeight, textViewHeight;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.TextViewImtuDate);
            textViewStatus = itemView.findViewById(R.id.TextViewImtuStatus);
            textViewScore = itemView.findViewById(R.id.TextViewImtuScore);
            textViewAge = itemView.findViewById(R.id.TextViewImtuAge);
            textViewWeight = itemView.findViewById(R.id.TextViewImtuWeight);
            textViewHeight = itemView.findViewById(R.id.TextViewImtuHeight);
            root = itemView.findViewById(R.id.root_imtu_list);
        }
    }
}

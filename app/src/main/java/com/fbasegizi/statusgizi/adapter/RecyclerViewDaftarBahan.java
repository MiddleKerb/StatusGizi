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
import com.fbasegizi.statusgizi.model.Bahan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerViewDaftarBahan extends RecyclerView.Adapter<RecyclerViewDaftarBahan.ViewHolder> {

    private Context context;
    private List<Bahan> bahans;

    public RecyclerViewDaftarBahan(Context context, List<Bahan> bahans) {
        this.context = context;
        this.bahans = bahans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_bahan_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Bahan bahan = bahans.get(i);

        viewHolder.textViewNama.setText(bahan.getBahanNama());
        viewHolder.textViewKalori.setText(String.format("%s Kilokalori", String.valueOf(bahan.getBahanKalori())));
        viewHolder.textViewKarbo.setText(String.format("%s Gram", String.valueOf(bahan.getBahanKarbo())));
        viewHolder.textViewLemak.setText(String.format("%s Gram", String.valueOf(bahan.getBahanLemak())));
        viewHolder.textViewProtein.setText(String.format("%s Gram", String.valueOf(bahan.getBahanProtein())));

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (uid.equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Peringatan");
                    alertDialogBuilder
                            .setMessage("Apakah Anda yakin ingin mengapus data ini?")
                            .setCancelable(false)
                            .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteChild(bahan.getBahanId());
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
            }
        });
    }

    private void deleteChild(final String id) {
        FirebaseDatabase.getInstance().getReference().child("Bahan")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseDatabase.getInstance().getReference().child("Bahan").child(id).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return bahans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNama, textViewKalori, textViewKarbo, textViewLemak, textViewProtein;
        CardView root;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNama = itemView.findViewById(R.id.TextViewBahanNama);
            textViewKalori = itemView.findViewById(R.id.TextViewBahanKalori);
            textViewKarbo = itemView.findViewById(R.id.TextViewBahanKarbo);
            textViewLemak = itemView.findViewById(R.id.TextViewBahanLemak);
            textViewProtein = itemView.findViewById(R.id.TextViewBahanProtein);
            root = itemView.findViewById(R.id.root_bahan_list);
        }
    }
}

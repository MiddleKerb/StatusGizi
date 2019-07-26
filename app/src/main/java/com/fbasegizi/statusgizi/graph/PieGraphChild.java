package com.fbasegizi.statusgizi.graph;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PieGraphChild extends BaseActivity {
    private DatabaseReference mDatabase;

    private String History;
    private TextView pieTotal;

    private PieChart chart;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_graph_child);

        Intent intent = getIntent();
        History = intent.getStringExtra("history");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        pieTotal = findViewById(R.id.PieTotal);
        progressBar = findViewById(R.id.ProgressPieGraph);
        chart = findViewById(R.id.PieChart);

        chart.setVisibility(View.GONE);
        pieTotal.setText("");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if ("BeratBadanUmur".equals(History)) {
                actionBar.setTitle("Berat Badan Umur");
            } else if ("TinggiBadanUmur".equals(History)) {
                actionBar.setTitle("Tinggi Badan Umur");
            } else if ("BeratBadanTinggiBadan".equals(History)) {
                actionBar.setTitle("Berat Badan Tinggi Badan");
            } else if ("IndeksMassaTubuhUmur".equals(History)) {
                actionBar.setTitle("Indeks Massa Tubuh");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child(History).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PieEntry> entries = new ArrayList<>();
                String status = null, statA = null, statB = null, statC = null, statD = null;
                Integer a = 0, b = 0, c = 0, d = 0, e = 0;
                switch (History) {
                    case "BeratBadanUmur":
                        status = "bbuStatus";
                        statA = "Gizi Buruk";
                        statB = "Gizi Kurang";
                        statC = "Gizi Baik";
                        statD = "Gizi Lebih";
                        break;
                    case "TinggiBadanUmur":
                        status = "tbuStatus";
                        statA = "Sangat Pendek";
                        statB = "Pendek";
                        statC = "Normal";
                        statD = "Tinggi";
                        break;
                    case "BeratBadanTinggiBadan":
                        status = "bbtbStatus";
                        statA = "Sangat Kurus";
                        statB = "Kurus";
                        statC = "Normal";
                        statD = "Gemuk";
                        break;
                    case "IndeksMassaTubuhUmur":
                        status = "imtuStatus";
                        statA = "Sangat Kurus";
                        statB = "Kurus";
                        statC = "Normal";
                        statD = "Gemuk";
                        break;
                }
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                            for (DataSnapshot grandSnapshot : childSnapshot.getChildren()) {
                                String type = (String) grandSnapshot.child(status).getValue();
                                if (statA.equals(type)) {
                                    a++;
                                } else if (statB.equals(type)) {
                                    b++;
                                } else if (statC.equals(type)) {
                                    c++;
                                } else if (statD.equals(type)) {
                                    d++;
                                } else {
                                    e++;
                                }
                                pieTotal.setText(String.format("Total Data : %s",
                                        String.valueOf(a + b + c + d + e)));
                            }
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    entries.add(new PieEntry(a, statA));
                    entries.add(new PieEntry(b, statB));
                    entries.add(new PieEntry(c, statC));
                    entries.add(new PieEntry(d, statD));

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    PieData data = new PieData(dataSet);

                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int x : ColorTemplate.MATERIAL_COLORS)
                        colors.add(x);
                    for (int x : ColorTemplate.MATERIAL_COLORS)
                        colors.add(x);
                    for (int x : ColorTemplate.MATERIAL_COLORS)
                        colors.add(x);
                    for (int x : ColorTemplate.MATERIAL_COLORS)
                        colors.add(x);
                    colors.add(ColorTemplate.getHoloBlue());
                    dataSet.setColors(colors);
                    dataSet.setSliceSpace(2f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setValueTextSize(12f);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                    chart.setVisibility(View.VISIBLE);
                    chart.setExtraOffsets(5, 10, 5, 5);
                    chart.setDragDecelerationFrictionCoef(0.95f);
                    chart.setEntryLabelColor(Color.WHITE);
                    chart.setEntryLabelTextSize(12f);
                    chart.setTransparentCircleColor(Color.WHITE);
                    chart.setTransparentCircleAlpha(110);
                    chart.setRotationAngle(0);
                    chart.animateY(2000);
                    chart.setRotationEnabled(true);
                    chart.setHighlightPerTapEnabled(true);
                    chart.getDescription().setEnabled(false);
                    //chart.getLegend().setEnabled(false);
                    Legend l = chart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setDrawInside(false);

                    chart.setData(data);
                    chart.notifyDataSetChanged();
                    chart.invalidate();

                } else {
                    progressBar.setVisibility(View.GONE);
                    chart.setVisibility(View.VISIBLE);
                    chart.setNoDataText("DATA KOSONG");
                    pieTotal.setText("");
                    chart.setNoDataTextColor(Color.BLACK);
                    chart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(20f));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
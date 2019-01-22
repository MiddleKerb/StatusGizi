package com.fbasegizi.statusgizi.graph;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.count.AnakCount;
import com.fbasegizi.statusgizi.model.BeratBadanTinggiBadan;
import com.fbasegizi.statusgizi.model.BeratBadanUmur;
import com.fbasegizi.statusgizi.model.IndeksMassaTubuhUmur;
import com.fbasegizi.statusgizi.model.TinggiBadanUmur;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LineGraphChild extends BaseActivity {
    private DatabaseReference mDatabase;

    private LineChart chart;
    private LinearLayout linearLayoutName, linearLayoutDate;
    private ProgressBar progressBar;

    private String id, history, nama, gender, tanggal;
    private TextView graphName, graphDOB, graphSex, empty;
    private Button button;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph_child);

        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        graphName = findViewById(R.id.GraphName);
        graphDOB = findViewById(R.id.GraphDOB);
        graphSex = findViewById(R.id.GraphSex);
        chart = findViewById(R.id.ChildLineChart);
        progressBar = findViewById(R.id.ProgressLineGraph);
        empty = findViewById(R.id.empty_line_graph);
        button = findViewById(R.id.buttonLineGraph);
        linearLayoutDate = findViewById(R.id.LayoutDate);
        linearLayoutName = findViewById(R.id.LayoutName);
        cardView = findViewById(R.id.CardLineGraph);

        id = intent.getStringExtra("id");
        history = intent.getStringExtra("history");
        nama = intent.getStringExtra("nama");
        gender = intent.getStringExtra("gender");
        tanggal = intent.getStringExtra("tanggal");

        graphName.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        linearLayoutName.setVisibility(View.GONE);
        linearLayoutDate.setVisibility(View.GONE);

        graphName.setText(nama);
        graphDOB.setText(tanggal);
        graphSex.setText(gender);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnakCount.class);
                startActivity(intent);
                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if ("BeratBadanUmur".equals(history)) {
                actionBar.setTitle("Berat Badan Umur");
            } else if ("TinggiBadanUmur".equals(history)) {
                actionBar.setTitle("Tinggi Badan Umur");
            } else if ("BeratBadanTinggiBadan".equals(history)) {
                actionBar.setTitle("Berat Badan Tinggi Badan");
            } else if ("IndeksMassaTubuhUmur".equals(history)) {
                actionBar.setTitle("Indeks Massa Tubuh");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.child(history).child(getUid()).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> entries = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    if ("BeratBadanUmur".equals(history)) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            BeratBadanUmur beratBadanUmur =
                                    postSnapshot.getValue(BeratBadanUmur.class);
                            if (beratBadanUmur != null) {
                                entries.add(new Entry(beratBadanUmur.getBbuAge(),
                                        Float.parseFloat(beratBadanUmur.getBbuScore())));
                            }
                        }

                    } else if ("TinggiBadanUmur".equals(history)) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            TinggiBadanUmur tinggiBadanUmur =
                                    postSnapshot.getValue(TinggiBadanUmur.class);
                            if (tinggiBadanUmur != null) {
                                entries.add(new Entry(tinggiBadanUmur.getTbuAge(),
                                        Float.parseFloat(tinggiBadanUmur.getTbuScore())));
                            }
                        }

                    } else if ("BeratBadanTinggiBadan".equals(history)) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            BeratBadanTinggiBadan beratBadanTinggiBadan =
                                    postSnapshot.getValue(BeratBadanTinggiBadan.class);
                            if (beratBadanTinggiBadan != null) {
                                entries.add(new Entry(beratBadanTinggiBadan.getBbtbAge(),
                                        Float.parseFloat(beratBadanTinggiBadan.getBbtbScore())));
                            }
                        }

                    } else if ("IndeksMassaTubuhUmur".equals(history)) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            IndeksMassaTubuhUmur indeksMassaTubuhUmur =
                                    postSnapshot.getValue(IndeksMassaTubuhUmur.class);
                            if (indeksMassaTubuhUmur != null) {
                                entries.add(new Entry(indeksMassaTubuhUmur.getImtuAge(),
                                        Float.parseFloat(indeksMassaTubuhUmur.getImtuScore())));
                            }
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    final LineDataSet dataSet = new LineDataSet(entries, "Z-Score");
                    LineData data = new LineData(dataSet);
                    if (Utils.getSDKInt() >= 18) {
                        Drawable drawable = ContextCompat.
                                getDrawable(LineGraphChild.this, R.drawable.fade_blue);
                        dataSet.setFillDrawable(drawable);
                    } else {
                        dataSet.setFillColor(Color.BLUE);
                    }
                    dataSet.setDrawFilled(true);
                    dataSet.setDrawCircles(true);
                    dataSet.setHighlightEnabled(true);
                    dataSet.setDrawHighlightIndicators(true);
                    dataSet.setValueTextSize(12f);
                    dataSet.setColor(Color.BLUE);
                    dataSet.setCircleColor(Color.BLUE);
                    dataSet.setCircleRadius(6f);
                    dataSet.setCircleHoleRadius(4f);
                    dataSet.setLineWidth(2f);
                    dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    dataSet.setFormLineWidth(1f);
                    dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    dataSet.setFormSize(15.f);

                    //chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    chart.getXAxis().setGranularity(1f);
                    chart.getAxisRight().setEnabled(false);
                    chart.getAxisLeft().setDrawGridLines(false);
                    chart.getAxisLeft().setDrawAxisLine(false);
                    chart.getXAxis().setDrawGridLines(false);
                    chart.getDescription().setEnabled(false);
                    chart.setTouchEnabled(true);
                    chart.setDragEnabled(true);
                    chart.setScaleEnabled(true);
                    chart.setPinchZoom(true);
                    chart.animateY(2000);

                    IMarker marker = new MyMarkerView(LineGraphChild.this, R.layout.custom_marker_view);
                    chart.setMarker(marker);

                    LimitLine limit1 = new LimitLine(2f, "Batas Atas");
                    limit1.setLineWidth(4f);
                    limit1.enableDashedLine(10f, 10f, 0f);
                    limit1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    limit1.setTextSize(10f);

                    LimitLine limit2 = new LimitLine(-3f, "Batas Bawah");
                    limit2.setLineWidth(4f);
                    limit2.enableDashedLine(10f, 10f, 0f);
                    limit2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                    limit2.setTextSize(10f);

                    YAxis leftAxis = chart.getAxisLeft();
                    leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                    leftAxis.addLimitLine(limit1);
                    leftAxis.addLimitLine(limit2);
                    leftAxis.setAxisMaximum(5f);
                    leftAxis.setAxisMinimum(-5f);
                    leftAxis.setDrawLimitLinesBehindData(true);
                    leftAxis.enableGridDashedLine(10f, 10f, 0f);
                    leftAxis.setDrawZeroLine(false);

                    cardView.setVisibility(View.VISIBLE);
                    linearLayoutDate.setVisibility(View.VISIBLE);
                    linearLayoutName.setVisibility(View.VISIBLE);
                    graphName.setVisibility(View.VISIBLE);

                    chart.setData(data);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                } else {
                    progressBar.setVisibility(View.GONE);
                    cardView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Server error, coba ulangi beberapa saat lagi!",
                        Toast.LENGTH_SHORT).show();
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

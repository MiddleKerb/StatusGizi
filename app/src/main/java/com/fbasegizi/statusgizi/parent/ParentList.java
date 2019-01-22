package com.fbasegizi.statusgizi.parent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.AnakList;
import com.fbasegizi.statusgizi.model.Anak;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParentList extends BaseActivity {

    public static final String CHILD_ID = "com.fbasegizi.statusgizi.childid";
    public static final String CHILD_NAME = "com.fbasegizi.statusgizi.childname";

    private DatabaseReference mDatabase;
    private List<Anak> anaks;

    private ListView ListChild;
    private TextView emptyText;
    private ProgressBar progressBar;

    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_graph);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emptyText = findViewById(R.id.empty_parent_graph);
        progressBar = findViewById(R.id.ProgressParentGraph);

        ListChild = findViewById(R.id.listViewParentChild);
        anaks = new ArrayList<>();
        ListChild.setEmptyView(progressBar);

        ListChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anak anak = anaks.get(position);

                Intent intent = new Intent(getApplicationContext(), ParentDetailAdd.class);

                intent.putExtra(CHILD_ID, anak.getChildId());
                intent.putExtra(CHILD_NAME, anak.getChildname());

                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Data Orang Tua");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("child").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anaks.clear();
                state = ListChild.onSaveInstanceState();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                            Anak anak = childSnapshot.getValue(Anak.class);
                            if (anak != null) {
                                anak.setChildId(childSnapshot.getKey());
                            }
                            anaks.add(anak);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    ListChild.setEmptyView(emptyText);
                }
                AnakList anakAdapter = new AnakList(ParentList.this, anaks);
                anakAdapter.notifyDataSetChanged();
                ListChild.setAdapter(anakAdapter);
                ListChild.onRestoreInstanceState(state);
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

package com.fbasegizi.statusgizi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.child.ListAnak;
import com.fbasegizi.statusgizi.count.AnakCount;
import com.fbasegizi.statusgizi.graph.ChildGraph;
import com.fbasegizi.statusgizi.history.HistoryAnak;
import com.fbasegizi.statusgizi.makan.MakanMain;
import com.fbasegizi.statusgizi.research.MySqlMain;
import com.google.firebase.auth.FirebaseAuth;

public class MenuFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        root.setFocusableInTouchMode(true);
        root.requestFocus();

        CardView menu_1 = root.findViewById(R.id.menu_1);
        CardView menu_2 = root.findViewById(R.id.menu_2);
        CardView menu_3 = root.findViewById(R.id.menu_3);
        CardView menu_4 = root.findViewById(R.id.menu_4);
        CardView menu_6 = root.findViewById(R.id.menu_6);
        CardView menu_research = root.findViewById(R.id.menu_research);

        menu_1.setOnClickListener(this);
        menu_2.setOnClickListener(this);
        menu_3.setOnClickListener(this);
        menu_4.setOnClickListener(this);
        menu_6.setOnClickListener(this);
        menu_research.setOnClickListener(this);

        if (!getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            menu_research.setVisibility(View.GONE);
        }

        return root;
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.menu_1:
                i = new Intent(getActivity(), ListAnak.class);
                startActivity(i);
                break;
            case R.id.menu_2:
                i = new Intent(getActivity(), AnakCount.class);
                startActivity(i);
                break;
            case R.id.menu_3:
                i = new Intent(getActivity(), HistoryAnak.class);
                startActivity(i);
                break;
            case R.id.menu_4:
                i = new Intent(getActivity(), ChildGraph.class);
                startActivity(i);
                break;
            case R.id.menu_6:
                i = new Intent(getActivity(), MakanMain.class);
                startActivity(i);
                break;
            case R.id.menu_research:
                i = new Intent(getActivity(), MySqlMain.class);
                startActivity(i);
                break;
        }
    }
}

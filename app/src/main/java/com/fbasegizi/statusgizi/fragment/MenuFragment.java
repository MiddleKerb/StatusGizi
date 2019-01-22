package com.fbasegizi.statusgizi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.child.ListAnak;
import com.fbasegizi.statusgizi.count.AnakCount;
import com.fbasegizi.statusgizi.graph.ChildGraph;
import com.fbasegizi.statusgizi.history.HistoryAnak;
import com.fbasegizi.statusgizi.parent.ParentList;
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
        CardView menu_5 = root.findViewById(R.id.menu_5);

        menu_1.setOnClickListener(this);
        menu_2.setOnClickListener(this);
        menu_3.setOnClickListener(this);
        menu_4.setOnClickListener(this);
        menu_5.setOnClickListener(this);

        if (getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            menu_5.setVisibility(View.VISIBLE);
        } else {
            menu_5.setVisibility(View.GONE);
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
            case R.id.menu_5:
                i = new Intent(getActivity(), ParentList.class);
                startActivity(i);
                break;
        }
    }
}

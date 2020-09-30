package com.github.slondy.lifestyleapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.slondy.lifestyleapp.AddUserActivity;
import com.github.slondy.lifestyleapp.R;
import com.github.slondy.lifestyleapp.UsersActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    CardView users;
    CardView addusers;
    CardView reports;
    CardView settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        users = view.findViewById(R.id.users);
        addusers = view.findViewById(R.id.addusers);

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UsersActivity.class));
            }
        });

        addusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddUserActivity.class));
            }
        });

        return view;
    }
}

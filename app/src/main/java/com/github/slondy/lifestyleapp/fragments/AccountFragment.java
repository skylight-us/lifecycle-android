package com.github.slondy.lifestyleapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.slondy.lifestyleapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    TextView name, mail;
    Button logout;

    public AccountFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        logout = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.name);
        mail = view.findViewById(R.id.mail);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();

                //Intent intent = new Intent(getContext(), LoginActivity.class);
                //startActivity(intent);
            }
        });

        return view;
    }


}

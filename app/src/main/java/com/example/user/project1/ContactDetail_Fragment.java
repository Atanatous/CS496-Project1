package com.example.user.project1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ContactDetail_Fragment extends Fragment {
    private ListViewItem mItem;

    public static ContactDetail_Fragment newInstance(ListViewItem item){
        ContactDetail_Fragment fragment = new ContactDetail_Fragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.contact_detail_fragment, container, false);



        return v;
    }
}

package com.example.user.project1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */

public class Matching_Fragment extends Fragment {

    private ListViewAdapter mAdapter;
    private ArrayList<ListViewItem> mItemList;
    private final int QUIZ_NUMBER = 9;

    public Matching_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.matching_fragment, container, false);

        // receive adapter data from mainActivity
        Bundle bundle = this.getArguments();
        if (bundle != null){
            ArrayList<ListViewAdapter> adapterList = (ArrayList< ListViewAdapter>) bundle.get("adapterList");
            mAdapter = adapterList.get(0);
        }

        // add random 9 items in itemList
        int numOfContact = mAdapter.getCount();
        Random generator = new Random();
        ListViewItem item;

        for (int i = 0; i < QUIZ_NUMBER; i++){
            item = mAdapter.getItem(generator.nextInt(numOfContact));
            // remove duplication
            while (mItemList.contains(item)){
                item = mAdapter.getItem(generator.nextInt(numOfContact));
            }
            mItemList.add(item);
        }

        for (int i = 0; i < QUIZ_NUMBER; i++){
            String nameId = "name" + i;
            String iconId = "icon" + i;
            int nameResId = getResources().getIdentifier(nameId, "id", getActivity().getPackageName());
            int iconResId = getResources().getIdentifier(iconId, "id", getActivity().getPackageName());

            TextView nameText = (TextView) v.findViewById(nameResId);
            ImageView iconImage = (ImageView) v.findViewById(iconResId);

            String name = mItemList.get(i).getName();

            nameText.setText(name);
        }
        
        return v;
    }

}

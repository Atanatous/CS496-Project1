package com.example.user.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */

public class Matching_Fragment extends Fragment {

    private ListViewAdapter mAdapter;
    private ArrayList<ListViewItem> mItemList;
    private ArrayList<String> mPhoneNumList;
    private final int QUIZ_NUMBER = 9;
    private ArrayList<ImageView> mIconList;
    private ArrayList<TextView> mNameList;
    private int correctNum;
    private int wrongNum;
    private TextView correctText;
    private TextView wrongText;

    public Matching_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.matching_fragment, container, false);
        mItemList = new ArrayList<>();
        mPhoneNumList = new ArrayList<>();
        mIconList = new ArrayList<>();
        mNameList = new ArrayList<>();
        correctNum = 0;
        wrongNum = 0;
        correctText = (TextView) v.findViewById(R.id.correctNum);
        wrongText = (TextView) v.findViewById(R.id.wrongNum);

        // receive adapter data from mainActivity
        Bundle bundle = this.getArguments();
        if (bundle != null){
            ArrayList<ListViewAdapter> adapterList = (ArrayList< ListViewAdapter>) bundle.get("adapterList");
            mAdapter = adapterList.get(0);
        }

        // add random 9 items in itemList
        int numOfContact = mAdapter.getCount();
        Log.d("WARNING!!", "numOfContact : " + numOfContact);
        Random generator = new Random();
        ListViewItem item;


        for (int i = 0; i < ((QUIZ_NUMBER > numOfContact) ? numOfContact : QUIZ_NUMBER); i++){
            item = mAdapter.getItem(generator.nextInt(numOfContact));
            // remove duplication
            while (mItemList.contains(item)){
                item = mAdapter.getItem(generator.nextInt(numOfContact));
            }
            mItemList.add(item);
        }

        // Set Icon and Name with data passing from Contact_Fragment
        for (int i = 0; i < ((QUIZ_NUMBER > numOfContact) ? numOfContact : QUIZ_NUMBER); i++){
            String nameId = "name" + (i+1);
            String iconId = "icon" + (i+1);
            int nameResId = getResources().getIdentifier(nameId, "id", getActivity().getPackageName());
            int iconResId = getResources().getIdentifier(iconId, "id", getActivity().getPackageName());

            TextView nameText = (TextView) v.findViewById(nameResId);
            ImageView iconImage = (ImageView) v.findViewById(iconResId);

            String name = mItemList.get(i).getName();
            Drawable icon = mItemList.get(i).getIcon();
            String phoneNum = mItemList.get(i).getPhoneNum();

            mPhoneNumList.add(phoneNum);
            mIconList.add(iconImage);
            mNameList.add(nameText);

            nameText.setText(name);
            iconImage.setImageDrawable(icon);

        }

        final ArrayList<String> shuffledNum = new ArrayList<>(mPhoneNumList);
        long seed = System.nanoTime();
        Collections.shuffle(shuffledNum, new Random(seed));

        final CharSequence[] shuffledSequence = shuffledNum.toArray(new CharSequence[shuffledNum.size()]);

        // Set OnClickListener on iconImage
        for (int i = 0; i < ((QUIZ_NUMBER > numOfContact) ? numOfContact : QUIZ_NUMBER); i++){
            final ImageView iconImage = mIconList.get(i);
            final String name = mItemList.get(i).getName();
            final String phoneNum = mPhoneNumList.get(i);

            iconImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(name + "님의 전화번호는 무엇일까요?")
                            .setItems(shuffledSequence, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int index) {
                                    if (shuffledNum.get(index).equals(phoneNum)){
                                        Correct(iconImage);
                                    }else{
                                        Wrong(iconImage);
                                    }
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        return v;
    }

    private void Correct(ImageView iconImage){
        Drawable circleMark = ContextCompat.getDrawable(getActivity(), R.drawable.correct);
        iconImage.setImageDrawable(circleMark);
        iconImage.setOnClickListener(null);
        correctNum++;
        correctText.setText("맞은 갯수 : " + correctNum);
        Toast.makeText(getActivity(), "맞았습니다!", Toast.LENGTH_LONG).show();
    }

    private void Wrong(ImageView iconImage){
        Drawable wrongMark = ContextCompat.getDrawable(getActivity(), R.drawable.wrong);
        iconImage.setImageDrawable(wrongMark);
        iconImage.setOnClickListener(null);
        wrongNum++;
        wrongText.setText("틀린 갯수 : " + wrongNum);
        Toast.makeText(getActivity(), "틀렸습니다..", Toast.LENGTH_LONG).show();
    }

}

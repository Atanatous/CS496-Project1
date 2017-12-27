package com.example.user.project1;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    private Button mAddressBtn;
    private ListView mListView;
    private Cursor mCursor;
    private List<String> mContactList;
    private ArrayAdapter mAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment1, container, false);

        mAddressBtn = (Button) v.findViewById(R.id.btnAddress);
        mAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdaptor = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mContactList);
                mListView.setAdapter(mAdaptor);
            }
        });

        mListView = (ListView) v.findViewById(R.id.contact_list_view);
        mContactList = new ArrayList<>();

        ContentResolver cr = getActivity().getContentResolver();
        mCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        mCursor.moveToFirst();

        while (mCursor.moveToNext()){
            int phoneidx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameidx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String result = mCursor.getString(nameidx) + " : " + mCursor.getString(phoneidx);
            mContactList.add(result);
        }

        Collections.sort(mContactList, new CompareNameDesc());

        return v;
    }

    static class CompareNameDesc implements Comparator<String>{
        @Override
        public int compare(String o1, String o2){
            return o1.compareTo(o2);
        }
    }

}


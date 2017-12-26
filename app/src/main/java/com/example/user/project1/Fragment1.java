package com.example.user.project1;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.invoke.ConstantCallSite;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Cursor cursor = getURI();

        int count = 0;
        int end = cursor.getCount();
        String[] name = new String[end];
        String[] phoneNumber = new String[end];

        if (cursor.moveToFirst()){
            // 컬럼명으로 컬럼 인덱스 찾기
            int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        }

        return inflater.inflate(R.layout.fragment_fragment1, container, false);
    }

    private Cursor getURI(){

        // 주소록 URI
        Uri people = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // 검색할 Column 정하기
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // 쿼리 날려서 커서 얻기
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return this.getActivity().managedQuery(people, projection, null, selectionArgs, sortOrder);
    }

}


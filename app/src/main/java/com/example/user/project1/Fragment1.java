package com.example.user.project1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    Button mAddressBtn;
    private ListView mListView;
    Cursor mCursor;
    private ListViewAdapter mAdaptor;
    private ListViewItem item;
    final int REQ_CODE_SELECT_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment1, container, false);

        mListView = (ListView) v.findViewById(R.id.contact_list_view);
        mAdaptor = new ListViewAdapter();

        ContentResolver cr = getActivity().getContentResolver();
        mCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        mCursor.moveToFirst();

        while (mCursor.moveToNext()){
            int phoneidx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameidx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            String name = mCursor.getString(nameidx);
            String phoneNum = mCursor.getString(phoneidx);

            if (mAdaptor.isDuplicate(name, phoneNum)){
                continue;
            }else {
                mAdaptor.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_account_box),
                        name, phoneNum);
            }
        }

        Collections.sort(mAdaptor.getItemList(), new CompareNameDesc());

        mAddressBtn = (Button) v.findViewById(R.id.btnAddress);
        mAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.setAdapter(mAdaptor);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item = (ListViewItem) parent.getItemAtPosition(position);
                final CharSequence[] items = {"사진 추가", "수정하기", "삭제하기"};

                String name = item.getName();
                String phoneNum = item.getPhoneNum();
                Drawable icon = item.getIcon();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("원하시는 작업을 선택하세요.")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                switch(index){
                                    case 0:
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK){
            Toast.makeText(getActivity(), "Failed...", Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            try {
                Bitmap mImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                Drawable mImageDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(mImage, 160, 160, true));

                item.setIcon(mImageDrawable);
                mAdaptor.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class CompareNameDesc implements Comparator<ListViewItem>{
        @Override
        public int compare(ListViewItem o1, ListViewItem o2){
            return o1.getName().compareTo(o2.getName());
        }
    }

}


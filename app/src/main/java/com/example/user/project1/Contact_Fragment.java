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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Contact_Fragment extends Fragment {
    private ListView mListView;
    private ListViewAdapter mAdapter = new ListViewAdapter();
    private ListViewItem mItem;
    final int REQ_CODE_SELECT_IMAGE = 100;
    boolean isFirst = true;

    public ListViewAdapter getAdapter() {
        return mAdapter;
    }

    // Main function.
    // Make ListView and set listeners on it.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_fragment, container, false);

        mListView = (ListView) v.findViewById(R.id.contact_list_view);

        // At First, load contacts inside the phone
        // Next time, just follow adapter's contact.
        if (isFirst) {
            makeContactList();
            isFirst = false;
        }else{
            mListView.setAdapter(mAdapter);
        }

        // Make Reset Button
        Button mAddressBtn = (Button) v.findViewById(R.id.btnAddress);
        mAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.getItemList().clear();
                makeContactList();
            }
        });

        //Set ShortClick Listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pass data 'adapter' and 'clicked item' to Detail_Fragment
                // To pass, use bundle packing.
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                ArrayList<ListViewItem> itemList = new ArrayList<>();
                ArrayList<ListViewAdapter> adapterList = new ArrayList<>();
                itemList.add(item);
                adapterList.add(mAdapter);

                ContactDetail_Fragment detail_fragment = new ContactDetail_Fragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemList", itemList);
                bundle.putSerializable("adapterList", adapterList);
                detail_fragment.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack("List");
                transaction.replace(R.id.fragment_container, detail_fragment);
                transaction.commit();
            }
        });


        // Set LongClick Listener
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mItem = (ListViewItem) parent.getItemAtPosition(position);
                final CharSequence[] items = {"사진 추가", "수정하기", "삭제하기"};
                final String name = mItem.getName();

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
                                        mAdapter.getItemList().remove(mItem);
                                        Toast.makeText(getActivity(), name +"이(가) 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                        mAdapter.notifyDataSetChanged();
                                        break;
                                }
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        return v;
    }

    // Load all contact data
    private void makeContactList(){
        ContentResolver cr = getActivity().getContentResolver();
        Cursor mCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        mCursor.moveToFirst();

        while (mCursor.moveToNext()){
            int phoneidx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameidx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            String name = mCursor.getString(nameidx);
            String phoneNum = mCursor.getString(phoneidx);

            // make phone number pretty
            if (!phoneNum.contains("-")){
                if (phoneNum.length() == 8){
                    phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 8) + "-" + phoneNum.substring(8);
                }
                else{
                    phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7);
                }
            }

            // check if adaptor has same contact
            if (mAdapter.isDuplicate(name, phoneNum)){
                continue;
            }else {
                mAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_account_box),
                        name, phoneNum);
            }
        }

        Collections.sort(mAdapter.getItemList(), new CompareNameDesc());
        mListView.setAdapter(mAdapter);
    }


    // Get Photo from inside gallery.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK){
            return;
        }

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            try {
                Bitmap mImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                Drawable mImageDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(mImage, 300, 300, true));

                mItem.setIcon(mImageDrawable);
                mAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // To sort mAdapter.
    static class CompareNameDesc implements Comparator<ListViewItem>{
        @Override
        public int compare(ListViewItem o1, ListViewItem o2){
            return o1.getName().compareTo(o2.getName());
        }
    }

}


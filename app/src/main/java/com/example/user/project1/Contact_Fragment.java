package com.example.user.project1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        final View v = inflater.inflate(R.layout.contact_fragment, container, false);

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
                final String phoneNum = mItem.getPhoneNum();

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
                                        AlertDialog.Builder modifyBuilder = new AlertDialog.Builder(getActivity());
                                        modifyBuilder.setTitle("연락처 수정");

                                        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        LinearLayout modifyLayout = (LinearLayout) layoutInflater.inflate(R.layout.contact_modify, null, false);
                                        final EditText nameEdit = modifyLayout.findViewById(R.id.nameEdit);
                                        final EditText phoneEdit = modifyLayout.findViewById(R.id.phoneEdit);

                                        phoneEdit.addTextChangedListener(new TextWatcher() {
                                            boolean isFormatting;
                                            boolean deletingHyphen;
                                            int hyphenStart;
                                            boolean deletingBackward;

                                            @Override
                                            public void onTextChanged(CharSequence sequence, int start, int before, int count){

                                            }

                                            @Override
                                            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
                                                if (isFormatting)
                                                    return;

                                                final int selStart = Selection.getSelectionStart(sequence);
                                                final int selEnd = Selection.getSelectionEnd(sequence);
                                                if (sequence.length() > 1
                                                        && count == 1
                                                        && after == 0
                                                        && sequence.charAt(start) == '-'
                                                        && selStart == selEnd){
                                                    deletingHyphen = true;
                                                    hyphenStart = start;
                                                    if (selStart == start + 1){
                                                        deletingBackward = true;
                                                    } else {
                                                        deletingBackward = false;
                                                    }
                                                } else {
                                                    deletingHyphen = false;
                                                }
                                            }


                                            @Override
                                            public void afterTextChanged(Editable text) {
                                                    if (isFormatting)
                                                        return;

                                                    isFormatting = true;

                                                    if (deletingHyphen && hyphenStart > 0){
                                                        if(deletingBackward){
                                                            if (hyphenStart - 1 < text.length()){
                                                                text.delete(hyphenStart - 1, hyphenStart);
                                                        }
                                                    } else if (hyphenStart < text.length()){
                                                            text.delete(hyphenStart, hyphenStart + 1);
                                                        }
                                                    }
                                                    if (text.length() == 3 || text.length() == 8){
                                                        text.append('-');
                                                    }

                                                    isFormatting = false;
                                                }
                                            });

                                        nameEdit.setText(name);
                                        phoneEdit.setText(phoneNum);
                                        modifyBuilder.setView(modifyLayout);

                                        modifyBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String modifiedName = nameEdit.getText().toString();
                                                String modifiedNum = phoneEdit.getText().toString();
                                                mItem.setName(modifiedName);
                                                mItem.setPhoneNum(modifiedNum);
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        });

                                        modifyBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });

                                        modifyBuilder.show();
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


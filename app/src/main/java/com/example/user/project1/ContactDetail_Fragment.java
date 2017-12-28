package com.example.user.project1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactDetail_Fragment extends Fragment {
    private ListViewItem mItem;
    private ListViewAdapter mAdapter;

    public ContactDetail_Fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.contact_detail_fragment, container, false);
        ArrayList<ListViewItem> itemList;
        ArrayList<ListViewAdapter> adapterList;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            itemList = (ArrayList<ListViewItem>) getArguments().get("itemList");
            adapterList = (ArrayList<ListViewAdapter>) getArguments().get("adapterList");
            mItem = itemList.get(0);
            mAdapter = adapterList.get(0);
        }else{
            Log.d(null, "onCreateView: Fucking where is my Args");
        }

        final ImageView backBtn = (ImageView) v.findViewById(R.id.backBtn);
        final ImageView removeBtn = (ImageView) v.findViewById(R.id.removeBtn);
        final ImageView detail_icon = (ImageView) v.findViewById(R.id.detail_icon);
        final TextView detail_name = (TextView) v.findViewById(R.id.detail_name);
        final TextView detail_phoneNum = (TextView) v.findViewById(R.id.detail_phoneNum);

        detail_icon.setImageDrawable(mItem.getIcon());
        detail_name.setText(mItem.getName());
        detail_phoneNum.setText(mItem.getPhoneNum());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(ContactDetail_Fragment.this).commit();
                fragmentManager.popBackStack();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(detail_name.getText() + "님을 연락처에서 삭제하시겠습니까?").setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // remove data from listViewAdapter
                        mAdapter.getItemList().remove(mItem);
                        mAdapter.notifyDataSetChanged();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().remove(ContactDetail_Fragment.this).commit();
                        fragmentManager.popBackStack();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return v;
    }
}

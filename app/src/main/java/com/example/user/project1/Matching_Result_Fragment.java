package com.example.user.project1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class Matching_Result_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.matching_result_fragment, container, false);

        Bundle bundle = this.getArguments();
        int correctNum = bundle.getInt("correctNum");

        ImageView restartBtn = (ImageView) v.findViewById(R.id.restartBtn);
        TextView resultNum = (TextView) v.findViewById(R.id.resultNum);

        resultNum.setText("총 " + correctNum + "명 맞추셨습니다!");

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).callFragment(3);
            }
        });



        return v;
    }
}

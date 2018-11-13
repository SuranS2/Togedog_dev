package com.example.togedog.togedog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class VotelayoutActivity extends AppCompatActivity {
    LinearLayout linear1,linear2,linear3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_layout);


        Intent intent = getIntent() ;


        TextView TextViewSpot = (TextView) findViewById(R.id.TextViewSpot) ;
        String spot = intent.getStringExtra("Spot") ;
        if (spot != null)
            TextViewSpot.setText(spot) ;


        TextView TextViewCalender = (TextView) findViewById(R.id.TextViewCalender) ;
        String calender = intent.getStringExtra("Calender") ;
        if (calender != null)
            TextViewCalender.setText(calender) ;


        TextView TextViewTime = (TextView) findViewById(R.id.TextViewTime) ;
        String time= intent.getStringExtra("Time") ;
        if (time != null)
            TextViewTime.setText(time) ;

        TextView TextViewMinute = (TextView) findViewById(R.id.TextViewMinute) ;
        String minute= intent.getStringExtra("Minute") ;
        if (minute != null)
            TextViewMinute.setText(minute) ;





        TextView TextViewSpot2 = (TextView) findViewById(R.id.TextViewSpot2) ;
        String spot2 = intent.getStringExtra("Spot2") ;
        if (spot2 != null)
            TextViewSpot2.setText(spot2) ;


        TextView TextViewCalender2 = (TextView) findViewById(R.id.TextViewCalender2) ;
        String calender2 = intent.getStringExtra("Calender2") ;
        if (calender2 != null)
            TextViewCalender2.setText(calender2) ;


        TextView TextViewTime2 = (TextView) findViewById(R.id.TextViewTime2) ;
        String time2= intent.getStringExtra("Time2") ;
        if (time2 != null)
            TextViewTime2.setText(time2) ;

        TextView TextViewMinute2 = (TextView) findViewById(R.id.TextViewMinute2) ;
        String minute2= intent.getStringExtra("Minute2") ;
        if (minute2 != null)
            TextViewMinute2.setText(minute2) ;




        TextView TextViewSpot3 = (TextView) findViewById(R.id.TextViewSpot3) ;
        String spot3 = intent.getStringExtra("Spot3") ;
        if (spot3 != null)
            TextViewSpot3.setText(spot3) ;


        TextView TextViewCalender3 = (TextView) findViewById(R.id.TextViewCalender3) ;
        String calender3 = intent.getStringExtra("Calender3") ;
        if (calender3 != null)
            TextViewCalender3.setText(calender3) ;


        TextView TextViewTime3 = (TextView) findViewById(R.id.TextViewTime3) ;
        String time3= intent.getStringExtra("Time3") ;
        if (time3 != null)
            TextViewTime3.setText(time3) ;

        TextView TextViewMinute3 = (TextView) findViewById(R.id.TextViewMinute3) ;
        String minute3= intent.getStringExtra("Minute3") ;
        if (minute3 != null)
            TextViewMinute3.setText(minute3) ;


        linear1=(LinearLayout)findViewById(R.id.linear_1);
        linear1.setOnClickListener(mOnclicklistener);
        linear2=(LinearLayout)findViewById(R.id.linear_2);
        linear2.setOnClickListener(mOnclicklistener);
        linear3=(LinearLayout)findViewById(R.id.linear_3);
        linear3.setOnClickListener(mOnclicklistener);

    }
    private View.OnClickListener mOnclicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
            switch (v.getId()){
                case R.id.linear_1:
                    break;
                case R.id.linear_2:
                    break;
                case R.id.linear_3:
                    break;
            }

        }
    };

    public void onClick(View view) {
        switch(view.getId()){

            case R.id.button2_cancel_vote:
                finish();
                break;
        }
    }
}

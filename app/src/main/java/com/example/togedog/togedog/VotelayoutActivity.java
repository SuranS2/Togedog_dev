package com.example.togedog.togedog;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import static android.widget.Toast.makeText;

public class VotelayoutActivity extends AppCompatActivity {
    LinearLayout linear1,linear2,linear3;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference dref;
    private String chat_name;
    TextView TextViewSpot,TextViewCalender,TextViewTime,TextViewMinute;
    TextView TextViewSpot2,TextViewCalender2,TextViewTime2,TextViewMinute2;
    TextView TextViewSpot3,TextViewCalender3,TextViewTime3,TextViewMinute3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //파이어베이스 정의
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dref = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        chat_name = intent.getStringExtra("chat_name");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_layout);

        final DatabaseReference place_day1 = dref.child("chat_room").child(chat_name).child("vote").child("place_day1");
        final DatabaseReference place_hour1 = dref.child("chat_room").child(chat_name).child("vote").child("place_hour1");
        final DatabaseReference place_min1 = dref.child("chat_room").child(chat_name).child("vote").child("place_min1");
        final DatabaseReference place_day2 = dref.child("chat_room").child(chat_name).child("vote").child("place_day2");
        final DatabaseReference place_hour2 = dref.child("chat_room").child(chat_name).child("vote").child("place_hour2");
        final DatabaseReference place_min2 = dref.child("chat_room").child(chat_name).child("vote").child("place_min2");
        final DatabaseReference place_day3 = dref.child("chat_room").child(chat_name).child("vote").child("place_day3");
        final DatabaseReference place_hour3 = dref.child("chat_room").child(chat_name).child("vote").child("place_hour3");
        final DatabaseReference place_min3 = dref.child("chat_room").child(chat_name).child("vote").child("place_min3");


        TextViewSpot = (TextView) findViewById(R.id.TextViewSpot) ;
        TextViewCalender = (TextView) findViewById(R.id.TextViewCalender) ;
        TextViewTime=(TextView)findViewById(R.id.TextViewTime);
        TextViewMinute=(TextView)findViewById(R.id.TextViewMinute);

        TextViewSpot2 = (TextView) findViewById(R.id.TextViewSpot2) ;
        TextViewCalender2 = (TextView) findViewById(R.id.TextViewCalender2) ;
        TextViewTime2=(TextView)findViewById(R.id.TextViewTime2);
        TextViewMinute2=(TextView)findViewById(R.id.TextViewMinute2);

        TextViewSpot3 = (TextView) findViewById(R.id.TextViewSpot3) ;
        TextViewCalender3 = (TextView) findViewById(R.id.TextViewCalender3) ;
        TextViewTime3=(TextView)findViewById(R.id.TextViewTime3);
        TextViewMinute3=(TextView)findViewById(R.id.TextViewMinute3);


        //날짜1
        place_day1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewCalender.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //시간1
        place_hour1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewTime.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //분1
        place_min1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewMinute.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //날짜2
        place_day2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewCalender2.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //시간2
        place_hour2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewTime2.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //시간2
        place_min2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewMinute2.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //날짜3
        place_day3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewCalender3.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //시간3
        place_hour3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewTime3.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //시간3
        place_min3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                TextViewMinute3.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






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

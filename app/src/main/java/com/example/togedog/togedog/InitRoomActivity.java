package com.example.togedog.togedog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import static java.lang.Thread.sleep;

public class InitRoomActivity extends AppCompatActivity {

    TextView infor_name,infor_roomplace,infor_roomdoro,infor_limit;
    String infor_ropl="",infor_rodo="",infor_romda="",infor_lm="";
    ImageView infor_roomdays_mo,infor_roomdays_tu,infor_roomdays_we,infor_roomdays_thu;
    ImageView infor_roomdays_fri,infor_roomdays_sat,infor_roomdays_sun;
    ImageView infor_roomlim_six,infor_roomlim_one,infor_roomlim_so,infor_roomlim_jung,infor_roomlim_dae,infor_roomlim_un;
    int[] chat_days;
    int[] chat_limits;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference dref;
    private String chat_name;
    Button btn_regi,btn_cancle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initroom);

        infor_name=(TextView)findViewById(R.id.infor_name);
        infor_roomplace=(TextView)findViewById(R.id.infor_roomplace);
        infor_roomdays_mo=(ImageView) findViewById(R.id.infor_roomdays_mo);
        infor_roomdays_tu=(ImageView) findViewById(R.id.infor_roomdays_tu);
        infor_roomdays_we=(ImageView) findViewById(R.id.infor_roomdays_we);
        infor_roomdays_thu=(ImageView) findViewById(R.id.infor_roomdays_thu);
        infor_roomdays_fri=(ImageView) findViewById(R.id.infor_roomdays_fri);
        infor_roomdays_sat=(ImageView) findViewById(R.id.infor_roomdays_sat);
        infor_roomdays_sun=(ImageView) findViewById(R.id.infor_roomdays_sun);

        infor_roomlim_six=(ImageView) findViewById(R.id.infor_roomlim_six);
        infor_roomlim_one=(ImageView) findViewById(R.id.infor_roomlim_one);
        infor_roomlim_so=(ImageView) findViewById(R.id.infor_roomlim_so);
        infor_roomlim_dae=(ImageView) findViewById(R.id.infor_roomlim_dae);
        infor_roomlim_jung=(ImageView) findViewById(R.id.infor_roomlim_jung);
        infor_roomlim_un=(ImageView) findViewById(R.id.infor_roomlim_un);
        btn_regi=(Button)findViewById(R.id.infor_register);
        btn_cancle=(Button)findViewById(R.id.infor_cancle);

        chat_days= new int[]{0, 0, 0, 0, 0, 0, 0};
        chat_limits=new int[]{0,0,0,0,0,0};



        //파이어베이스 정의
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dref = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        chat_name = intent.getStringExtra("chat_name");

        DatabaseReference RoomName = dref.child("chat_room").child(chat_name).child("info").child("RoomName");
        DatabaseReference Monday = dref.child("chat_room").child(chat_name).child("info").child("Monday");
        DatabaseReference Tuesday = dref.child("chat_room").child(chat_name).child("info").child("Tuesday");
        DatabaseReference Wednesday = dref.child("chat_room").child(chat_name).child("info").child("Wednesday");
        DatabaseReference Thursday = dref.child("chat_room").child(chat_name).child("info").child("Thursday");
        DatabaseReference Friday = dref.child("chat_room").child(chat_name).child("info").child("Friday");
        DatabaseReference Saturday = dref.child("chat_room").child(chat_name).child("info").child("Saturday");
        DatabaseReference Sunday = dref.child("chat_room").child(chat_name).child("info").child("Sunday");
        DatabaseReference LimitSixmonth = dref.child("chat_room").child(chat_name).child("info").child("LimitSixmonth");
        DatabaseReference LimitYear = dref.child("chat_room").child(chat_name).child("info").child("LimitYear");
        DatabaseReference LimitSo = dref.child("chat_room").child(chat_name).child("info").child("LimitSo");
        DatabaseReference LimitJung = dref.child("chat_room").child(chat_name).child("info").child("LimitJung");
        DatabaseReference LimitDae = dref.child("chat_room").child(chat_name).child("info").child("LimitDae");
        DatabaseReference UnLimit = dref.child("chat_room").child(chat_name).child("info").child("UnLimit");
        DatabaseReference Warning = dref.child("chat_room").child(chat_name).child("info").child("Warning");


        //제목
        RoomName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String RoomName = dataSnapshot.getValue(String.class);
                infor_name.setText(RoomName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //요일 -월
        Monday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int Mon = dataSnapshot.getValue(int.class);
                if(Mon==1){
                    infor_roomdays_mo.setImageResource(R.drawable.mon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //화
        Tuesday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int Tues = dataSnapshot.getValue(int.class);
                if(Tues==1){
                    infor_roomdays_tu.setImageResource(R.drawable.tues);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //수
        Wednesday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int Wedn = dataSnapshot.getValue(int.class);
                if(Wedn==1){
                    infor_roomdays_we.setImageResource(R.drawable.wednes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //목
        Thursday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int Thur = dataSnapshot.getValue(int.class);
                if(Thur==1){
                    infor_roomdays_thu.setImageResource(R.drawable.thurs);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //금
        Friday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int Fri = dataSnapshot.getValue(int.class);
                if(Fri==1){
                    infor_roomdays_fri.setImageResource(R.drawable.fri);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //토
        Saturday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int Satur = dataSnapshot.getValue(int.class);
                if(Satur==1){
                    infor_roomdays_sat.setImageResource(R.drawable.satur);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //일
        Sunday.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int Sun= dataSnapshot.getValue(int.class);
                if(Sun==1){
                    infor_roomdays_sun.setImageResource(R.drawable.sun);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //제한-여섯살
        LimitSixmonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int LimitSix = dataSnapshot.getValue(int.class);
                if(LimitSix==1){
                    infor_roomlim_six.setImageResource(R.drawable.limbt1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //년도
        LimitYear.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int LimitYe = dataSnapshot.getValue(int.class);
                if(LimitYe==1){
                    infor_roomlim_one.setImageResource(R.drawable.limbt2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //소형견
        LimitSo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int LimitS = dataSnapshot.getValue(int.class);
                if(LimitS==1){
                    infor_roomlim_so.setImageResource(R.drawable.limbt3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //중형견
        LimitJung.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int LimitJu = dataSnapshot.getValue(int.class);
                if(LimitJu==1){
                    infor_roomlim_jung.setImageResource(R.drawable.limbt4);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //대형견
        LimitDae.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int LimitDae = dataSnapshot.getValue(int.class);
                if(LimitDae==1){
                    infor_roomlim_dae.setImageResource(R.drawable.limbt5);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //제한없음
        UnLimit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int UnLimi = dataSnapshot.getValue(int.class);
                if(UnLimi==1){
                    infor_roomlim_un.setImageResource(R.drawable.limbt6);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try{
            sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        btn_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(getApplicationContext(), ChatActivity.class);
                intent2.putExtra("chat_name" , chat_name );
                startActivity(intent2);
            }
        });


        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

    }
}

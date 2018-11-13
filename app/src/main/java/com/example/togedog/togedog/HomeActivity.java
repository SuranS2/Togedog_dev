package com.example.togedog.togedog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private EditText user_edit;
    private ListView chat_list;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    Activity act = this;
    public static GridView gridView;

    int day[]=new int[7];
    int mon_day=0,tue_day=0,wed_day=0,thur_day=0,fri_day=0,sat_day=0,sun_day=0;

    public static ArrayList<String> title = new ArrayList<String>();
    public ArrayList<String> search = new ArrayList<String>();
    public ArrayList<String> time = new ArrayList<String>();
    public ArrayList<int[]> days = new ArrayList<int[]>();

    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7;
    int count=1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent = getIntent();


        gridView = (GridView) findViewById(R.id.Home_grid);

        final String six_month=intent.getStringExtra("six_month");
        final String year=intent.getStringExtra("year");
        final String small=intent.getStringExtra("small");
        final String medium=intent.getStringExtra("medium");
        final String big=intent.getStringExtra("big");
        final String unlimit=intent.getStringExtra("unlimit");


        final String mon=intent.getStringExtra("mon");
        final String tue=intent.getStringExtra("tue");
        final String wed=intent.getStringExtra("wed");
        final String thu=intent.getStringExtra("the");
        final String fri=intent.getStringExtra("fri");
        final String sat=intent.getStringExtra("sat");
        final String sun=intent.getStringExtra("sun");

        final String edit1 = intent.getStringExtra("chatName");
        final String edit2 = intent.getStringExtra("입력한제목2");
        final String edit3 = intent.getStringExtra("입력한제목3");

        final String hour1=intent.getStringExtra("hour1");
        final String minute1=intent.getStringExtra(("minute1"));
        final String hour2=intent.getStringExtra("hour2");
        final String minute2=intent.getStringExtra(("minute2"));


        //chat화면 테스트용

        Button button2 = (Button)findViewById(R.id.Coin);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent2);
            }
        });


        //방생성 테스트용

        Button button4 =(Button)findViewById(R.id.test);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mon_day=0; tue_day=0; wed_day=0; thur_day=0; fri_day=0; sat_day=0; sun_day=0;


                title.add("제목 "+edit1);
                search.add("모임 지역 "+edit2);
                time.add("모임 시간 : "+hour1+" "+minute1+" ~ "+hour2+" "+minute2);

                if(mon!=null) mon_day=1;
                if(tue!=null) tue_day=1;
                if(wed!=null) wed_day=1;
                if(thu!=null) thur_day=1;
                if(fri!=null) fri_day=1;
                if(sat!=null) sun_day=1;
                if(sun!=null) sun_day=1;
                int day[]={mon_day,tue_day,wed_day,thur_day,fri_day,sat_day,sun_day};
                days.add(day);

                gridView.setAdapter(new gridAdapter());

            }
        });



        //즐겨찾기 목록
        Button button = (Button)findViewById(R.id.myroom_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                startActivity(intent);
            }
        });

        //방생성 버튼

//        chat_list = (ListView) findViewById(R.id.chat_list);
        Button button3 = (Button)findViewById(R.id.Create_room);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3= new Intent(getApplicationContext(), CreateActivity.class);
                //채팅리스트 id넘김
                intent3.putExtra("chat_list" , chat_list.getId());
                startActivity(intent3);
            }
        });


        //챗리스트 소환
        showChatList();
        //챗리스트 소환
    }

    //grid 추가
    public class gridAdapter extends BaseAdapter {


        LayoutInflater inflater;

        private gridAdapter() {

            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public int getCount() {
            return title.size();
        }

        @Override
        public Object getItem(int position) {
            return title.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {

                convertView = inflater.inflate(R.layout.input_gridlayout, parent, false);

            }

            TextView textView1 = (TextView) convertView.findViewById(R.id.test_input);

            TextView textView2 = (TextView) convertView.findViewById(R.id.test_input2);

            TextView textView3 = (TextView) convertView.findViewById(R.id.test_input3);



            textView1.setText(title.get(position));

            textView2.setText(search.get(position));

            textView3.setText(time.get(position));

           // imageView1.setBackgroundResource(R.drawable.mon_checked);




            return convertView;

//            user_edit = (EditText) findViewById(R.id.user_edit);

            //그리드에 방 입장버튼 추가

            //방입장 버튼에 유저이름추가
//            intent.putExtra("userName", user_edit.getText().toString());
        }
    }


    //채팅방 리스트 불러오기
    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_list.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
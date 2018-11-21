package com.example.togedog.togedog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class HomeActivity extends AppCompatActivity {

    private EditText user_edit;
    private ListView chat_list;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    Activity act = this;
    public static GridView gridView;



    //도, 시 구분
    private Spinner do_spinner;
    private ArrayAdapter<String> do_adapter;
    private ArrayList<String> do_entries;
    private Spinner gun_spinner;
    private ArrayAdapter<String> gun_adapter;
    private ArrayList<String> gun_entries;




    int day[]=new int[7];
    int mon_day=0,tue_day=0,wed_day=0,thur_day=0,fri_day=0,sat_day=0,sun_day=0;

    public static ArrayList<String> title = new ArrayList<String>();
    public ArrayList<String> search = new ArrayList<String>();
    public ArrayList<String> time = new ArrayList<String>();
    public ArrayList<int[]> days = new ArrayList<int[]>();

    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7;
    int count=1;





    private String AccessToken = "";
    private String[][] Do;
    private String[] Gun;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent HomeAct_intent = getIntent();

        try {
            ActTask task1 = new ActTask();
            AccessToken = task1.execute().get();
            DoTask task2 = new DoTask(AccessToken);
            Do = task2.DoResult( task2.execute().get() );
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //https://kin.naver.com/qna/detail.nhn?d1id=1&dirId=1040104&docId=294654337&qb=7JWI65Oc66Gc7J2065OcIOyKpO2UvOuEiCDqsJIg7LaU6rCA&enc=utf8&section=kin&rank=2&search_sort=0&spq=0&pid=T/s70wpVuFZsss9kmzVssssst6V-417469&sid=9xdQ/FBZJ/wNs524961Mgw%3D%3D

        // array.xml 에서 Do 를 가지고옵니다.
        String[] do_test = getResources().getStringArray(R.array.Do);
        // String- ArrayList 생성, String배열 list를 ArrayList로 변환합니다.
        do_entries = new ArrayList<String>(Arrays.asList(do_test));
        do_spinner = (Spinner) findViewById(R.id.Spinner_do);
        // initialize adapter
        do_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, do_entries);
        do_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        do_spinner.setAdapter(do_adapter);


        int i=0;
        do_adapter.clear();
        do_adapter.add("도 선택");
        while(true){
            if(i<Do[1].length){
                do_adapter.add(Do[1][i]);
            }else{
                break;
            }
            i++;
        }

        gun_spinner = (Spinner)findViewById(R.id.Spinner_Gun);

        // array.xml 에서 Do 를 가지고옵니다.
        String[] gun_test = getResources().getStringArray(R.array.Gun);
        // String- ArrayList 생성, String배열 list를 ArrayList로 변환합니다.
        gun_entries = new ArrayList<String>(Arrays.asList(gun_test));
        gun_spinner = (Spinner) findViewById(R.id.Spinner_Gun);
        // initialize adapter
        gun_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, gun_entries);
        gun_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gun_spinner.setAdapter(gun_adapter);
        do_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(0 < position){
                    try {
                        Log.d("시군구 선택" ,"테스트");
                        gun_adapter.clear();
                        gun_adapter.add("시/군/구 선택");
                        DoTask task2 = new DoTask(AccessToken);

                        GunTask task3 = new GunTask(AccessToken, Integer.parseInt( Do[0][position-1] ) );
                        Gun = task3.GunResult(task3.execute().get() );
                        int i=0;
                        while(true){
                            if(i<Gun.length){
                                gun_adapter.add(Gun[i]);
                            }else{
                                break;
                            }
                            i++;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else {
                    gun_adapter.clear();
                    gun_adapter.add("시/군/구 선택");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });








        gridView = (GridView) findViewById(R.id.Home_grid);




        //즐겨찾기 목록
        Button Myroombt = (Button)findViewById(R.id.myroom_bt);
        Myroombt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                startActivity(intent);
            }
        });

        chat_list = (ListView) findViewById(R.id.chat_list);



        //방생성 버튼
        Button Crbt = (Button)findViewById(R.id.Create_room);
        Crbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3= new Intent(getApplicationContext(), CreateActivity.class);
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
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        adapter.clear();
        chat_list.setAdapter(adapter);

        //리스트 액션 추가
        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent infor_room = new Intent(getApplicationContext(),InitRoomActivity.class);
                Log.d("에러 : " , chat_list.getItemAtPosition(position).toString());
                infor_room.putExtra("chat_name" , chat_list.getItemAtPosition(position).toString() );
                startActivity(infor_room);

            }
        });

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat_room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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


    //요청주소
// https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json
//보안키
// 3e509c339f3448acbb82
//서비스id
// 701fe9171c3541c2b601


//도 제공 url
//    URL url = new URL("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?"
//            // 도 검색
//            + "pg_yn=0&"
//            //액세스 토큰
//            + " accessToken=a0472395-a539-4f67-be40-6ad967bc4329"
//    );

// 액세스 토큰 얻는 링크
//                        url = new URL("https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json?"
//                                + "consumer_key=b0de22ac13c54e3e869f&"
//                                + "consumer_secret=8c4f11f00f41453f92a4");

    class ActTask extends AsyncTask<String, Void , String> {
        private String str, receiveMsg;
        private URL url = null;
        ActTask(){
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json?"
                        + "consumer_key=701fe9171c3541c2b601&"
                        + "consumer_secret=3e509c339f3448acbb82");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    reader.close();
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return AccessToken(receiveMsg);
        }
        private String AccessToken(String jsonString) {

            String AccessT = null;
            try {
                JSONObject jObj = new JSONObject(jsonString);
                JSONObject ActObj = jObj.getJSONObject("result");
                AccessT = ActObj.getString("accessToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return AccessT;
        }
    }
    class DoTask extends AsyncTask<String, Void , String> {
        private String str, receiveMsg;
        private URL url = null;

        //도 제공 url
        DoTask(String AccessToken) {
            try {
                this.url = new URL("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?"
                        + "pg_yn=0&"
                        + "accessToken=" + AccessToken);
            } /*catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } */ catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    reader.close();
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        // initialize adapter

        public String[][] DoResult(String jsonString) {
            String cd = null;
            String addr_name = null;
            String[][] array = new String[2][17];
            try {
                JSONArray jarray = new JSONObject(jsonString).getJSONArray("result");
                for (int i = 0; i < jarray.length(); i++) {
                    HashMap map = new HashMap<>();
                    JSONObject jObject = jarray.getJSONObject(i);

                    cd = jObject.optString("cd");
                    addr_name = jObject.optString("addr_name");

                    array[0][i] = cd;
                    array[1][i] = addr_name;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return array;
        }
    }
    class GunTask extends AsyncTask<String, Void , String> {
        private String str, receiveMsg;
        private URL url = null;
        private int cd;

        //군 제공 url
        GunTask(String AccessToken, int cd) {
            try {
                this.url = new URL("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?"
                        + "pg_yn=0&"
                        + "cd=" + cd + "&"
                        + "accessToken=" + AccessToken);
                this.cd = cd;
            } /*catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } */ catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("receiveMsg : ", receiveMsg);

                    reader.close();
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (java.net.MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        public String[] GunResult(String jsonString) {
//            String cd = null;
            String addr_name = null;
            ArrayList<String> StringArray = new ArrayList<String>();
            try {
                JSONArray jarray = new JSONObject(jsonString).getJSONArray("result");
                for (int i = 0; i < jarray.length(); i++) {
                    HashMap map = new HashMap<>();
                    JSONObject jObject = jarray.getJSONObject(i);

//                    cd = jObject.optString("cd");
                    addr_name = jObject.optString("addr_name");

//                    array[0][i] = cd;
//                    array[i] = addr_name;
                    StringArray.add(addr_name);

                }
                for (int i = 0; i < StringArray.size(); i++) {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] array = StringArray.toArray(new String[0]);
//            String 배열 인스턴스가 파라메터로 넘어갔는데, size를 '0'으로 명시했다.
//            이것이 무슨의미일까? 정리하자면 아래와 같다.
//
//            1. List를 toArray 메서드에 파라메터로 넘어가는 배열 객체의 size만큼의 배열로 전환한다.
//            2. 단, 해당 List size가 인자로 넘어가는 배열 객체의 size보다 클때, 해당 List의 size로 배열이 만들어진다.
//            3. 반대로 해당 List size가 인자로 넘어가는 배열객체의 size보다 작을때는, 인자로 넘어가는 배열객체의 size로 배열이 만들어진다.
//
//                    여기서 위의 예제에서의 stringArray의 크기는 '3' 이다. 인자로 넘어가는 배열의 size가 '0'이므로, 원래 List의 size로 배열이 만들어진 것이다.
            //http://asuraiv.blogspot.com/2015/06/java-list-toarray.html
            return array;
        }
    }
}
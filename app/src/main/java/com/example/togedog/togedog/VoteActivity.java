package com.example.togedog.togedog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

public class VoteActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{


    TextView mTV;
    TextView mTV2;
    TextView mTV3;

    Button mBtn;
    Button mBtn2;
    Button mBtn3;

    Calendar c;
    DatePickerDialog dpd;

    EditText editTextSpot,editTextSpot2,editTextSpot3;
    Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6;
    TextView TextViewTextCalender,TextViewTextCalender2,TextViewTextCalender3;
    TextView textView_v1,textview_vp1,textView_v2,textview_vp2,textview_v3,textview_vp3;


    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    private VoteDTO vote_dto;
    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    protected GoogleApiClient mGoogleApiClient;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    String chat_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Intent intent = getIntent() ;
        chat_name = intent.getStringExtra("chat_name") ;

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .build();


        Button button = (Button)findViewById(R.id.button2_cancel_vote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTV = (TextView)findViewById(R.id.txtView);
        mBtn = (Button)findViewById(R.id.btnPick);
        textView_v1=(TextView)findViewById(R.id.TextView_v1);
        textview_vp1=(TextView)findViewById(R.id.TextView_vp1);


        mTV2 = (TextView)findViewById(R.id.txtView2);
        mBtn2 = (Button)findViewById(R.id.btnPick2);
        textView_v2=(TextView)findViewById(R.id.TextView_v2);
        textview_vp2=(TextView)findViewById(R.id.TextView_vp2);

        mTV3 = (TextView)findViewById(R.id.txtView3);
        mBtn3 = (Button)findViewById(R.id.btnPick3);
        textview_v3=(TextView)findViewById(R.id.TextView_v3);
        textview_vp3=(TextView)findViewById(R.id.TextView_vp3);

        textView_v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(VoteActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                c= Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd =new DatePickerDialog(VoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {

                        mTV.setText(mYear + "년" + (mMonth+1) + "월" + mDay +"일");
                    }
                }, day,month,year);
                dpd.show();
            }
        });
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c=Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd =new DatePickerDialog(VoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {

                        mTV2.setText(mYear + "년" + (mMonth+1) + "월" + mDay+ "일");
                    }
                }, day,month,year);
                dpd.show();
            }
        });



        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c=Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd =new DatePickerDialog(VoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {

                        mTV3.setText(mYear + "년" + (mMonth+1) + "월" + mDay+ "일");
                    }
                }, day,month,year);
                dpd.show();
            }
        });

        //방생성

        Button button_create_vote = (Button) findViewById(R.id.button_create_vote) ;

        button_create_vote.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(VoteActivity.this, VotelayoutActivity.class);

                  // public String place_day1;
                  //    public String place_hour1;
                  //    public String place_min1;

                    updateVoteList(chat_name);


/*
                  editTextSpot2 = (EditText) findViewById(R.id.editText2);
                  intent.putExtra("Spot2", editTextSpot2.getText().toString());




                  EditText editTextSpot3 = (EditText) findViewById(R.id.editText3);
                  intent.putExtra("Spot3", editTextSpot3.getText().toString());




                  startActivity(intent);
                  finish();
*/


              }
        });




        Spinner yearSpinner = (Spinner)findViewById(R.id.vote_time1);

        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearAdapter);


        Spinner yearSpinner2 = (Spinner)findViewById(R.id.vote_time2);

        ArrayAdapter yearAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);

        yearAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner2.setAdapter(yearAdapter2);



        Spinner yearSpinner3 = (Spinner)findViewById(R.id.vote_time3);

        ArrayAdapter yearAdapter3 = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);

        yearAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner3.setAdapter(yearAdapter2);





        Spinner monthSpinner = (Spinner)findViewById(R.id.vote_minute1);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner.setAdapter(monthAdapter);



        Spinner monthSpinner2 = (Spinner)findViewById(R.id.vote_minute2);

        ArrayAdapter monthAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);

        monthAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner2.setAdapter(monthAdapter2);


        Spinner monthSpinner3 = (Spinner)findViewById(R.id.vote_minute3);

        ArrayAdapter monthAdapter3 = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);

        monthAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthSpinner3.setAdapter(monthAdapter3);



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST: {
                try {
                    final Place place = PlacePicker.getPlace(this, data);
                    final CharSequence addres2 = place.getName();
                    final CharSequence address2 = place.getAddress();
                    String attributions = (String) place.getAttributions();
                    if (attributions == null) {
                        attributions = "";
                    }
                    String addres_2 = addres2.toString();
                    String address_2 = address2.toString();


                    vote_dto.place_where1= addres_2;//장소이름
                    vote_dto.place_spwhere1 = address_2;//장소위치
                    textView_v1.setText(addres_2);
                    textview_vp1.setText(address_2);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);

        }

    }
    public void updateVoteList(String chat_name) {
    try {

        VoteDTO vote_dto = new VoteDTO();

        TextViewTextCalender = (TextView) findViewById(R.id.txtView);
        vote_dto.place_day1 = TextViewTextCalender.getText().toString();


        spinner1 = (Spinner) findViewById(R.id.vote_time1);
        vote_dto.place_hour1 = spinner1.getSelectedItem().toString();

        spinner2 = (Spinner) findViewById(R.id.vote_minute1);
        vote_dto.place_min1 = spinner2.getSelectedItem().toString();

        TextViewTextCalender2 = (TextView) findViewById(R.id.txtView2);
        vote_dto.place_day2= TextViewTextCalender2.getText().toString();


        spinner3 = (Spinner) findViewById(R.id.vote_time2);
        vote_dto.place_hour2=spinner3.getSelectedItem().toString();

        spinner4 = (Spinner) findViewById(R.id.vote_minute2);
        vote_dto.place_min2=spinner4.getSelectedItem().toString();

        TextViewTextCalender3 = (TextView) findViewById(R.id.txtView3);
        vote_dto.place_day3=TextViewTextCalender3.getText().toString();


        Spinner spinner5 = (Spinner) findViewById(R.id.vote_time3);
        vote_dto.place_hour3= spinner5.getSelectedItem().toString();

        Spinner spinner6 = (Spinner) findViewById(R.id.vote_minute3);
        vote_dto.place_min3=spinner6.getSelectedItem().toString();



        //  ChatDTO chat = new ChatDTO("☺", auth.getCurrentUser().getDisplayName() + "님이 채팅방을 생성했습니다.");
//        conditionRef.child(chat_name.toString()).setValue(ChatInfoDTO);

        vote_dto.vote_room_name = chat_name;
        //  databaseReference.ch
        //        dref = FirebaseDatabase.getInstance().getReference()ild("chat_room").child(vote_dto.vote_room_name).child("vote").child("RoomName").setValue(vote_dto.vote_room_name);
        // databaseReference.child("chat_room").child(vote_dto.vote_room_name).child("vote").child("place_where1").setValue(vote_dto.place_where1);
        //database.getReference().child("chat_room").child(chat_name).child("vote").child("place_spwhere1").setValue(vote_dto.place_spwhere1);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_day1").setValue(vote_dto.place_day1);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_hour1").setValue(vote_dto.place_hour1);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_min1").setValue(vote_dto.place_min1);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_day2").setValue(vote_dto.place_day2);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_hour2").setValue(vote_dto.place_hour2);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_min2").setValue(vote_dto.place_min2);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_day3").setValue(vote_dto.place_day3);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_hour3").setValue(vote_dto.place_hour3);
        databaseReference.child("chat_room").child(chat_name).child("vote").child("place_min3").setValue(vote_dto.place_min3);


    }catch (NullPointerException e){
        e.printStackTrace();
    }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




}
package com.example.togedog.togedog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class VoteActivity extends AppCompatActivity{


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);



        Button button = (Button)findViewById(R.id.button2_cancel_vote);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTV = (TextView)findViewById(R.id.txtView);
        mBtn = (Button)findViewById(R.id.btnPick);


        mTV2 = (TextView)findViewById(R.id.txtView2);
        mBtn2 = (Button)findViewById(R.id.btnPick2);

        mTV3 = (TextView)findViewById(R.id.txtView3);
        mBtn3 = (Button)findViewById(R.id.btnPick3);

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

                  editTextSpot = (EditText) findViewById(R.id.editText);
                  intent.putExtra("Spot", editTextSpot.getText().toString());


                  TextViewTextCalender = (TextView) findViewById(R.id.txtView);
                  intent.putExtra("Calender", TextViewTextCalender.getText().toString());


                  spinner1 = (Spinner) findViewById(R.id.vote_time1);
                  intent.putExtra("Time", spinner1.getSelectedItem().toString());

                  spinner2 = (Spinner) findViewById(R.id.vote_minute1);
                  intent.putExtra("Minute", spinner2.getSelectedItem().toString());


                  editTextSpot2 = (EditText) findViewById(R.id.editText2);
                  intent.putExtra("Spot2", editTextSpot2.getText().toString());


                  TextViewTextCalender2 = (TextView) findViewById(R.id.txtView2);
                  intent.putExtra("Calender2", TextViewTextCalender2.getText().toString());


                  spinner3 = (Spinner) findViewById(R.id.vote_time2);
                  intent.putExtra("Time2", spinner3.getSelectedItem().toString());

                  spinner4 = (Spinner) findViewById(R.id.vote_minute2);
                  intent.putExtra("Minute2", spinner4.getSelectedItem().toString());


                  EditText editTextSpot3 = (EditText) findViewById(R.id.editText3);
                  intent.putExtra("Spot3", editTextSpot3.getText().toString());


                  TextViewTextCalender3 = (TextView) findViewById(R.id.txtView3);
                  intent.putExtra("Calender3", TextViewTextCalender3.getText().toString());


                  Spinner spinner5 = (Spinner) findViewById(R.id.vote_time3);
                  intent.putExtra("Time3", spinner5.getSelectedItem().toString());

                  Spinner spinner6 = (Spinner) findViewById(R.id.vote_minute3);
                  intent.putExtra("Minute3", spinner6.getSelectedItem().toString());


                  startActivity(intent);
                  finish();
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
}

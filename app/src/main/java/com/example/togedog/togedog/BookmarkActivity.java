package com.example.togedog.togedog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class  BookmarkActivity extends AppCompatActivity {

    Button home_btn;
    TextView gid_view;
    TextView nick_view;
    TextView dogname_view;
    TextView dogtype_view;
    TextView dogbirth_view;
    ImageView photo_view;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        //파이어베이스 정의
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dref = FirebaseDatabase.getInstance().getReference();

        //버튼, 뷰 정의
        home_btn = (Button) findViewById(R.id.Home_bt);
        gid_view = (TextView) findViewById(R.id.id_view);
        nick_view = (TextView) findViewById(R.id.nick_view);
        dogname_view = (TextView) findViewById(R.id.dogname_view);
        dogtype_view = (TextView) findViewById(R.id.dogtype_view);
        dogbirth_view = (TextView) findViewById(R.id.dogbirth_view);
        photo_view = (ImageView) findViewById(R.id.Photo_View);

        //이미지 경로


        //홈 액티비티로 돌아가기
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        //파이어베이스에서 데이터 폴더 가져오기
        DatabaseReference userid = dref.child("User").child(auth.getUid()).child("GoogleId");
        DatabaseReference nickname = dref.child("User").child(auth.getUid()).child("NickName");
        DatabaseReference dogname = dref.child("User").child(auth.getUid()).child("DogName");
        DatabaseReference dogtype = dref.child("User").child(auth.getUid()).child("DogType");
        DatabaseReference dogbirth = dref.child("User").child(auth.getUid()).child("Birth");
        final DatabaseReference image = dref.child("User").child(auth.getUid()).child("ProfileImage");

        //이미지 가져오기
        StorageReference storageReference = storage.getReferenceFromUrl("gs://togedog-3795c.appspot.com");
        final StorageReference pathRef = storageReference.child("Profiles").child(auth.getUid());
        pathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(BookmarkActivity.this).load(uri).into(photo_view);
            }
        });

        //구글아이디 가져오기
        userid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String googleid = dataSnapshot.getValue(String.class);
                gid_view.setText(googleid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //닉네임 가져오기
        nickname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nick = dataSnapshot.getValue(String.class);
                nick_view.setText(nick);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //개 이름 가져오기
        dogname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dogname = dataSnapshot.getValue(String.class);
                dogname_view.setText(dogname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //견종 가져오기
        dogtype.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dogtype = dataSnapshot.getValue(String.class);
                dogtype_view.setText(dogtype);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //생년월일 가져오기
        dogbirth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String birth = dataSnapshot.getValue(String.class);
                dogbirth_view.setText(birth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}

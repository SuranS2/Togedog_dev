package com.example.togedog.togedog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.support.annotation.NonNull;
import android.text.Html;
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;


public class CreateActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    int count = 0;
    EditText editTextWarning;
    Button limitsix_bt, limityear_bt, limitso_bt, limitjung_bt, limitdae_bt, unlimit_bt, monday_bt, tuesday_bt, wednesday_bt, thursday_bt, friday_bt, saturday_bt, sunday_bt, create_button;
    Spinner shourspinner, fhourspinner, sminutespinner, fminutespinner;

    //    int lm[0]=0,lm[1]=0,lm[2]=0,lm[3]=0,lm[4]=0,lm[5]=0,dow[0]=0,dow[1]=0,dow[2]=0,dow[3]=0,dow[4]=0,dow[5]=0,dow[6]=0;
    int[] lm = {0, 0, 0, 0, 0, 0};
    int[] dow = {0, 0, 0, 0, 0, 0, 0};
    Uri mImageCaputreUri;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;
    private String ImagePath;
    private String absolutePath;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;


    ImageView iv_UserPhoto;


    //firebase 채팅방 생성
    private EditText chat_name;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //firebase 채팅방 생성
    private Intent room_create;
    private ChatInfoDTO chatinfo_dto;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    protected GoogleApiClient mGoogleApiClient;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private TextView mName;
    private TextView mAddress;
    private TextView mAttributions;
    private String chat_do;
    private String chat_gun;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        chatinfo_dto = new ChatInfoDTO();

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        TextView pickerButton = (TextView) findViewById(R.id.pickerButton);

        mName = (TextView) findViewById(R.id.textView_c1);
        mAddress = (TextView) findViewById(R.id.textView_c2);
        mAttributions = (TextView) findViewById(R.id.textView_c3);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(CreateActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        shourspinner = (Spinner) findViewById(R.id.Spinner_time1);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shourspinner.setAdapter(yearAdapter);


        fhourspinner = (Spinner) findViewById(R.id.Spinner_time2);
        ArrayAdapter yearAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);
        yearAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fhourspinner.setAdapter(yearAdapter2);

        limitsix_bt = (Button) findViewById(R.id.Limit_bt1);
        limityear_bt = (Button) findViewById(R.id.Limit_bt2);
        limitso_bt = (Button) findViewById(R.id.Limit_bt3);
        limitjung_bt = (Button) findViewById(R.id.Limit_bt4);
        limitdae_bt = (Button) findViewById(R.id.Limit_bt5);

        sminutespinner = (Spinner) findViewById(R.id.Spinner_minute1);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sminutespinner.setAdapter(monthAdapter);


        fminutespinner = (Spinner) findViewById(R.id.Spinner_minute2);
        ArrayAdapter monthAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);
        monthAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fminutespinner.setAdapter(monthAdapter2);


        room_create = new Intent(CreateActivity.this, HomeActivity.class);


        //권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        }
    }

    // 액티비티 내부에 작동할 함수 등록


    public void doTakeAlbumAction() { //앨범에서 이미지 가져오기
        //앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                ImagePath = getPath(data.getData());
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    iv_UserPhoto = (ImageView) findViewById(R.id.Create_img);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        iv_UserPhoto.setBackground(null);
                    }
                    iv_UserPhoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Log.d(TAG, String.valueOf(bitmap));


            }
            case PLACE_PICKER_REQUEST: {
                try {
                    final Place place = PlacePicker.getPlace(this, data);
                    final CharSequence addres = place.getName();
                    final CharSequence address = place.getAddress();
                    String attributions = (String) place.getAttributions();
                    if (attributions == null) {
                        attributions = "";
                    }
                    String addres_1 = addres.toString();
                    String address_1 = address.toString();

                    String[] array = address_1.split(" ");

                    String chat_do = array[1];
                    String chat_gun = array[2];
                    String chat_dong = array[3];

                    String chat_add = chat_dong + " " + addres_1;
                    chatinfo_dto.chat_do = chat_do;//도
                    chatinfo_dto.chat_gun = chat_gun;//군
                    chatinfo_dto.chat_add = chat_dong;//건물이름
                    mName.setText(chat_do);
                    mAddress.setText(chat_gun);
                    mAttributions.setText(Html.fromHtml(attributions));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void onClick(View view) {

        view.setSelected(!view.isSelected());
        switch (view.getId()) {
            // 버튼 이벤트
            case R.id.Create_img:
                // 참고 http://jeongchul.tistory.com/287
                // 앨범선택! (사진 선택해서 등록)
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                // 알림창 등록 및 띄우기
                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
                break;
            case R.id.Limit_bt1:
                if (view.isSelected()) lm[0] = 1;
                else lm[0] = 0;
                break;
            case R.id.Limit_bt2:
                if (view.isSelected()) lm[1] = 1;
                else lm[1] = 0;
                break;
            case R.id.Limit_bt3:
                if (view.isSelected()) lm[2] = 1;
                else lm[2] = 0;
                break;
            case R.id.Limit_bt4:
                if (view.isSelected()) lm[3] = 1;
                else lm[3] = 0;
                break;
            case R.id.Limit_bt5:
                if (view.isSelected()) lm[4] = 1;
                else lm[4] = 0;
                break;
            case R.id.Limit_bt6:
                if (view.isSelected()) {
                    limitsix_bt.setSelected(false);
                    limityear_bt.setSelected(false);
                    limitso_bt.setSelected(false);
                    limitjung_bt.setSelected(false);
                    limitdae_bt.setSelected(false);
                    for (int i = 0; i < lm.length - 1; i++) {
                        lm[i] = 0;
                    }
                    lm[5] = 1;
                } else lm[5] = 0;
                break;
            case R.id.Monday:
                if (view.isSelected()) dow[0] = 1;
                else dow[0] = 0;
                break;
            case R.id.Tuesday:
                if (view.isSelected()) dow[1] = 1;
                else dow[1] = 0;
                break;
            case R.id.Wednesday:
                if (view.isSelected()) dow[2] = 1;
                else dow[2] = 0;
                break;
            case R.id.Thursday:
                if (view.isSelected()) dow[3] = 1;
                else dow[3] = 0;
                break;
            case R.id.Friday:
                if (view.isSelected()) dow[4] = 1;
                else dow[4] = 0;
                break;
            case R.id.Saturday:
                if (view.isSelected()) dow[5] = 1;
                else dow[5] = 0;
                break;
            case R.id.Sunday:
                if (view.isSelected()) dow[6] = 1;
                else dow[6] = 0;
                break;
            case R.id.Create_bt:

                // 참고 http://youeye.tistory.com/43
                // 기본 이미지 뷰의 배경 화면인지 확인
                /*if( findViewById(iv_UserPhoto.getId()) == this.findViewById(R.id.Dog_Photo_in) ){
                    Toast.makeText(this , "사진을 등록해주세요!" , Toast.LENGTH_SHORT).show();
                    break;
                }

                Drawable d = iv_UserPhoto.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) d ).getBitmap();
                // 참고 http://installed.tistory.com/entry/20-%EB%8B%A4%EB%A5%B8-Activity-%EB%A1%9C-text%EC%99%80-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%84%98%EA%B8%B0%EA%B8%B0
                intent.putExtra("Dog_img" , bitmap);
                */

                //채팅방 이름
                chat_name = (EditText) findViewById(R.id.Room_name);

                if (chat_name.getText().toString().equals("")) {
                    Toast.makeText(this, "방 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                editTextWarning = (EditText) findViewById(R.id.Warning);
                String editWarning_str = "";
                editWarning_str = editTextWarning.getText().toString();
                if (chat_name.getText().toString().equals("")) {
                    Toast.makeText(this, "주의사항을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                chatinfo_dto.sta_hour = shourspinner.getSelectedItem().toString();
                chatinfo_dto.sta_min = sminutespinner.getSelectedItem().toString();

                chatinfo_dto.fin_hour = fhourspinner.getSelectedItem().toString();
                chatinfo_dto.fin_min = fhourspinner.getSelectedItem().toString();

//                chatinfo_dto.dow = dow;
                chatinfo_dto.chat_mon = dow[0];
                chatinfo_dto.chat_tues = dow[1];
                chatinfo_dto.chat_wed = dow[2];
                chatinfo_dto.chat_thur = dow[3];
                chatinfo_dto.chat_fri = dow[4];
                chatinfo_dto.chat_sat = dow[5];
                chatinfo_dto.chat_sun = dow[6];
//                chatinfo_dto.lm = lm;
                if (lm[5] == 1) {
                    for (int i = 0; i < lm.length - 1; i++) {
                        lm[i] = 0;
                    }
                }
                chatinfo_dto.chat_limitsixmon = lm[0];
                chatinfo_dto.chat_limityear = lm[1];
                chatinfo_dto.chat_limitso = lm[2];
                chatinfo_dto.chat_limitjung = lm[3];
                chatinfo_dto.chat_limitdae = lm[4];
                chatinfo_dto.chat_unlimit = lm[5];
                chatinfo_dto.chat_warning = editWarning_str;


                //firebase 채팅방 리스트 업데이트
                // 방 이름 전송
                updateChatList(chat_name.getText().toString());
                upload(ImagePath);
                startActivity(room_create);

                break;

        }
    }


    private void updateChatList(String chat_name) {
        auth = FirebaseAuth.getInstance();
//        ChatDTO chat = new ChatDTO("☺", auth.getCurrentUser().getDisplayName() + "님이 채팅방을 생성했습니다.");
//        conditionRef.child(chat_name.toString()).setValue(ChatInfoDTO);

        chatinfo_dto.room_name = chat_name;
// 데이터 푸쉬
        // databaseReference.child("chat_room").child(chat_name).child("info").setValue(chatinfo_dto);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("message").push();
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("RoomName").setValue(chatinfo_dto.room_name);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Monday").setValue(chatinfo_dto.chat_mon);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Tuesday").setValue(chatinfo_dto.chat_tues);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Wednesday").setValue(chatinfo_dto.chat_wed);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Thursday").setValue(chatinfo_dto.chat_thur);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Friday").setValue(chatinfo_dto.chat_fri);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Saturday").setValue(chatinfo_dto.chat_sat);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Sunday").setValue(chatinfo_dto.chat_sun);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("LimitSixmonth").setValue(chatinfo_dto.chat_limitsixmon);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("LimitYear").setValue(chatinfo_dto.chat_limityear);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("LimitSo").setValue(chatinfo_dto.chat_limitso);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("LimitJung").setValue(chatinfo_dto.chat_limitjung);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("LimitDae").setValue(chatinfo_dto.chat_limitdae);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("LimitSixmonth").setValue(chatinfo_dto.chat_limitsixmon);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("UnLimit").setValue(chatinfo_dto.chat_unlimit);
        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Warning").setValue(chatinfo_dto.chat_warning);
//        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Do").setValue(chatinfo_dto.chat_do);
//        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("SiGunGu").setValue(chatinfo_dto.chat_gun);
//        databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("Address").setValue(chatinfo_dto.chat_add);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //도랑 시 db에 넣는 거 도전
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);
    }

    private void upload(String uri) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://togedog-3795c.appspot.com");

        Uri file = Uri.fromFile(new File(uri));
        final StorageReference RoomIRef = storageRef.child("RoomImage/").child(chatinfo_dto.room_name);
        UploadTask uploadTask = RoomIRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return RoomIRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    chatinfo_dto.imageUrl_chat = downloadUri.toString();
                    databaseReference.child("chat_room").child(chatinfo_dto.room_name).child("info").child("ChatImageUrl").setValue(chatinfo_dto.imageUrl_chat);


                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
}

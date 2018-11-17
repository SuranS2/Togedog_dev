package com.example.togedog.togedog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;


public class CreateActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    EditText editText2,editText3;
    Button create_button,button, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12,button19;
    Spinner yearSpinner, yearSpinner2,monthSpinner,monthSpinner2;
    Intent intent;
    String title3,title4,hour1,hour2,minute1,minute2;
    int ch1=0,ch2=0,ch3=0,ch4=0,ch5=0,ch6=0,ch7=0,ch8=0,ch9=0,ch10=0,ch11=0,ch12=0,ch13=0;
    Uri mImageCaputreUri;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;
    private String absolutePath;
    private String doo_1;
    private String si_1;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    ImageView iv_UserPhoto;

    int count=0;





    //firebase 채팅방 생성
    private EditText chat_name;
    private ListView chat_list;
    private ListView chat_view;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //firebase 채팅방 생성
    private Intent room_create;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    protected GoogleApiClient mGoogleApiClient;


    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private TextView mName;
    private TextView mAddress;
    private TextView mAttributions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


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



        chat_list = (ListView) findViewById(R.id.chat_list);
        chat_view = (ListView) findViewById(R.id.chat_view);

        Intent intent = getIntent();

        yearSpinner = (Spinner)findViewById(R.id.Spinner_time1);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);


        yearSpinner2 = (Spinner)findViewById(R.id.Spinner_time2);
        ArrayAdapter yearAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);
        yearAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner2.setAdapter(yearAdapter2);




        monthSpinner = (Spinner)findViewById(R.id.Spinner_minute1);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);



        monthSpinner2 = (Spinner)findViewById(R.id.Spinner_minute2);
        ArrayAdapter monthAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);
        monthAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner2.setAdapter(monthAdapter2);



        iv_UserPhoto = (ImageView)findViewById(R.id.Create_img);
        create_button = (Button)findViewById(R.id.Create_bt);
        button = (Button)findViewById(R.id.Limit_bt1);
        button2 = (Button)findViewById(R.id.Limit_bt2);
        button3 = (Button)findViewById(R.id.Limit_bt3);
        button4 = (Button)findViewById(R.id.Limit_bt4);
        button5 = (Button)findViewById(R.id.Limit_bt5);
        button19 = (Button)findViewById(R.id.Limit_bt6);
        button6 = (Button)findViewById(R.id.Monday);
        button7 = (Button)findViewById(R.id.Tuesday);
        button8 = (Button)findViewById(R.id.Wednesday);
        button9 = (Button)findViewById(R.id.Thursday);
        button10 = (Button)findViewById(R.id.Friday);
        button11 = (Button)findViewById(R.id.Saturday);
        button12 = (Button)findViewById(R.id.Sunday);



        room_create = new Intent(CreateActivity.this, HomeActivity.class);


    }

    // 액티비티 내부에 작동할 함수 등록

    public void doTakePhotoAction()
    { //카메라 촬영 후 이미지 가져오기
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        //외부저장소 이용
        mImageCaputreUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url ));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT , mImageCaputreUri );
        startActivityForResult(intent, PICK_FROM_CAMERA );
    }
    public void doTakeAlbumAction(){ //앨범에서 이미지 가져오기
        //앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM );
    }
    public void onActivityResult(int requestCode , int resultCode , Intent data){
        super.onActivityResult(requestCode , resultCode , data);
        if(resultCode != RESULT_OK) {
            return;
        }
        if ((requestCode == PLACE_PICKER_REQUEST)
                && (resultCode == Activity.RESULT_OK)) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }
            String address_1=address.toString();

            String[] array = address_1.split(" ");

            doo_1 = array[1];
            si_1 = array[2];

            mName.setText(doo_1);
            mAddress.setText(si_1);
            mAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        switch(requestCode){
            case PICK_FROM_ALBUM:{
                //이후의 처리가 카메라와 같으므로 일단 Break 없이 진행
                //실제 코드에서는 좀 더 합리적인 방법을 선택하시길 바랍니다.
                mImageCaputreUri = data.getData();
                Log.d("SmartWheel" , mImageCaputreUri.getPath().toString() );
            }
            case PICK_FROM_CAMERA:{
                //이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정
                //이후에 이미지 크롭 어플리케이션 호출
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaputreUri, "image/*");

                //CROP할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX" ,200 ); //CROP 이미지 x축 크기
                intent.putExtra("outputY" ,200 ); //CROP 이미지 y축 크기
                intent.putExtra("aspectX" ,1 ); //CROP 이미지 x축 비율
                intent.putExtra("aspectY" ,1 ); //CROP 이미지 y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data" , true);
                startActivityForResult(intent , CROP_FROM_iMAGE);//CROP_FROM_iMAGE로 case문 이동
                break;

            }
            case CROP_FROM_iMAGE: {
                //크롭이 된 이후의 이미지를 넘겨받는다
                //이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                //임시 파일 삭제
                if (resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();
                //CROP된 이미지를 저장하기 위한 FILE 경로
                //외부저장소 이용
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");//CROP된 BITMAP
                    iv_UserPhoto.setImageBitmap(photo);//레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌

                    storeCropImage(photo, filePath); //CROP된 이미지를 외부저장소 , 앨범에 저장한다.
                    absolutePath = filePath;
                    break;
                }
                //임시 파일 삭제
                File f = new File(mImageCaputreUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }

    }
    //Bitmap 저장하는 부분
    private void storeCropImage(Bitmap bitmap , String filePath){
        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식
        // 외부저장소 절대경로
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";

        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists()){
            //SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우 )
            directory_SmartWheel.mkdir();
        }
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , out );
            //sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE , Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onClick(View view) {

        view.setSelected(!view.isSelected());
        switch (view.getId()) {
            // 버튼 이벤트
            case R.id.Create_img :
                // 참고 http://jeongchul.tistory.com/287
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };

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
                        .setPositiveButton("사진촬영",cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소",cancelListener)
                        .show();
                break;
            case R.id.Limit_bt1:
                if(view.isSelected()) ch1=1;
                else ch1=0;
                break;
            case R.id.Limit_bt2:
                if(view.isSelected()) ch2=1;
                else ch2=0;
                break;
            case R.id.Limit_bt3:
                if(view.isSelected()) ch3=1;
                else ch3=0;
                break;
            case R.id.Limit_bt4:
                if(view.isSelected()) ch4=1;
                else ch4=0;
                break;
            case R.id.Limit_bt5:
                if(view.isSelected()) ch5=1;
                else ch5=0;
                break;
            case R.id.Limit_bt6:
                if(view.isSelected()) ch6=1;
                else ch6=0;
                break;

            case R.id.Monday:
                if(view.isSelected()) ch7=1;
                else ch7=0;
                break;
            case R.id.Tuesday:
                if(view.isSelected()) ch8=1;
                else ch8=0;
                break;
            case R.id.Wednesday:
                if(view.isSelected()) ch9=1;
                else ch9=0;
                break;
            case R.id.Thursday:
                if(view.isSelected()) ch10=1;
                else ch10=0;
                break;
            case R.id.Friday:
                if(view.isSelected()) ch11=1;
                else ch11=0;
                break;
            case R.id.Saturday:
                if(view.isSelected()) ch12=1;
                else ch12=0;
                break;
            case R.id.Sunday:
                if(view.isSelected()) ch13=1;
                else ch13=0;
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
                

//                if(ch1==1) intent.putExtra("six_month", "six_month");
//                if(ch2==1) intent.putExtra("year", "one_year");
//                if(ch3==1) intent.putExtra("small", "small");
//                if(ch4==1) intent.putExtra("medium", "medium");
//                if(ch5==1) intent.putExtra("big", "big");
//                if(ch6==1) intent.putExtra("unlimit", "unlimt");
//
//                if(ch7==1) intent.putExtra("mon", "mon");
//                if(ch8==1) intent.putExtra("tue", "tue");
//                if(ch9==1) intent.putExtra("wed", "wed");
//                if(ch10==1) intent.putExtra("thu", "thu");
//                if(ch11==1) intent.putExtra("fri", "fri");
//                if(ch12==1) intent.putExtra("sat","sat");
//                if(ch13==1)intent.putExtra("sun", "sun");

                //채팅방 이름
                chat_name = (EditText) findViewById(R.id.Room_name);

                // 방 이름 전송
                if(chat_name.getText().toString().equals("")){
                    Toast.makeText(this,"방 제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }



                editText3 = (EditText) findViewById(R.id.Warning);
                String edit3 = "";
                edit3 = editText3.getText().toString();
                if(edit3.length()==0){
                    Toast.makeText(this,"주의사항을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
//                    intent.putExtra("입력한제목3", edit3);
                }


//                hour1=yearSpinner.getSelectedItem().toString();
//                minute1=monthSpinner.getSelectedItem().toString();
//                intent.putExtra("hour1", hour1);
//                intent.putExtra("minute1", minute1);
//
//                hour2=yearSpinner2.getSelectedItem().toString();
//                minute2=monthSpinner2.getSelectedItem().toString();
//                intent.putExtra("hour2", hour2);
//                intent.putExtra("minute2", minute2);
//                intent.putExtra("count",Integer.toString(count));

                //firebase 채팅방 리스트 업데이트
                updateChatList(chat_name.getText().toString());
                startActivity(room_create);

                break;
        }
    }




    private void updateChatList(String chat_name) {
        auth = FirebaseAuth.getInstance();
        ChatDTO chat = new ChatDTO("☺", auth.getCurrentUser().getDisplayName() + "님이 채팅방을 생성했습니다.");
        databaseReference.child("chat").child(chat_name).push().setValue(chat); // 데이터 푸쉬
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //도랑 시 db에 넣는 거 도전

    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj,null, null, null);

        Cursor cursor = cursorLoader.loadInBackground ();
        int index = cursor.getColumnIndexOrThrow (MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(index);
    }

    private void upload(String uri){
        StorageReference storageRef = storage.getReferenceFromUrl("gs://togedog-3795c.appspot.com");

        Uri file = Uri.fromFile(new File(uri));
        final StorageReference riversRef = storageRef.child("Profiles/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    MapDTO mapDTO = new MapDTO();

                    mapDTO.doo = doo_1;
                    mapDTO.ssi = si_1;
                    database.getReference().child("MapTest").child(doo_1).setValue(mapDTO);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}

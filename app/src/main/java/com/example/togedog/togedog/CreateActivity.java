package com.example.togedog.togedog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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


public class CreateActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    int count=0;
    EditText editTextWarning;
//    Button limitsix_bt, limityear_bt, limitso_bt, limitjung_bt, limitdae_bt, unlimit_bt,monday_bt, Tuesday_bt, Wednesday_bt, Thursday_bt, button10, button11, button12,create_button;
    Spinner shourspinner, fhourspinner,sminutespinner,fminutespinner;
   
//    int lm1=0,lm2=0,lm3=0,lm4=0,lm5=0,lm6=0,dow[0]=0,dow[1]=0,dow[2]=0,dow[3]=0,dow[4]=0,dow[5]=0,dow[6]=0;
    int[] lm = {0, 0, 0, 0, 0, 0};
    int[] dow = {0, 0, 0, 0, 0, 0, 0};
    Uri mImageCaputreUri;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;
    private String ImagePath;
    private String absolutePath;
    
    
    
    
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
    
    private FirebaseAuth auth;


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

        shourspinner = (Spinner)findViewById(R.id.Spinner_time1);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shourspinner.setAdapter(yearAdapter);


        fhourspinner = (Spinner)findViewById(R.id.Spinner_time2);
        ArrayAdapter yearAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_time, android.R.layout.simple_spinner_item);
        yearAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fhourspinner.setAdapter(yearAdapter2);




        sminutespinner = (Spinner)findViewById(R.id.Spinner_minute1);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sminutespinner.setAdapter(monthAdapter);



        fminutespinner = (Spinner)findViewById(R.id.Spinner_minute2);
        ArrayAdapter monthAdapter2 = ArrayAdapter.createFromResource(this, R.array.date_minute, android.R.layout.simple_spinner_item);
        monthAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fminutespinner.setAdapter(monthAdapter2);


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
        switch(requestCode){
            case PICK_FROM_ALBUM:{
                //이후의 처리가 카메라와 같으므로 일단 Break 없이 진행
                //실제 코드에서는 좀 더 합리적인 방법을 선택하시길 바랍니다.
                mImageCaputreUri = data.getData();
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
            case PLACE_PICKER_REQUEST:{
                try {
                    final Place place = PlacePicker.getPlace(this, data);
                    final CharSequence name = place.getName();
                    final CharSequence address = place.getAddress();
                    String attributions = (String) place.getAttributions();
                    if (attributions == null) {
                        attributions = "";
                    }
                    String address_1 = address.toString();

                    String[] array = address_1.split(" ");

                    String chat_do = array[1];
                    String chat_gun = array[2];

                    chatinfo_dto.chat_do = chat_do;
                    chatinfo_dto.chat_gun = chat_gun;

                    mName.setText(chat_do);
                    mAddress.setText(chat_gun);
                    mAttributions.setText(Html.fromHtml(attributions));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            }
            default:
                super.onActivityResult(requestCode , resultCode , data);
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
                if(view.isSelected()) lm[0]=1;
//                else lm[0]=0;
                break;
            case R.id.Limit_bt2:
                if(view.isSelected()) lm[1]=1;
//                else lm[1]=0;
                break;
            case R.id.Limit_bt3:
                if(view.isSelected()) lm[2]=1;
//                else lm[2]=0;
                break;
            case R.id.Limit_bt4:
                if(view.isSelected()) lm[3]=1;
//                else lm[3]=0;
                break;
            case R.id.Limit_bt5:
                if(view.isSelected()) lm[4]=1;
//                else lm[4]=0;
                break;
            case R.id.Limit_bt6:
                if(view.isSelected()) lm[5]=1;
//                else lm[5]=0;
                break;
            case R.id.Monday:
                if(view.isSelected()) dow[0]=1;
//                else dow[0]=0;
                break;
            case R.id.Tuesday:
                if(view.isSelected()) dow[1]=1;
//                else dow[1]=0;
                break;
            case R.id.Wednesday:
                if(view.isSelected()) dow[2]=1;
//                else dow[2]=0;
                break;
            case R.id.Thursday:
                if(view.isSelected()) dow[3]=1;
//                else dow[3]=0;
                break;
            case R.id.Friday:
                if(view.isSelected()) dow[4]=1;
//                else dow[4]=0;
                break;
            case R.id.Saturday:
                if(view.isSelected()) dow[5]=1;
//                else dow[5]=0;
                break;
            case R.id.Sunday:
                if(view.isSelected()) dow[6]=1;
//                else dow[6]=0;
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

                if(chat_name.getText().toString().equals("")){
                    Toast.makeText(this,"방 제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }
                editTextWarning = (EditText) findViewById(R.id.Warning);
                String editWarning_str = "";
                editWarning_str = editTextWarning.getText().toString();
                if(chat_name.getText().toString().equals("")){
                    Toast.makeText(this,"주의사항을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }
                
                chatinfo_dto.sta_hour = shourspinner.getSelectedItem().toString();
                chatinfo_dto.sta_min = sminutespinner.getSelectedItem().toString();
                
                chatinfo_dto.fin_hour = fhourspinner.getSelectedItem().toString();
                chatinfo_dto.fin_min = fhourspinner.getSelectedItem().toString();
                
                chatinfo_dto.dow = dow;
//                chatinfo_dto.chat_tues = dow[1];
//                chatinfo_dto.chat_wed = dow[2];
//                chatinfo_dto.chat_thur = dow[3];
//                chatinfo_dto.chat_fri = dow[4];
//                chatinfo_dto.chat_sat = dow[5];
//                chatinfo_dto.chat_sun = dow[6];
                chatinfo_dto.lm = lm;
//                chatinfo_dto.chat_limitsixmon = lm1;
//                chatinfo_dto.chat_limityear = lm2;
//                chatinfo_dto.chat_limitso = lm3;
//                chatinfo_dto.chat_limitjung = lm4;
//                chatinfo_dto.chat_limitdae = lm5;
//                chatinfo_dto.chat_unlimit = lm6;
                chatinfo_dto.chat_warning = editWarning_str;
                
                //firebase 채팅방 리스트 업데이트
                // 방 이름 전송
                updateChatList(chat_name.getText().toString());
                startActivity(room_create);

                break;
                
        }
    }




    private void updateChatList(String chat_name) {
        auth = FirebaseAuth.getInstance();
//        ChatDTO chat = new ChatDTO("☺", auth.getCurrentUser().getDisplayName() + "님이 채팅방을 생성했습니다.");
//        conditionRef.child(chat_name.toString()).setValue(ChatInfoDTO);
        databaseReference.child("chat_room").child(chat_name).child("info").setValue(chatinfo_dto); // 데이터 푸쉬
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //도랑 시 db에 넣는 거 도전


}

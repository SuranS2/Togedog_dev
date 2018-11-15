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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class CreateActivity extends AppCompatActivity{
    EditText editText,editText2,editText3;
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


    ImageView iv_UserPhoto;

    int count=0;





    //firebase 채팅방 생성
    private EditText user_chat;
    private ListView chat_list;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //firebase 채팅방 생성

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        chat_list = (ListView) findViewById(R.id.chat_list);

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



        intent = new Intent(CreateActivity.this, HomeActivity.class);


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
            case CROP_FROM_iMAGE:{
                //크롭이 된 이후의 이미지를 넘겨받는다
                //이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                //임시 파일 삭제
                if(resultCode != RESULT_OK){
                    return;
                }
                final Bundle extras = data.getExtras();
                //CROP된 이미지를 저장하기 위한 FILE 경로
                //외부저장소 이용
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");//CROP된 BITMAP
                    iv_UserPhoto.setImageBitmap(photo);//레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌

                    storeCropImage(photo , filePath); //CROP된 이미지를 외부저장소 , 앨범에 저장한다.
                    absolutePath = filePath;
                    break;
                }
                //임시 파일 삭제
                File f = new File(mImageCaputreUri.getPath());
                if(f.exists()){
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
                

                if(ch1==1) intent.putExtra("six_month", "six_month");
                if(ch2==1) intent.putExtra("year", "one_year");
                if(ch3==1) intent.putExtra("small", "small");
                if(ch4==1) intent.putExtra("medium", "medium");
                if(ch5==1) intent.putExtra("big", "big");
                if(ch6==1) intent.putExtra("unlimit", "unlimt");

                if(ch7==1) intent.putExtra("mon", "mon");
                if(ch8==1) intent.putExtra("tue", "tue");
                if(ch9==1) intent.putExtra("wed", "wed");
                if(ch10==1) intent.putExtra("thu", "thu");
                if(ch11==1) intent.putExtra("fri", "fri");
                if(ch12==1) intent.putExtra("sat","sat");
                if(ch13==1)intent.putExtra("sun", "sun");

                //채팅방 이름
                user_chat = (EditText) findViewById(R.id.Room_name);
                String edit1 = "";
                edit1 = editText.getText().toString();

                // 방 이름 전송
                if(user_chat.getText().toString().equals("") && edit1.length()==0){
                    Toast.makeText(this,"방 제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    intent.putExtra("chat_name_test", user_chat.getText().toString());
                }

                editText2 = (EditText) findViewById(R.id.Place_Search);
                String edit2 = "";
                edit2 = editText2.getText().toString();
                if(edit2.length()==0){
                    Toast.makeText(this,"주 모임 장소를\n 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    intent.putExtra("입력한제목2", edit2);
                }

                editText3 = (EditText) findViewById(R.id.Warning);
                String edit3 = "";
                edit3 = editText3.getText().toString();
                if(edit3.length()==0){
                    Toast.makeText(this,"주의사항을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    intent.putExtra("입력한제목3", edit3);
                }


                hour1=yearSpinner.getSelectedItem().toString();
                minute1=monthSpinner.getSelectedItem().toString();
                intent.putExtra("hour1", hour1);
                intent.putExtra("minute1", minute1);

                hour2=yearSpinner2.getSelectedItem().toString();
                minute2=monthSpinner2.getSelectedItem().toString();
                intent.putExtra("hour2", hour2);
                intent.putExtra("minute2", minute2);
                intent.putExtra("count",Integer.toString(count));

                //firebase 채팅방 리스트 업데이트
//                updateChatList();
                startActivity(intent);

                break;
        }
    }

//    private ListView chat_list = getChatlist;
//    private void updateChatList() {
//        // 리스트 어댑터 생성 및 세팅
//        final ArrayAdapter<String> adapter
//
//                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//        chat_list.setAdapter(adapter);
//
//        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
//        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
//                adapter.add(dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }



}

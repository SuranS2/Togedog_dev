package com.example.togedog.togedog;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {



    private String ImagePath;
    private Uri mImageCaputreUri;
    private ImageView iv_UserPhoto;
    private String absolutePath;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;
    private static final int MEMBER_JOIN = 3;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    Calendar c;
    DatePickerDialog dpd;
    TextView birth;
    Button btn_birth;
    int reqWidth;
    int reqHeight;
    File filePath;
    String dirPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/myApp";
    File dir=new File(dirPath);
    Uri photoURI;
    EditText nick;
    EditText dog_name;
    EditText dog_type;
    EditText dog_kg;
    EditText dog_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        //권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        }


        iv_UserPhoto = (ImageView)findViewById(R.id.Dog_Photo_in);

        birth=(TextView)findViewById(R.id.Dog_birth);
        btn_birth=(Button)findViewById(R.id.btn_birth);
        nick = (EditText)findViewById(R.id.Nick_in);
        dog_name = (EditText)findViewById(R.id.Dog_Name_in);
        dog_type = (EditText)findViewById(R.id.Dog_Type_in);
        dog_kg = (EditText) findViewById(R.id.Dog_Kg_in);
        dog_num = (EditText) findViewById(R.id.Dog_Num_in);
        //사진 뷰에 나타낼때 넓이이
        reqWidth = getResources().getDimensionPixelSize(R.dimen.request_image_width);
        reqHeight = getResources().getDimensionPixelSize(R.dimen.request_image_height);
    }

    // id 설정 출처 http://pblab.tistory.com/20
    public String Radiogroup_Selected(int id){
        RadioGroup rg = (RadioGroup)findViewById(id);// 라디오그룹 객체 맵핑
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            // 출처 http://blog.naver.com/PostView.nhn?blogId=idinho2&logNo=30127252228
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radio_btn = (RadioButton) findViewById(checkedId);
            }
        });
        // rg 라디오그룹의 체크된(getCheckedRadioButtonId) 라디오버튼 객체 맵핑 , 널값이면 오류로 종료
        RadioButton selectedRdo = (RadioButton)findViewById(rg.getCheckedRadioButtonId());
        String selectedValue = selectedRdo.getText().toString(); // 해당 라디오버튼 객체의 값 가져오기
        return selectedValue;

    }


    public void onClick(View view) {
        //버튼의 id를 가져온다
        switch (view.getId()){
            //각 버튼을 눌렀을 때 처리
            case R.id.Overlap :
                Toast.makeText(this , "데이터베이스가 없습니다", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Dog_Photo_in :
                // 참고 http://jeongchul.tistory.com/287
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };
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

                new AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영",cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소",cancelListener)
                        .show();
                break;
            case R.id.btn_birth:

                c= Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd =new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {

                        birth.setText(mYear + "년" + (mMonth+1) + "월" + mDay +"일");
                    }
                }, day,month,year);
                dpd.show();
                break;
            case R.id.Boy_in :
                break;
            case R.id.Girl_in :
                break;
            case R.id.Neut_yesin :
                break;
            case R.id.Neut_noin :
                break;
            case R.id.So_in :
                break;
            case R.id.Joong_in :
                break;
            case R.id.Dae_in :
                break;
            case R.id.Join : {
                upload(ImagePath);
                break;
            }
        }
    }
    public void doTakePhotoAction(){ //카메라 촬영 후 이미지 가져오기

        if(ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            try{
                if(!dir.exists())
                    dir.mkdir();
                filePath=File.createTempFile("IMG",".jpg", dir);
                if(!filePath.exists())
                    filePath.createNewFile();

                //photoURI= FileProvider.getUriForFile(SignupActivity.this, BuildConfig.APPLICATION_ID+".provider"+"", filePath);
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }


    }
    public void doTakeAlbumAction(){ //앨범에서 이미지 가져오기
        //앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM );

    }
    public void onActivityResult(int requestCode , int resultCode , Intent data){
        super.onActivityResult(requestCode , resultCode , data);
        if(resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode){
            case PICK_FROM_ALBUM:{

                ImagePath = getPath(data.getData());
                File f = new File(ImagePath);
                iv_UserPhoto.setImageURI(Uri.fromFile(f));

            }
            case PICK_FROM_CAMERA:{

                if(filePath != null){

                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inJustDecodeBounds=true;
                    try{
                        InputStream in=new FileInputStream(filePath);
                        BitmapFactory.decodeStream(in, null, options);
                        in.close();
                        in=null;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    final int height=options.outHeight;
                    final int width=options.outWidth;
                    int inSampleSize=1;
                    if(height>reqHeight || width>reqWidth){
                        final int heightRatio=Math.round((float)height/(float)reqHeight);
                        final int widthtRatio=Math.round((float)width/(float)reqWidth);

                        inSampleSize=heightRatio<widthtRatio ? heightRatio : widthtRatio;
                    }

                    BitmapFactory.Options imgOptions=new BitmapFactory.Options();
                    imgOptions.inSampleSize=inSampleSize;
                    Bitmap bitmap=BitmapFactory.decodeFile(filePath.getAbsolutePath(), imgOptions);
                    iv_UserPhoto.setImageBitmap(bitmap);
                }

            }

        }

    }

    //파일 업로드 하는 함수 두개
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
        final StorageReference riversRef = storageRef.child("Profiles/").child(auth.getUid());
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

                    ProfileDTO profileDTO = new ProfileDTO();
                    profileDTO.imageUrl = downloadUri.toString();
                    profileDTO.Nick = nick.getText().toString();
                    profileDTO.Dogname= dog_name.getText().toString();
                    profileDTO.Birth = birth.getText().toString();
                    profileDTO.DogType = dog_type.getText().toString();
                    profileDTO.DogWeight = dog_kg.getText().toString();
                    profileDTO.DogNum = dog_num.getText().toString();

                    profileDTO.uid = auth.getCurrentUser().getUid();
                    profileDTO.userid = auth.getCurrentUser().getEmail();
                    database.getReference().child("User").child(auth.getUid()).child("GoogleId").setValue(profileDTO.userid);
                    database.getReference().child("User").child(auth.getUid()).child("NickName").setValue(profileDTO.Nick);
                    database.getReference().child("User").child(auth.getUid()).child("ProfileImage").setValue(profileDTO.imageUrl);
                    database.getReference().child("User").child(auth.getUid()).child("DogName").setValue(profileDTO.Dogname);
                    database.getReference().child("User").child(auth.getUid()).child("Birth").setValue(profileDTO.Birth);
                    database.getReference().child("User").child(auth.getUid()).child("DogType").setValue(profileDTO.DogType);
                    database.getReference().child("User").child(auth.getUid()).child("DogWeight").setValue(profileDTO.DogWeight);
                    database.getReference().child("User").child(auth.getUid()).child("DogNum").setValue(profileDTO.DogNum);

                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);   // 보내는 클래스, 받는 클래스
                    startActivity(intent);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}
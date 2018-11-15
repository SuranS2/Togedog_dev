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
//        reqWidth = getResources().getDimensionPixelSize(R.dimen.request_image_width);
//        reqHeight = getResources().getDimensionPixelSize(R.dimen.request_image_height);
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
                Intent intent = new Intent(this, HomeActivity.class);   // 보내는 클래스, 받는 클래스
                startActivity(intent);

                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if (nick.getText().toString().length() == 0) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this, "반려견의 별칭을 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra("Dog_nick", nick.getText().toString());

                }

                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if (dog_name.getText().toString().length() == 0) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this, "반려견의 이름을 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra("Dog_name", dog_name.getText().toString());

                }


                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if (dog_name.getText().toString().length() == 0) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this, "반려견의 견종을 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra("Dog_type", dog_type.getText().toString());

                }


                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if (dog_kg.getText().toString().length() == 0) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this, "반려견의 몸무게를 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra("Dog_kg", Integer.parseInt(dog_kg.getText().toString()));

                }


                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if (dog_num.getText().toString().length() == 0) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this, "반려견의 등록번호를 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra("Dog_num", Integer.parseInt(dog_num.getText().toString()));

                }
                if (birth.getText().toString().length() == 0) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this, "반려견의 생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra("Dog_birth", birth.getText().toString());

                }

                String rg_gender_selected = Radiogroup_Selected(R.id.radioGroup_Gender);
                String rg_neut_selected = Radiogroup_Selected(R.id.radioGroup_Neut);
                String rg_size_selected = Radiogroup_Selected(R.id.radioGroup_Size);

                intent.putExtra("Gender", rg_gender_selected); // "Gender"란 키 값으로 문자열 Gender 선택값을 넘김
                intent.putExtra("Neut", rg_neut_selected); // "Gender"란 키 값으로 문자열 Neut 선택값을 넘김
                intent.putExtra("Size", rg_size_selected); // "Gender"란 키 값으로 문자열 Size 선택값을 넘김


                Drawable d = iv_UserPhoto.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                // 참고 http://installed.tistory.com/entry/20-%EB%8B%A4%EB%A5%B8-Activity-%EB%A1%9C-text%EC%99%80-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%84%98%EA%B8%B0%EA%B8%B0
                intent.putExtra("Dog_img", bitmap);
                // 가입 버튼 누를 시 홈 액티비티로 인텐트 보냄


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
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaputreUri);
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
                //이후의 처리가 카메라와 같으므로 일단 Break 없이 진행
                //실제 코드에서는 좀 더 합리적인 방법을 선택하시길 바랍니다.
                // mImageCaputreUri = data.getData();
                //Log.d("SmartWheel" , mImageCaputreUri.getPath().toString() );

                ImagePath = getPath(data.getData());
                File f = new File(ImagePath);
                iv_UserPhoto.setImageURI(Uri.fromFile(f));

                /*
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    iv_UserPhoto.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                */

/*
                //사진 파일 스토리지에 업로드

                */
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
            /*
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
                //내부저장소 이용
                //String filePath = getFilesDir().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";
                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");//CROP된 BITMAP
                    iv_UserPhoto.setImageBitmap(photo);//레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌

                    //storeCropImage(photo , filePath); //CROP된 이미지를 외부저장소 , 앨범에 저장한다.
                    absolutePath = filePath;
                    break;
                }
                //임시 파일 삭제
                File f = new File(mImageCaputreUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            }
*/
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
                    database.getReference().child("User").child(auth.getUid()).setValue(profileDTO);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
/*
    //Bitmap 저장하는 부분
    private void storeCropImage(Bitmap bitmap , String filePath){
        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식
        // 외부저장소 절대경로
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        // 내부저장소 절대경로
        //String dirPath = getFilesDir().getAbsolutePath() + "/SmartWheel";
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
    */
}
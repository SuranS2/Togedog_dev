package com.example.togedog.togedog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {


    private Uri mImageCaputreUri;
    private ImageView iv_UserPhoto;
    private String absolutePath;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    Calendar c;
    DatePickerDialog dpd;
    TextView birth;
    Button btn_birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        iv_UserPhoto = (ImageView) this.findViewById(R.id.Dog_Photo_in);

        birth=(TextView)findViewById(R.id.Dog_birth);
        btn_birth=(Button)findViewById(R.id.btn_birth);
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
            case R.id.Join :
                Intent intent = new Intent( this, HomeActivity.class );   // 보내는 클래스, 받는 클래스

                EditText nick = (EditText)findViewById(R.id.Nick_in);
                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if ( nick.getText().toString().length() == 0 ) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this , "반려견의 별칭을 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra( "Dog_nick" ,  nick.getText().toString() );

                }
                EditText dog_name = (EditText)findViewById(R.id.Dog_Name_in);
                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if ( dog_name.getText().toString().length() == 0 ) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this , "반려견의 이름을 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra( "Dog_name" ,  dog_name.getText().toString() );

                }

                EditText dog_type = (EditText)findViewById(R.id.Dog_Type_in);
                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if ( dog_name.getText().toString().length() == 0 ) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this , "반려견의 견종을 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra( "Dog_type" ,  dog_type.getText().toString() );

                }

                EditText dog_kg = (EditText)findViewById(R.id.Dog_Kg_in);
                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if ( dog_kg.getText().toString().length() == 0 ) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this , "반려견의 몸무게를 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra( "Dog_kg" ,  Integer.parseInt(dog_kg.getText().toString()) );

                }
//                if (!strNo.isEmpty() && strNo.matches("^[0-9]*$")) {
//                    // check numbers by RegEx.
//                    intent.putExtra("contact_no", Integer.parseInt(strNo));
//                } else {
//                    intent.putExtra("contact_no", 0) ;
//                }
//                출처: http://recipes4dev.tistory.com/83 [개발자를 위한 레시피]


                EditText dog_num = (EditText)findViewById(R.id.Dog_Num_in);
                // 인텐트 Nick_in 키에 문자열 데이터 적재
                if ( dog_num.getText().toString().length() == 0 ) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this , "반려견의 등록번호를 기입해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra( "Dog_num" ,  Integer.parseInt(dog_num.getText().toString() ) );

                }
                if ( birth.getText().toString().length() == 0 ) {
                    //공백일 때 처리할 내용
                    Toast.makeText(this , "반려견의 생년월일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    //공백이 아닐 때 처리할 내용
                    intent.putExtra( "Dog_birth" ,  birth.getText().toString() );

                }

                String rg_gender_selected = Radiogroup_Selected(R.id.radioGroup_Gender);
                String rg_neut_selected = Radiogroup_Selected(R.id.radioGroup_Neut);
                String rg_size_selected = Radiogroup_Selected(R.id.radioGroup_Size);

                intent.putExtra("Gender", rg_gender_selected); // "Gender"란 키 값으로 문자열 Gender 선택값을 넘김
                intent.putExtra("Neut", rg_neut_selected); // "Gender"란 키 값으로 문자열 Neut 선택값을 넘김
                intent.putExtra("Size", rg_size_selected); // "Gender"란 키 값으로 문자열 Size 선택값을 넘김


                Drawable d = iv_UserPhoto.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) d ).getBitmap();
                // 참고 http://installed.tistory.com/entry/20-%EB%8B%A4%EB%A5%B8-Activity-%EB%A1%9C-text%EC%99%80-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%84%98%EA%B8%B0%EA%B8%B0
                intent.putExtra("Dog_img" , bitmap);
                // 가입 버튼 누를 시 홈 액티비티로 인텐트 보냄
                startActivity(intent);
                break;
        }
    }
    public void doTakePhotoAction(){ //카메라 촬영 후 이미지 가져오기
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        //외부저장소 이용
        mImageCaputreUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url ));
        //내부저장소 이용
        //mImageCaputreUri = Uri.fromFile(new File(getFilesDir().getPath(), url ));

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
                //내부저장소 이용
                //String filePath = getFilesDir().getAbsolutePath() + "/SmartWheel/" + System.currentTimeMillis() + ".jpg";
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
}


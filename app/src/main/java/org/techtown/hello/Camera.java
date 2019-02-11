package org.techtown.hello;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity {

    private static final int MY_PERMISSION_STORAGE = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;

    Button Button_Camera;
    Button Button_Album;
    Button Button_Get_Image;
    ImageView imageView;
    ImageView imageView2;
    File file;
    String mCurrentPhotoPath;

    Uri imageUri;
    Uri photoURI, albumURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //String sdcard = Environment.getExternalStorageDirectory()+"/AP/";
        //File sdcard = Environment.getExternalStorageDirectory();

        checkPermission();

        Button_Camera = (Button) findViewById(R.id.camera);
        Button_Album = (Button) findViewById(R.id.album);
        Button_Get_Image = (Button) findViewById(R.id.get_image);
        imageView = (ImageView) findViewById(R.id.view);
        imageView2 = (ImageView) findViewById(R.id.imageView);

        Button_Camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.bignerdranch.android.test.fileprovider", file);
                imageUri = uri;
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i,REQUEST_TAKE_PHOTO);
                */
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e("captureCamera Error", ex.toString());
                    }
                    if (photoFile != null) {
                        // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함

                        Uri providerURI = FileProvider.getUriForFile(getApplicationContext(), "com.bignerdranch.test.fileprovider", photoFile);
                        imageUri = providerURI;

                        // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button_Album.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_TAKE_ALBUM);
            }
        });

        Button_Get_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imgFile2 = new File(Environment.getExternalStorageDirectory() + "/AP/album/JPEG_20190208_160909.jpg");
                File path = new File(Environment.getExternalStorageDirectory() + "/AP/album");

                String files[] = path.list();

                for( int i = 0; i<files.length; i++){
                    String k = String.valueOf(i);
                    //Toast.makeText(getApplicationContext(),files[i], Toast.LENGTH_SHORT).show();
                    //File k = new File(Environment.getExternalStorageDirectory() + "/AP/album/"+files[i]);

                }

                Toast.makeText(getApplicationContext(),imgFile2.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                if (imgFile2.exists()) {
                    Toast.makeText(getApplicationContext(),"파일존재", Toast.LENGTH_SHORT).show();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
                    imageView2.setImageBitmap(myBitmap);
                } else {
                    Toast.makeText(getApplicationContext(),"파일 음슴", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/AP", "album");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }



    //  사진을 찍고 사진을 가져와요, intent의 결과를 여기서 확인할 수 있음
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        switch(requestCode){
            case REQUEST_TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    galleryAddPic();
                    imageView.setImageURI(imageUri);
                    Toast.makeText(getApplicationContext(), "사진을 잘 찍었습니다.", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),"사진 찍기를 취소했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_TAKE_ALBUM:
                if(resultCode == RESULT_OK){
                        }

                    }
    }

    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, contentUri.getPath() + "에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }


        /*
        if(requestCode==REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            Toast.makeText(getApplicationContext(),"성공", Toast.LENGTH_SHORT).show();
            //imageView.setImageURI(data.getData());

            imageView.setImageURI(imageUri);
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //ImageView.setImageBitmap(imageBitmap);
        }else{
            Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT).show();
            return ;
        }
        */
        //data.getData()를 통해 방금 찍은 사진의 uri 가지고 일 수 있음



    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(getApplicationContext(), "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }

}

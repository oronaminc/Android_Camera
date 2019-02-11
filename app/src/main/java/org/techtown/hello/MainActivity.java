package org.techtown.hello;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_STORAGE = 1111;
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);

        PermissionListener permissionListener = new PermissionListener() {
            // 접근권한 허용되었을 때
            @Override
            public void onPermissionGranted() {
                RecyclerView recyclerView = findViewById(R.id.main_rv);
                final GalleryRecyclerViewAdapter adapter = new GalleryRecyclerViewAdapter(getApplicationContext(), new PhotoUtil().getAllPhotoPathList(getApplicationContext()));

                // recyclerview 그리드 형식의 3열로 셋팅
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                recyclerView.setAdapter(adapter);

                Button button = findViewById(R.id.main_btn);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selectedPhoto = "";
                        for (Listitem photo : adapter.getSelectedPhotos()) {
                            selectedPhoto += photo.getPath() + ", " + '\n';
                        }
                        Toast.makeText(getApplicationContext(), selectedPhoto + "를 보냈습니다.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            // 접근권한 허용되지 않았을 때
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();




    }

    public void gotoCamera(View v) {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
        //startActivityForResult(intent,1);
    }
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
                        Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }
}

    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                imageView.setImageResource(R.drawable.logo1);
            }
        }
        //data.getData()를 통해 방금 찍은 사진의 uri 가지고 일 수 있음

    }
    */

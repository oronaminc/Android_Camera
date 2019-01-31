package org.techtown.hello;

import android.content.Intent;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void gotoCamera(View v){
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
        //startActivityForResult(intent,1);
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
}
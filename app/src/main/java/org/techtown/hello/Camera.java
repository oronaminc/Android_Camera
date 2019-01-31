package org.techtown.hello;

import android.content.Intent;
import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

public class Camera extends AppCompatActivity implements View.OnClickListener {

    Button Button_Camera;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button_Camera = (Button) findViewById(R.id.camera);
        imageView = (ImageView) findViewById(R.id.view);
        Button_Camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,0);
    }

    //  사진을 찍고 사진을 가져와요, intent의 결과를 여기서 확인할 수 있음
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                imageView.setImageURI(data.getData());
            }
        }else{
            return ;
        }
        //data.getData()를 통해 방금 찍은 사진의 uri 가지고 일 수 있음

    }

}

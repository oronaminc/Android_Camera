package org.techtown.hello;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PhotoUtil {

    String strImage;

    public ArrayList<Listitem> getAllPhotoPathList(Context context) {
        ArrayList<Listitem> photos = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DATA,
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        // 사진 아이디
        int columnIndexId = cursor.getColumnIndex(MediaStore.Images.Media._ID);

        // 사진 경로
        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        //사진 데이터
        int nCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA); // bitmap



        while (cursor.moveToNext()) {

            strImage = cursor.getString(nCol);
            if( strImage.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath()+"/AP"))
            {
                Listitem Photo = new Listitem(cursor.getString(columnIndexId), cursor.getString(columnIndexData));
                photos.add(Photo);
            }
        }

        cursor.close();

        // 최근 순으로 정렬
        Collections.sort(photos, new DescendingId());

        return photos;
    }

    class DescendingId implements Comparator<Listitem> {
        @Override
        public int compare(Listitem Photo, Listitem t1) {
            return ((Integer)Integer.parseInt(t1.getId())).compareTo((Integer)Integer.parseInt(Photo.getId()));
        }
    }

}
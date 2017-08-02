package com.sunboxsoft.deeper.moblie.handwriting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import petrochina.ghzy.a10fieldwork.R;

/**
 * Created by caomang on 2016/11/11.
 */
public class TestImageshow extends Activity{
    private ImageView test_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageshow_layout);
        String urlForPictest = getIntent().getExtras().getString("urlForPictest");
        test_image=(ImageView)findViewById(R.id.test_image);
        Bitmap bitmap = BitmapFactory.decodeFile(urlForPictest);
        test_image.setImageBitmap(bitmap);



//        ImageLoader imageLoader=new ImageLoader(this);
//        imageLoader.displayImage2(urlForPictest,test_image,R.drawable.ic_alarm_processing);




    }
}

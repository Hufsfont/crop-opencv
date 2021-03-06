package com.example.font_opencv;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.font_opencv.utils.UiHelper;

public class activity_sub_02 extends AppCompatActivity {
/*
    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

 */

    ImageView imageView1, imageView2, imageView3;
    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();
/*
    private Mat img_input;

    private Mat img_output1;
    private Mat img_output2;
    private Mat img_output3;
    private Mat img_output4;
    private Mat img_output5;
    private Mat img_output6;
    private Mat img_output7;
    private Mat img_output8;
    private Mat img_output9;
    private Mat img_output10;

 */
/*
    public native void opencv_02(long inputImage, long outputImage1, long outputImage2, long outputImage3, long outputImage4, long outputImage5,
                              long outputImage6,  long outputImage7, long outputImage8, long outputImage9, long outputImage10);
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_02);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Mydata.data_count++;
                Intent intent = new Intent(getApplicationContext(), activity_sub_02_01.class);
                startActivityForResult(intent, 1);
            }
        });

        ImageButton imageButton_1 = findViewById(R.id.imageButton_1); // 도움말 표시하기
        imageButton_1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setContentView(new SomeView(activity_sub_02.this));
            }
        });
/*
        img_input = new Mat();
        //Bitmap bitmap2 = Mydata.sentence_bitmap;
        //Bitmap bmp32 = bitmap2.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(Mydata.sentence_bitmap, img_input);


        if (img_output1 == null)
            img_output1 = new Mat();

        if (img_output2 == null)
            img_output2 = new Mat();

        if (img_output3 == null)
            img_output3 = new Mat();

        if (img_output4 == null)
            img_output4 = new Mat();

        if (img_output5 == null)
            img_output5 = new Mat();

        if (img_output6 == null)
            img_output6 = new Mat();

        if (img_output7 == null)
            img_output7 = new Mat();

        if (img_output8 == null)
            img_output8 = new Mat();

        if (img_output9 == null)
            img_output9 = new Mat();

        if (img_output10 == null)
            img_output10 = new Mat();

        opencv_02(img_input.getNativeObjAddr(), img_output1.getNativeObjAddr(), img_output2.getNativeObjAddr(), img_output3.getNativeObjAddr(),
                img_output4.getNativeObjAddr(), img_output5.getNativeObjAddr(),img_output6.getNativeObjAddr(), img_output7.getNativeObjAddr(),
                img_output8.getNativeObjAddr(), img_output9.getNativeObjAddr(), img_output10.getNativeObjAddr());

        if(img_output1 != null){
            Mydata.example[0] = Bitmap.createBitmap(img_output1.cols(), img_output1.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output1, Mydata.example[0]);
        }
        else{
            uiHelper.toast(this, "null image");
        }

        if(img_output2 != null){
            Mydata.example[1] = Bitmap.createBitmap(img_output2.cols(), img_output2.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output2, Mydata.example[1]);
        }
        else{
            uiHelper.toast(this, "null image");
        }


        if(img_output3 != null){
            Mydata.example[2] = Bitmap.createBitmap(img_output3.cols(), img_output3.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output3, Mydata.example[2]);
        }
        else{
            uiHelper.toast(this, "null image");
        }

        if(img_output4 != null){
            Mydata.example[3] = Bitmap.createBitmap(img_output4.cols(), img_output4.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output4, Mydata.example[3]);
        }
        else{
            uiHelper.toast(this, "null image");
        }


        if(img_output5 != null){
            Mydata.example[4] = Bitmap.createBitmap(img_output5.cols(), img_output5.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output5, Mydata.example[4]);
        }
        else{
            uiHelper.toast(this, "null image");
        }

        if(img_output6 != null){
            Mydata.example[5] = Bitmap.createBitmap(img_output6.cols(), img_output6.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output6, Mydata.example[5]);
        }
        else{
            uiHelper.toast(this, "null image");
        }

        if(img_output7 != null){
            Mydata.example[6] = Bitmap.createBitmap(img_output7.cols(), img_output7.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output7, Mydata.example[6]);
        }
        else{
            uiHelper.toast(this, "null image");
        }

        if(img_output8 != null){
            Mydata.example[7] = Bitmap.createBitmap(img_output8.cols(), img_output8.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output8, Mydata.example[7]);
        }
        else{
            uiHelper.toast(this, "null image");
        }

        if(img_output9 != null){
            Mydata.example[8] = Bitmap.createBitmap(img_output9.cols(), img_output9.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output9, Mydata.example[8]);
        }
        else{
            uiHelper.toast(this, "null image");
        }

        if(img_output10 != null){
            Mydata.example[9] = Bitmap.createBitmap(img_output10.cols(), img_output10.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output10, Mydata.example[9]);
        }
        else{
            uiHelper.toast(this, "null image");
        }
*/


        if(Mydata.example[Mydata.data_count] != null) {
            imageButton_1.setImageBitmap(Mydata.example[Mydata.data_count]);
        }
        else{
            uiHelper.toast(this, "이미지 인식에 실패하여 크롭된 이미지가 없습니다.");
        }


        if(Mydata.myletter_element[Mydata.data_count][0] != null) {
            Uri imageUri = Uri.parse(Mydata.myletter_element[Mydata.data_count][0]);
            imageView1 = (ImageView) findViewById(R.id.imageView1);
            imageView1.setImageURI(imageUri);
        }

        if(Mydata.myletter_element[Mydata.data_count][1] != null) {
            Uri imageUri = Uri.parse(Mydata.myletter_element[Mydata.data_count][1]);
            imageView2 = (ImageView) findViewById(R.id.imageView2);
            imageView2.setImageURI(imageUri);
        }

        if(Mydata.myletter_element[Mydata.data_count][2] != null) {
            Uri imageUri = Uri.parse(Mydata.myletter_element[Mydata.data_count][2]);
            imageView3 = (ImageView) findViewById(R.id.imageView3);
            imageView3.setImageURI(imageUri);
        }

    }

}


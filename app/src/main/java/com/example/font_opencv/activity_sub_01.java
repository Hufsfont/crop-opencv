package com.example.font_opencv;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.font_opencv.enums.ImagePickerEnum;
import com.example.font_opencv.listeners.IImagePickerLister;
import com.example.font_opencv.utils.FileUtils;
import com.example.font_opencv.utils.UiHelper;
import com.yalantis.ucrop.UCrop;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class activity_sub_01 extends AppCompatActivity implements IImagePickerLister {

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;

    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();
    private ImageView imageView1, imageView2, imageView3;

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

    public native void opencv_01(long inputImage, long outputImage1, long outputImage2, long outputImage3, long outputImage4, long outputImage5,
                                 long outputImage6,  long outputImage7, long outputImage8, long outputImage9, long outputImage10);


    //List<Mat> output_img_list = new ArrayList<Mat>();
    //Mat[] output_img = new Mat[10];
    //public native void cv_test(long inputImage, long[] outputImage);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 없애기

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_01); // actiyity에서 컨텐츠 보여주기

        Button button = findViewById(R.id.button); // 이전 activity로 돌아가기
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button button1 = findViewById(R.id.button1); // 다음 activity로 넘어가기
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_sub_02.class);
                startActivityForResult(intent, 2);
            }
        });

        ImageButton btn_popup = findViewById(R.id.btn_popup); // 도움말 표시하기
        btn_popup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), popup.class);
                startActivityForResult(intent, 3);
            }
        });

        findViewById(R.id.btn_camera).setOnClickListener(v -> { //카메라 버튼 클릭시 동작 설정
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    uiHelper.showImagePickerDialog(this, this);
        });

        imageView1 = findViewById(R.id.imageView1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, "ImageCropper needs Storage access in order to store your profile picture.");
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uiHelper.toast(this, "ImageCropper needs Camera access in order to take profile picture.");
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, "ImageCropper needs Camera and Storage access in order to take profile picture.");
                finish();
            }
        } else if (requestCode == ONLY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, "ImageCropper needs Camera access in order to take profile picture.");
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, "ImageCropper needs Storage access in order to store your profile picture.");
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = Uri.parse(currentPhotoPath);
            openCropActivity(uri, uri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = UCrop.getOutput(data);
                showImage1(uri);
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Uri sourceUri = data.getData();
                File file = getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                openCropActivity(sourceUri, destinationUri);
            } catch (Exception e) {
                uiHelper.toast(this, "Please select another image");
            }
        }
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);
    }

    private void showImage1(Uri imageUri) {
        try {
            File file;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                file = FileUtils.getFile(this, imageUri);
                Mydata.sentence_img = imageUri.toString();
            } else {
                file = new File(currentPhotoPath);
                Mydata.sentence_img  = currentPhotoPath;
            }
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Mydata.sentence_bitmap = bitmap;
            imageView1.setImageBitmap(bitmap);

            // native code 함수를 호출하려는 준비 단계
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

            // native code 함수 호출
            opencv_01(img_input.getNativeObjAddr(), img_output1.getNativeObjAddr(), img_output2.getNativeObjAddr(), img_output3.getNativeObjAddr(),
                    img_output4.getNativeObjAddr(), img_output5.getNativeObjAddr(),img_output6.getNativeObjAddr(), img_output7.getNativeObjAddr(),
                    img_output8.getNativeObjAddr(), img_output9.getNativeObjAddr(), img_output10.getNativeObjAddr());

            // 결과 Mat 객체를 비트맵으로 변환해서 Mydata.example[]에 삽입
            Mydata.example[0] = Bitmap.createBitmap(img_output1.cols(), img_output1.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output1, Mydata.example[0]);

            Mydata.example[1] = Bitmap.createBitmap(img_output2.cols(), img_output2.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output2, Mydata.example[1]);

            Mydata.example[2] = Bitmap.createBitmap(img_output3.cols(), img_output3.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output3, Mydata.example[2]);

            Mydata.example[3] = Bitmap.createBitmap(img_output4.cols(), img_output4.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output4, Mydata.example[3]);

            Mydata.example[4] = Bitmap.createBitmap(img_output5.cols(), img_output5.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output5, Mydata.example[4]);

            Mydata.example[5] = Bitmap.createBitmap(img_output6.cols(), img_output6.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output6, Mydata.example[5]);

            Mydata.example[6] = Bitmap.createBitmap(img_output7.cols(), img_output7.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output7, Mydata.example[6]);

            Mydata.example[7] = Bitmap.createBitmap(img_output8.cols(), img_output8.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output8, Mydata.example[7]);

            Mydata.example[8] = Bitmap.createBitmap(img_output9.cols(), img_output9.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output9, Mydata.example[8]);

            Mydata.example[9] = Bitmap.createBitmap(img_output10.cols(), img_output10.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img_output10, Mydata.example[9]);

 /*
            // 배열로 output image 값을 넘겨 받을 수 있는지 테스트

            for (int i = 0; i < 10; i++){
                Mat output_img = new Mat();
                output_img_list.add(output_img);
             }
            int elems = output_img_list.size();
            Log.v("Matobjdata", "number of Matobject read = " + elems);
            long[] temp_adr = new long[elems];

            for (int i = 0; i < elems; i++){
                Mat tempaddr = output_img_list.get(i);
                temp_adr[i] = tempaddr.getNativeObjAddr();
            }

            cv_test(img_input.getNativeObjAddr(), temp_adr);

             for (int i = 0; i < 10; i++){
                 if(output_img_list.get(i) != null) {
                    Mydata.example[i] = Bitmap.createBitmap(output_img_list.get(i).cols(), output_img_list.get(i).rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(output_img_list.get(i), Mydata.example[i]);
                     uiHelper.toast(this, "output_img 에 이미지가 있습니다.");
                }
                 else{
                     uiHelper.toast(this, "output_img 에 이미지가 없습니다.");
                 }
             }
            uiHelper.toast(this, "배열 성공.");
  */

        } catch (Exception e) {
            e.printStackTrace();
            uiHelper.toast(this, "Please select different profile picture.");
        }
    }

    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {
        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();
    }

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file;
        try {
            file = getImageFile(); // 1
        } catch (Exception e) {
            e.printStackTrace();
            uiHelper.toast(this, "Please take another image");
            return;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
        else
            uri = Uri.fromFile(file); // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        System.out.println(storageDir.getAbsolutePath());
        if (storageDir.exists())
            System.out.println("File exists");
        else
            System.out.println("File not exists");
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(500, 100)
                .withAspectRatio(5f, 1f)
                .start(this);
    }

}
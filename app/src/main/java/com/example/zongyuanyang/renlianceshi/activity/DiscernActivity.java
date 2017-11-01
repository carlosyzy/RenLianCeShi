package com.example.zongyuanyang.renlianceshi.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zongyuanyang.renlianceshi.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class DiscernActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener {
    JavaCameraView openCvCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    private int absoluteFaceSize;

    private ImageView iv_imageview1;
    private Bitmap bmp;
    private boolean isJiance=false;

    private ArrayList<Mat> arrayList=new ArrayList<>();
    private void initializeOpenCVDependencies() {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
        // And we are ready to go
        openCvCameraView.enableView();
        loadImag();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_discern);
        openCvCameraView = (JavaCameraView) findViewById(R.id.jcv);
        openCvCameraView.setCvCameraViewListener(this);

        iv_imageview1= (ImageView) findViewById(R.id.iv_imageview1);
        iv_imageview1.setVisibility(View.GONE);

    }
    private void loadImag(){
        Bitmap bitmap1=BitmapFactory.decodeResource(getResources(),R.mipmap.img1);
        Mat mat_src1 = new Mat(bitmap1.getWidth(), bitmap1.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap1, mat_src1);
        arrayList.add(mat_src1);
        Bitmap bitmap2=BitmapFactory.decodeResource(getResources(),R.mipmap.img2);
        Mat mat_src2 = new Mat(bitmap2.getWidth(), bitmap2.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap2, mat_src2);
        arrayList.add(mat_src2);

        Bitmap bitmap3=BitmapFactory.decodeResource(getResources(),R.mipmap.img3);
        Mat mat_src3 = new Mat(bitmap3.getWidth(), bitmap3.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap3, mat_src3);
        arrayList.add(mat_src3);
        Bitmap bitmap4=BitmapFactory.decodeResource(getResources(),R.mipmap.img4);
        Mat mat_src4 = new Mat(bitmap4.getWidth(), bitmap4.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap4, mat_src4);
        arrayList.add(mat_src4);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.e("log_wons", "OpenCV init error");
        }
        initializeOpenCVDependencies();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
        absoluteFaceSize = (int) (height * 0.2);
    }
    private Mat mRgba;
    private Mat mGray;
    @Override
    public void onCameraViewStopped() {
    }
    @Override
    public Mat onCameraFrame(Mat aInputFrame) {

        Imgproc.cvtColor(aInputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        MatOfRect faces = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();
        int faceCount = facesArray.length;

        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(aInputFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
            //saveImage(aInputFrame,new Rect(facesArray[i].tl(), facesArray[i].br()),"discern00_"+i);

        }
        if(faceCount>0){
            if(!isJiance){
                AA(aInputFrame);
                isJiance=true;
            }
        }




        return aInputFrame;
    }
    private void AA(Mat mat){
        bmp = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bmp);
        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv_imageview1.setVisibility(View.VISIBLE);
                    iv_imageview1.setImageBitmap(bmp);
                }
            });
        mat.convertTo(mat, CvType.CV_32F);

        for(int i=0;i<arrayList.size();i++){

            arrayList.get(i).convertTo(arrayList.get(i), CvType.CV_32F);
            double target = Imgproc.compareHist(mat, arrayList.get(i), Imgproc.CV_COMP_CORREL);
            Log.e("aaaa", "相似度 ：   ==" + target);
            Toast.makeText(this, "相似度 ：   ==" + target, Toast.LENGTH_SHORT).show();
        }
    }
    public void BB(View view){
        iv_imageview1.setVisibility(View.GONE);
        isJiance=false;
    }
//    //比较两张图片的相识度
//    private void AA(Mat srcMat,Mat desMat){
//        srcMat.convertTo(srcMat, CvType.CV_32F);
//        desMat.convertTo(desMat, CvType.CV_32F);
//        double target = Imgproc.compareHist(srcMat, desMat, Imgproc.CV_COMP_CORREL);
//        Log.e("aaaa", "相似度 ：   ==" + target);
//        Toast.makeText(this, "相似度 ：   ==" + target, Toast.LENGTH_SHORT).show();
//    }
//    public void saveImage(Mat matrix) {
//        //Mat frameRf = new Mat(matrix.rows(),matrix.cols(),CvType.CV_8UC3);
//        // Bitmap bmp= null;
////        bmp = Bitmap.createBitmap(matrix.width(), matrix.height(), Bitmap.Config.ARGB_8888);
////        Utils.matToBitmap(matrix, bmp);
//
//
//        Imgproc.cvtColor(matrix, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
//        MatOfRect faces = new MatOfRect();
//        if (cascadeClassifier != null) {
//            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
//                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
//        }
//        Rect[] facesArray = faces.toArray();
//        if(facesArray.length>0){
//            for (int i = 0; i < facesArray.length; i++) {
//                Imgproc.rectangle(matrix, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
//            }
//
//            bmp = Bitmap.createBitmap(matrix.width(), matrix.height(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(matrix, bmp);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    iv_imageview1.setVisibility(View.VISIBLE);
//                    iv_imageview1.setImageBitmap(bmp);
//                }
//            });
//        }
//
//
//    }























    public void saveImage(Mat matrix, Rect rect,String name) {

        //Mat frameRf = new Mat(matrix.rows(),matrix.cols(),CvType.CV_8UC3);

       // Bitmap bmp= null;
        bmp =Bitmap.createBitmap( matrix.width(),  matrix.height(),  Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matrix, bmp);

        //covMat2bm(frameRf,bmp);
//        final Bitmap finalBmp = bmp;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                iv_imageview1.setVisibility(View.VISIBLE);
//                iv_imageview1.setImageBitmap(finalBmp);
//            }
//        });


//        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.sousuo);
//        String fileName= Environment.getExternalStorageDirectory()+"/zongyuan"+name+"jpg";
//        Mat sub = matrix.submat(rect);
//        Mat mat=new Mat();
//        Size size = new Size(100, 100);
//        Imgproc.resize(sub, mat, size);


//        Mat src = new Mat();
//        Mat temp = new Mat();
//        Mat dst = new Mat();
//
//
//        Utils.bitmapToMat(bitmap, src);
//        Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGRA2BGR);
//        Log.i("CV", "image type:" + (temp.type() == CvType.CV_8UC3));
//        Imgproc.cvtColor(temp, dst, Imgproc.COLOR_BGR2GRAY);

//        Mat sub = matrix.submat(rect);
//        Mat mat=new Mat();
//        Size size = new Size(100, 100);
//        Imgproc.resize(sub, matrix, size);

//        Utils.matToBitmap(matrix, bitmap);
//        iv_imageview1.setImageBitmap(bitmap);

       // Imgproc.imwrite(fileName, mat);
        //Imgcodecs.imwrite(fileName,mat);
    }

}
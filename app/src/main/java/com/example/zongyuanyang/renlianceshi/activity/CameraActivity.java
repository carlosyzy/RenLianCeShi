package com.example.zongyuanyang.renlianceshi.activity;


/**
 * 2017.11.3 zongyuan.yang
 * 本类调用OpenCV的相机功能  ，检测相机范围中的人脸
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zongyuanyang.renlianceshi.R;
import com.example.zongyuanyang.renlianceshi.Utils.OpanCVUtils;
import com.example.zongyuanyang.renlianceshi.myAdapter.CameraFacesAdapter;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;


public class CameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener {
    private Mat grayscaleImage;
    private int absoluteFaceSize;
    private CascadeClassifier cascadeClassifier;
    private boolean isDetect = false;  //是否正在进行头像检测
    private JavaCameraView openCvCameraView;
    private ListView listview_camera;
    private ArrayList<Bitmap> face_arrayleist = new ArrayList<>();
    private CameraFacesAdapter cameraFacesAdapter;
    private ImageView iv_jiance;
    private Animation circle_anim;
    private TextView tv_jiance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        init();
    }

    private void init() {
        listview_camera = (ListView) findViewById(R.id.listview_camera);
        cameraFacesAdapter = new CameraFacesAdapter(this);

        iv_jiance = (ImageView) findViewById(R.id.iv_jiance);
        IV_faceDetectAnim();
        iv_jiance.startAnimation(circle_anim);
        tv_jiance = (TextView) findViewById(R.id.tv_jiance);
        tv_jiance.setText("检测中···");

        cascadeClassifier = OpanCVUtils.getInstance().getOpenCVClassifier();
        openCvCameraView = (JavaCameraView) findViewById(R.id.jcv);
        openCvCameraView.setCvCameraViewListener(this);
        openCvCameraView.enableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
        absoluteFaceSize = (int) (height * 0.2);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {

        Imgproc.cvtColor(inputFrame, grayscaleImage, Imgproc.COLOR_RGBA2RGB);
        MatOfRect faces = new MatOfRect();
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        // If there are any faces found, draw a rectangle around it
        Rect[] facesArray = faces.toArray();
        int faceCount = facesArray.length;

        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(inputFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
        }


        if (faceCount > 0) {  //表明当前摄像机可能检测到了人脸图像
            if (!isDetect) {   //当前没有进行人脸检测--进行摄像机中人脸的检测
                face_arrayleist.clear();
                isDetect = true;
                fadeDetection1(inputFrame);
            }

        }
        return inputFrame;
    }

    /**
     * 由于相机是动态获取人脸的，会导致获取时出现错误
     * 再次进行人脸识别
     */
    private void fadeDetection1(Mat mm) {
        //将图片灰度化 --提高检测的准确性
        Mat mat = new Mat();
        Imgproc.cvtColor(mm, mat, Imgproc.COLOR_BGR2GRAY);
        //再次进行人脸检测
        MatOfRect faces1 = new MatOfRect();
        cascadeClassifier.detectMultiScale(mat, faces1);
        final Rect[] facesArray1 = faces1.toArray();
        int faceCount1 = facesArray1.length;
        //Log.i("aaa","****************"+faceCount1);
        if (faceCount1 <= 0) {
            isDetect = false;
        } else {
            //检测到类似头像的图像，删除旋转动画
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv_jiance.clearAnimation();
                    tv_jiance.setText("检测完毕");
                }
            });
            for (int i = 0; i < facesArray1.length; i++) {
                Imgproc.rectangle(mat, facesArray1[i].tl(), facesArray1[i].br(), new Scalar(0, 255, 0, 255), 3);
                setFaceBitmap(mm, facesArray1[i].tl(), facesArray1[i].br());
            }
            setListViewAdapter();
        }

    }

    /**
     * 传入参数 Mat 、
     * 将Mat中的人脸提取出来 保存为BitMap
     * 将头像的bitmap进行缩放
     */
    private void setFaceBitmap(Mat mm, Point point1, Point point2) {
        Bitmap bitmap = Bitmap.createBitmap(mm.width(), mm.height(), Bitmap.Config.ARGB_8888);  //创建bitmap
        Utils.matToBitmap(mm, bitmap);  //进行类型转化
        //提取人脸
        final Bitmap bitmap_face = Bitmap.createBitmap(bitmap, (int) (point1.x), (int) (point1.y),
                (int) (point2.x - point1.x), (int) (point2.y - point1.y));
        face_arrayleist.add(bitmap_face);

    }

    private void setListViewAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cameraFacesAdapter.setList(face_arrayleist);
                listview_camera.setAdapter(cameraFacesAdapter);
            }
        });
    }

    /**
     * 检测按钮的点击事件
     */
    public void bt_faceDetect(View view) {
        isDetect = false;
        face_arrayleist.clear();
        cameraFacesAdapter.setList(face_arrayleist);
        cameraFacesAdapter.notifyDataSetChanged();
        iv_jiance.startAnimation(circle_anim);
        tv_jiance.setText("检测中···");
    }

    /**
     * 获取检测按钮的旋转动画
     * 对检测按钮实现动画效果
     */
    private void IV_faceDetectAnim() {
        circle_anim = AnimationUtils.loadAnimation(this, R.anim.anim_facedetect);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
    }
}
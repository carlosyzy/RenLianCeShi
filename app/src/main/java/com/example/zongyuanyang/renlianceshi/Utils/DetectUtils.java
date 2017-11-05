package com.example.zongyuanyang.renlianceshi.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;

import com.example.zongyuanyang.renlianceshi.R;
import com.example.zongyuanyang.renlianceshi.activity.HomeActivity;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by 15732 on 2017/11/5.
 * 此方法进行检测到的图片和库中的样本相似度的比较
 * 单利模式
 */

public class DetectUtils {
    // 设置想要的大小
    private int newWidth = 200;
    private int newHeight = 200;
    private ArrayList<Bitmap> face_arrayleist = new ArrayList<>();      //本地库的样本集合（Bitmap）
    private static DetectUtils detectUtils;
    public static DetectUtils getInstans(){
        if (detectUtils==null){
            detectUtils=new DetectUtils();
        }
        return detectUtils;
    }
    //私有的构造方法
    private DetectUtils(){

    }

    /**
     * 相似度比较，传进来的是Mat类型的两个参数
     */
    public double compare_similar0(Mat mat0){
        double target=0.0;
//        mat0.convertTo(mat0, CvType.CV_32F);
//        mat1.convertTo(mat1, CvType.CV_32F);
//        Log.e("aaaa", "相似度 ：   ==" + target);
        return target;
    }
    /**
     * 相似度比较，传进来的是Mat类型的两个参数
     */
    public double compare_similar1(Bitmap bitmap0){
        bitmap0 = resetBitmapSize(bitmap0);
        Mat mat0 = BitMapToMat(bitmap0);
        Mat mat01 =set_cvtColor(mat0);
        Log.i("aaa", "现1=="+bitmap0.getWidth()+"*******************"+bitmap0.getHeight());
        for (int i = 0; i <face_arrayleist.size() ; i++) {
            //暂时将样本的bitmap大小调整
            Mat mat1 = BitMapToMat(face_arrayleist.get(i));
            Mat mat11 = set_cvtColor(mat1);
            double d=compareFace(mat01,mat11);
            Log.e("aaaa", "相似度 ：   ==" + d);
        }
        return 0;
    }
    /**
     * 将Mat类型转化为bitmap类型
     */
    public Bitmap MatToBitMap(Mat mat){
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);  //创建bitmap
        Utils.matToBitmap(mat, bitmap);  //进行类型转化
        return bitmap;
    }
    /**
     * 将bitmap类型转化为Mat类型
     */
    public  Mat BitMapToMat(Bitmap  bitmap){
        Mat mat0 = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, mat0);
        return mat0;
    }

    /**
     *将图片灰度化
     */
    public Mat set_cvtColor(Mat mm){
        Mat mat = new Mat();
        Imgproc.cvtColor(mm, mat, Imgproc.COLOR_BGR2GRAY);
        return mat;
    }

    /**
     *比较两个mat 类型的相似度
     */
    private double compareFace(Mat mat01,Mat mat11){
        //比较相似度
        Log.e("aaaa", "现==" + mat01.size()+","+mat11.size());
        mat01.convertTo(mat01, CvType.CV_32F);
        mat11.convertTo(mat11, CvType.CV_32F);
        double target = Imgproc.compareHist(mat01, mat11, Imgproc.CV_COMP_CORREL);
        return target;
    }
    public Bitmap resetBitmapSize(Bitmap bitmap){
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //Log.i("aaa",bitmap.getWidth()+"*******************"+bitmap.getHeight());
        return bitmap;
    }
}

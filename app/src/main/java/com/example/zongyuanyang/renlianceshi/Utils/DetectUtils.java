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

        int newWidth = 200;
        int newHeight = 200;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / bitmap0.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap0.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix1 = new Matrix();
        matrix1.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap0 = Bitmap.createBitmap(bitmap0, 0, 0,  bitmap0.getWidth(), bitmap0.getHeight(), matrix1, true);
        Mat mat0 = new Mat(bitmap0.getWidth(), bitmap0.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap0, mat0);
        Mat mat01 = new Mat();
        Imgproc.cvtColor(mat0, mat01, Imgproc.COLOR_BGR2GRAY);
        Log.i("aaa", "现1=="+bitmap0.getWidth()+"*******************"+bitmap0.getHeight());
        for (int i = 0; i <face_arrayleist.size() ; i++) {
            //暂时将样本的bitmap大小调整
            Mat mat1 = new Mat(face_arrayleist.get(i).getWidth(), face_arrayleist.get(i).getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(face_arrayleist.get(i), mat1);
            Mat mat11 = new Mat();
            Imgproc.cvtColor(mat1, mat11, Imgproc.COLOR_BGR2GRAY);
            //比较相似度
            Log.e("aaaa", "现==" + mat01.size()+","+mat11.size());
            mat01.convertTo(mat01, CvType.CV_32F);
            mat11.convertTo(mat11, CvType.CV_32F);
            double target = Imgproc.compareHist(mat01, mat11, Imgproc.CV_COMP_CORREL);
            Log.e("aaaa", "相似度 ：   ==" + target);

        }
        return 0;
    }
    private ArrayList<Mat> arrayList=new ArrayList<>();
    private ArrayList<Bitmap> face_arrayleist = new ArrayList<>();
    public ArrayList<Bitmap> loadImag(Context context){
        Bitmap bitmap1= BitmapFactory.decodeResource(context.getResources(), R.mipmap.img1);
        Mat mat_src1 = new Mat(bitmap1.getWidth(), bitmap1.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap1, mat_src1);
        arrayList.add(mat_src1);
        Bitmap bitmap2=BitmapFactory.decodeResource(context.getResources(),R.mipmap.img2);
        Mat mat_src2 = new Mat(bitmap2.getWidth(), bitmap2.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap2, mat_src2);
        arrayList.add(mat_src2);

        Bitmap bitmap3=BitmapFactory.decodeResource(context.getResources(),R.mipmap.img3);
        Mat mat_src3 = new Mat(bitmap3.getWidth(), bitmap3.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap3, mat_src3);
        arrayList.add(mat_src3);
        Bitmap bitmap4=BitmapFactory.decodeResource(context.getResources(),R.mipmap.img4);
        Mat mat_src4 = new Mat(bitmap4.getWidth(), bitmap4.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap4, mat_src4);
        arrayList.add(mat_src4);
        for(int i=0;i<arrayList.size();i++){
            Mat mm=arrayList.get(i);
            Mat mat = new Mat();
            Imgproc.cvtColor(mm, mat, Imgproc.COLOR_BGR2GRAY);
            //再次进行人脸检测
            MatOfRect faces1 = new MatOfRect();
            OpanCVUtils.getInstance().getOpenCVClassifier().detectMultiScale(mat, faces1);
            final Rect[] facesArray1 = faces1.toArray();
            int faceCount1 = facesArray1.length;
            //Log.i("aaa","****************"+faceCount1);
            for (int j = 0; j < facesArray1.length; j++) {
                //Imgproc.rectangle(mat, facesArray1[j].tl(), facesArray1[j].br(), new Scalar(0, 255, 0, 255), 3);
                setFaceBitmap1(mm, facesArray1[j].tl(), facesArray1[j].br());
            }
        }
        Log.i("aaa","****************样本资源加载完毕");
        return face_arrayleist;
    }
    /**
     * 传入参数 Mat 、
     * 将Mat中的人脸提取出来 保存为BitMap
     * 将头像的bitmap进行缩放
     */
    private void setFaceBitmap1(Mat mm, Point point1, Point point2) {
        Bitmap bitmap = Bitmap.createBitmap(mm.width(), mm.height(), Bitmap.Config.ARGB_8888);  //创建bitmap
        Utils.matToBitmap(mm, bitmap);  //进行类型转化
        //提取人脸
        Bitmap bitmap_face = Bitmap.createBitmap(bitmap, (int) (point1.x), (int) (point1.y),
                (int) (point2.x - point1.x), (int) (point2.y - point1.y));


        //重置图片的大小

        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / bitmap_face.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap_face.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap_face = Bitmap.createBitmap(bitmap_face, 0, 0,  bitmap_face.getWidth(), bitmap_face.getHeight(), matrix, true);
        Log.i("aaa",bitmap_face.getWidth()+"*******************"+bitmap_face.getHeight());
        face_arrayleist.add(bitmap_face);

    }





















    //                //Toast.makeText(mContent,"此功能尚未解锁，请等待",Toast.LENGTH_SHORT).show();
//                //CameraActivity.AA(mList.get(i));
//                Mat mat1 = new Mat(mList.get(i).getWidth(),mList.get(i).getHeight(), CvType.CV_8UC4);
//                Utils.bitmapToMat(mList.get(i), mat1);

//                Mat srcMat = new Mat();
//                Imgproc.cvtColor(mat1, srcMat, Imgproc.COLOR_BGR2GRAY);
//                srcMat.convertTo(srcMat, CvType.CV_32F);
//
//                double target = Imgproc.compareHist(srcMat, srcMat, Imgproc.CV_COMP_CORREL);
//                Log.e("aaaa", "相似度 ：   ==" + target);

}

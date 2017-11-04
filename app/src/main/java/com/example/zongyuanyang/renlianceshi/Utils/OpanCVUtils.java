package com.example.zongyuanyang.renlianceshi.Utils;

import android.content.Context;
import android.util.Log;

import com.example.zongyuanyang.renlianceshi.R;

import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by 15732 on 2017/11/3.
 */

public class OpanCVUtils {
    private static CascadeClassifier cascadeClassifier;
    //私有构造函数 使用单利模式
    private OpanCVUtils(){}
    private static OpanCVUtils opanCVUtils;
    public static OpanCVUtils getInstance(){
        if(opanCVUtils==null){
            opanCVUtils=new OpanCVUtils();
        }
        return opanCVUtils;
    }
    /**
     * 加载面部识别的xml文件
     */
    public static void initializeOpenCVDependencies(Context context) {
        try {
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is =context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
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
            Log.i("aaa","加载xml文件成功");
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
    }
    public  void initOpenCV(Context context){
        if (!OpenCVLoader.initDebug()) {
            Log.e("log_wons", "OpenCV init error");
        }
        initializeOpenCVDependencies(context);
    }
    public CascadeClassifier getOpenCVClassifier(){
        return cascadeClassifier;
    }

}

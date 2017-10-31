package com.example.zongyuanyang.renlianceshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.example.zongyuanyang.renlianceshi.R;

/**
 * zongyuan.yang 2017.10.31  面部识别主界面
 */
public class HomeActivity extends Activity implements View.OnClickListener{

    private ImageView iv_sousuo;
    private Bitmap bitmap;

    //private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }
    private void init(){
        iv_sousuo= (ImageView) findViewById(R.id.iv_sousuo);
        iv_sousuo.setImageResource(R.mipmap.sousuo);
        iv_sousuo.setOnClickListener(this);
        //imageView= (ImageView) findViewById(R.id.imageview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_sousuo:   //打开本地相机
//                Intent intent = new Intent(
//                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 2);// 打开照相机
                startActivity(new Intent(this,DiscernActivity.class));
                break;
        }
    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            // 表示 调用照相机拍照
//            case 2:
//                if (resultCode == RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    bitmap = (Bitmap) bundle.get("data");
//                    //跳转到下一个activity 进行照片中的人脸识别
//                    //DiscernActivity.setBitmap(bitmap);
//                    startActivity(new Intent(this,DiscernActivity.class));
//
//                }
//                break;
//        }
//
//    }
}

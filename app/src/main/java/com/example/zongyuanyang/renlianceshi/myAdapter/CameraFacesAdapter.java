package com.example.zongyuanyang.renlianceshi.myAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zongyuanyang.renlianceshi.R;
import com.example.zongyuanyang.renlianceshi.Utils.DetectUtils;
import com.example.zongyuanyang.renlianceshi.activity.CameraActivity;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Created by 15732 on 2017/11/4.
 */

public class CameraFacesAdapter extends BaseAdapter {

    private Context mContent;
    private ArrayList<Bitmap> mList=new ArrayList<>();
    private LayoutInflater layoutInflater;
    //private ViewHolder viewHolder;
    public CameraFacesAdapter(Context context){
        layoutInflater=LayoutInflater.from(context);
        mContent=context;
    }
    public void setList(ArrayList list){
        mList=list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view=layoutInflater.inflate(R.layout.item_camera_face,null);
            viewHolder=new ViewHolder((ImageView) view.findViewById(R.id.iv_item_camera_face),
                    (TextView)view.findViewById(R.id.tv_item_face_discern),
                    (TextView)view.findViewById(R.id.tv_item_face_delete));
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.getImageView().setImageBitmap(mList.get(i));
        //识别按钮的点击事件
        viewHolder.getfacediscern().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetectUtils.getInstans().compare_similar1(mList.get(i));
            }
        });
        //删除按钮的点击事件
        viewHolder.getfacedelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(i);
                notifyDataSetChanged();

            }
        });

        return view;
    }
    public  class ViewHolder{
        private ImageView imageView;
        private TextView  tv_item_face_discern;
        private TextView  tv_item_face_delete;
        public ViewHolder(ImageView img,TextView tv1,TextView tv2){
            imageView=img;
            tv_item_face_discern=tv1;
            tv_item_face_delete=tv2;
        }
        public ImageView getImageView(){
            return  imageView;
        }
        public TextView getfacediscern(){
            return  tv_item_face_discern;
        }
        public TextView getfacedelete(){
            return  tv_item_face_delete;
        }
    }
}

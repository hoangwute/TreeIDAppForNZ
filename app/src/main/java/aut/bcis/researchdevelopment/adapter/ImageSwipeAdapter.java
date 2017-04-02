package aut.bcis.researchdevelopment.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import aut.bcis.researchdevelopment.treeidfornz.R;

/**
 * Created by admin on 17-Mar-17.
 */

public class ImageSwipeAdapter extends PagerAdapter {
    private ArrayList<Integer> imageList;
    private Activity context;
    private LayoutInflater layoutInflater;

    public ImageSwipeAdapter(Activity context, ArrayList imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.swipe_layout, container, false);
        ImageView imgPic = (ImageView) itemView.findViewById(R.id.imgPic);
        TextView txtImageTitle = (TextView)itemView.findViewById(R.id.txtImagePosition);
        imgPic.setImageResource(imageList.get(position));
        imgPic.setScaleType(ImageView.ScaleType.FIT_XY);
//        Picasso.with(context).load(new File(imageList.get(position))).centerCrop().resize(120, 105).into(imgPic); //load picture using Picasso library
        switch(position) {
            case 0:
                txtImageTitle.setText("Whole Tree");
                break;
            case 1:
                txtImageTitle.setText("Tree Flower");
                break;
            case 2:
                txtImageTitle.setText("Tree Leaf");
                break;
            case 3:
                txtImageTitle.setText("Tree Bark");
                break;
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}

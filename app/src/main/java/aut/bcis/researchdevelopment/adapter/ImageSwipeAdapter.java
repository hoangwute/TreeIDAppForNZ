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
import uk.co.senab.photoview.PhotoViewAttacher;

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
        imgPic.setImageResource(imageList.get(position));
        PhotoViewAttacher photoView = new PhotoViewAttacher(imgPic); //make the image view zoomable
        photoView.setScaleType(ImageView.ScaleType.FIT_XY);
        photoView.update();
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}

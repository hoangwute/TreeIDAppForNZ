package aut.bcis.researchdevelopment.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import aut.bcis.researchdevelopment.model.ListHeader;
import aut.bcis.researchdevelopment.model.OnSwipeTouchListener;
import aut.bcis.researchdevelopment.model.Sighting;
import aut.bcis.researchdevelopment.model.Tree;

import aut.bcis.researchdevelopment.treeidfornz.MySightingActivity;
import aut.bcis.researchdevelopment.treeidfornz.R;
import aut.bcis.researchdevelopment.treeidfornz.SightingInfoActivity;
import aut.bcis.researchdevelopment.treeidfornz.Utility;

/**
 * Created by admin on 24-Apr-17.
 */

public class SightingAdapter extends ArrayAdapter<Object> {
    private Activity context;
    private List<Object> objects;
    private Animation animation = null;

    public SightingAdapter(Activity context, List objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
    }

    private static class ViewHolder {
        private TextView txtSightingCommonName;
        private TextView txtSightingLocation;
        private TextView txtSightingLatinName;
        private TextView txtSightingHeader;
        private ImageView imgSightingPicture;
        private Button btnDeleteSighting;
        private LinearLayout entryLayout;

    }
    @Override
    public int getCount() { //fix array index out of bound exception
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2; // The number of distinct view types the getView() will return.
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Object item = this.objects.get(position);
        final SightingAdapter.ViewHolder holder;
        if(item instanceof Sighting) {
            if (convertView == null) {
                LayoutInflater inflater = this.context.getLayoutInflater();
                holder = new SightingAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.list_sighting_layout, null);
                holder.txtSightingCommonName = (TextView) convertView.findViewById(R.id.txtSightingCommonName);
                holder.txtSightingLocation = (TextView) convertView.findViewById(R.id.txtSightingLocation);
                holder.txtSightingLatinName = (TextView) convertView.findViewById(R.id.txtSightingLatinName);
                holder.imgSightingPicture = (ImageView) convertView.findViewById(R.id.imgSightingPicture);
                holder.btnDeleteSighting = (Button) convertView.findViewById(R.id.btnDeleteSighting);
                holder.entryLayout = (LinearLayout) convertView.findViewById(R.id.entryLayout);
                convertView.setTag(holder);
            } else {
                holder = (SightingAdapter.ViewHolder) convertView.getTag();
            }
            final Sighting sighting = (Sighting) item;
            holder.txtSightingCommonName.setText(sighting.getCommonName());
            holder.txtSightingLatinName.setText(sighting.getLatinName());
            holder.txtSightingLocation.setText(sighting.getLocation());
            if(sighting.getSightingPicture() != null)
                Picasso.with(context).load(new File(sighting.getSightingPicture())).centerCrop().resize(107, 107).into(holder.imgSightingPicture); //load picture using Picasso library
            else
                Picasso.with(context).load(R.drawable.noimagefound).centerCrop().resize(107, 107).into(holder.imgSightingPicture);
            holder.imgSightingPicture.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.entryLayout.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onSwipeRight() { //actually should be the reverse order.
                    if(holder.btnDeleteSighting.isShown()) {
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.deletebuttondisappear);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                holder.btnDeleteSighting.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        holder.btnDeleteSighting.startAnimation(animation);
                    }
                }
                public void onSwipeLeft() {
                    if(!holder.btnDeleteSighting.isShown()) {
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.deletebuttonappear);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                holder.btnDeleteSighting.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        holder.btnDeleteSighting.startAnimation(animation);
                    }
                }
                @Override
                public void onClick() {
                    Toast.makeText(context, String.valueOf(holder.btnDeleteSighting.isShown()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SightingInfoActivity.class);
                    Sighting sighting = (Sighting) MySightingActivity.sightingList.get(position);
                    intent.putExtra("ID", sighting.getId());
                    context.startActivity(intent);
                }
            });

            holder.btnDeleteSighting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.btnDeleteSighting.isShown()) {
                        if(position == objects.size()-1) {
                            Object previousItem = objects.get(position-1);
                            if(previousItem instanceof ListHeader)
                                handleDelete(previousItem);
                            handleDelete(sighting);
                            Utility.deleteSightingPicture(sighting.getId(), context);
                        }
                        else {
                            Object previousItem = objects.get(position-1);
                            Object nextItem = objects.get(position+1);
                            if(previousItem instanceof ListHeader && nextItem instanceof ListHeader)
                                handleDelete(previousItem);
                            handleDelete(sighting);
                            Utility.deleteSightingPicture(sighting.getId(), context);
                        }
                    }
                }
            });
        }
        else if(item instanceof ListHeader) {
            if (convertView == null) {
                LayoutInflater inflater = this.context.getLayoutInflater();
                holder = new SightingAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.header_list_layout, null);
                holder.txtSightingHeader = (TextView) convertView.findViewById(R.id.txtListHeader);
                convertView.setTag(holder);
            } else {
                holder = (SightingAdapter.ViewHolder) convertView.getTag();
            }
            ListHeader listHeader = (ListHeader) item;
            if(listHeader != null)
                holder.txtSightingHeader.setText(listHeader.getName());
        }
        animation = AnimationUtils.loadAnimation(this.context, R.anim.listvieweffect); //animation
        convertView.startAnimation(animation);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) { //must have for 2 layouts header and listtree_layout
        if (getItem(position) instanceof ListHeader){
            return 0;
        }else{
            return 1;
        }
    }

    private void handleDelete(Object listObject) {
        this.remove(listObject);
    }
}

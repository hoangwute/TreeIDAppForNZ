package aut.bcis.researchdevelopment.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aut.bcis.researchdevelopment.model.ListHeader;
import aut.bcis.researchdevelopment.model.Tree;
import aut.bcis.researchdevelopment.treeidfornz.IdentificationResultActivity;
import aut.bcis.researchdevelopment.treeidfornz.ListActivity;
import aut.bcis.researchdevelopment.treeidfornz.MainActivity;
import aut.bcis.researchdevelopment.treeidfornz.R;

/**
 * Created by VS9 X64Bit on 26/08/2016.
 */
public class TreeAdapter extends ArrayAdapter<Object> implements Filterable {
    private Activity context;
    private List<Object> objects;
    private ItemFilter mFilter = new ItemFilter();
    private List<Object> originalData = null;
    private Animation animation = null;

    public TreeAdapter(Activity context, List objects) {
        super(context, 0, objects); //resource id is set to 0 because we are going to inflate 2 layouts instead of just a fixed one.
        this.context = context;
        this.objects = objects;
        this.originalData = objects;
    }

    private static class ViewHolder {
        private TextView txtCommonName;
        private TextView txtLatinName;
        private ImageButton btnLike, btnDislike;
        private ImageView imgFirstPicture;
        private TextView txtListHeader;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Object item = this.objects.get(position);
        final ViewHolder holder;
        if(item instanceof Tree) {
            if (convertView == null) {
                LayoutInflater inflater = this.context.getLayoutInflater();
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listtree_layout, null);
                holder.txtCommonName = (TextView) convertView.findViewById(R.id.txtCommonName);
                holder.txtLatinName = (TextView) convertView.findViewById(R.id.txtLatinName);
                holder.btnLike = (ImageButton) convertView.findViewById(R.id.btnLike);
                holder.btnDislike = (ImageButton) convertView.findViewById(R.id.btnDisLike);
                holder.imgFirstPicture = (ImageView) convertView.findViewById(R.id.imgFirstPicture);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Tree tree = (Tree) item;
            holder.txtCommonName.setText(tree.getCommonName());
            holder.txtLatinName.setText(tree.getLatinName());
            Picasso.with(context).load(new File(tree.getFirstPicture())).centerCrop().resize(120, 105).into(holder.imgFirstPicture); //load picture using Picasso library
            if(tree.getLiked() == 1) {
                holder.btnLike.setVisibility(View.INVISIBLE);
                holder.btnDislike.setVisibility(View.VISIBLE);
            }
            else {
                holder.btnDislike.setVisibility(View.INVISIBLE);
                holder.btnLike.setVisibility(View.VISIBLE);
            }
            final TreeAdapter adapter = this;
            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btnLike.setVisibility(View.INVISIBLE);
                    holder.btnDislike.setVisibility(View.VISIBLE);
                    Cursor cursor = MainActivity.database.rawQuery("UPDATE Tree SET Liked = 1 where ID = " + tree.getId(), null);
                    cursor.moveToFirst();
                    cursor.close();
                    Toast.makeText(context, tree.getCommonName() + " has been added to the favourite list", Toast.LENGTH_SHORT).show();
                    tree.setLiked(1); //save the state of the object
                }
            });
            holder.btnDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((context.getLocalClassName().equals("ListActivity") && ListActivity.favouriteSelected == true) ||
                            (context.getLocalClassName().equals("IdentificationResultActivity") && IdentificationResultActivity.identifiedFavouriteSelected == true)) {
                        if(position == objects.size()-1) {
                            Object previousItem = objects.get(position-1);
                            if(previousItem instanceof ListHeader)
                                handleDelete(previousItem);
                            handleDelete(tree);
                        }
                        else {
                            Object previousItem = objects.get(position-1);
                            Object nextItem = objects.get(position+1);
                            if(previousItem instanceof ListHeader && nextItem instanceof ListHeader)
                                handleDelete(previousItem);
                            handleDelete(tree);
                        }
                    }
                    holder.btnDislike.setVisibility(View.INVISIBLE);
                    holder.btnLike.setVisibility(View.VISIBLE);
                    Cursor cursor = MainActivity.database.rawQuery("UPDATE Tree SET Liked = 0 where ID = " + tree.getId(), null);
                    cursor.moveToFirst();
                    cursor.close();
                    Toast.makeText(context, tree.getCommonName() + " has been removed from the favourite list", Toast.LENGTH_SHORT).show();
                    tree.setLiked(0); //save the state of the object
                    if(objects.size() == 0) {
                        if(ListActivity.favouriteSelected == true) {
                            ListActivity.txtAnnounce.setVisibility(View.VISIBLE);
                            ListActivity.txtAnnounce.setText("Please add more species into the favourite list.");
                        }
                        else if(IdentificationResultActivity.identifiedFavouriteSelected == true) {
                            IdentificationResultActivity.txtIdentifiedAnnounce.setVisibility(View.VISIBLE);
                            IdentificationResultActivity.txtIdentifiedAnnounce.setText("Please add more species into the favourite list.");
                        }
                    }
                }
            });
        }
        else if(item instanceof ListHeader) {
            if (convertView == null) {
                LayoutInflater inflater = this.context.getLayoutInflater();
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listheader_layout, null);
                holder.txtListHeader = (TextView) convertView.findViewById(R.id.txtListHeader);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ListHeader listHeader = (ListHeader) item;
            if(listHeader != null)
                holder.txtListHeader.setText(listHeader.getName());
        }
        animation = AnimationUtils.loadAnimation(this.context, R.anim.listvieweffect); //animation
        convertView.startAnimation(animation);
        return convertView;
    }
    private void handleDelete(Object listObject) {
        this.remove(listObject);
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
    public int getItemViewType(int position) { //must have for 2 layouts header and listtree_layout
        if (getItem(position) instanceof ListHeader){
            return 0;
        }else{
            return 1;
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Object> list = originalData;

            int count = list.size();
            final ArrayList<Object> nlist = new ArrayList<>();

            String filterableString = "";

            for (int i = 0; i < count; i++) {
                if(list.get(i) instanceof Tree) {
                    Tree tree = (Tree) list.get(i);
                    filterableString = tree.getCommonName() + tree.getLatinName();
                }
                else if(list.get(i) instanceof Object) {
                    ListHeader header = (ListHeader) list.get(i);
                    filterableString = header.getName();
                }
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            objects = (List<Object>) results.values;
            notifyDataSetChanged();
        }
    }
}

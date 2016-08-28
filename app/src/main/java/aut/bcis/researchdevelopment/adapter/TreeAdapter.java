package aut.bcis.researchdevelopment.adapter;

import android.app.Activity;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aut.bcis.researchdevelopment.model.Tree;
import aut.bcis.researchdevelopment.treeidfornz.R;

/**
 * Created by VS9 X64Bit on 26/08/2016.
 */
public class TreeAdapter extends ArrayAdapter<Tree> implements Filterable {
    private Activity context;
    private int resource;
    private List<Tree> objects;
    private ItemFilter mFilter = new ItemFilter();
    private List<Tree>originalData = null;
    private Animation animation = null;

    public TreeAdapter(Activity context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.originalData = objects;
    }

    private static class ViewHolder {
        private TextView txtCommonName;
        private TextView txtLatinName;
        private ImageButton btnLike;
        private ImageView imgFirstPicture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            holder = new ViewHolder();
            convertView = inflater.inflate(this.resource, null);
            holder.txtCommonName = (TextView) convertView.findViewById(R.id.txtCommonName);
            holder.txtLatinName = (TextView) convertView.findViewById(R.id.txtLatinName);
            holder.btnLike = (ImageButton) convertView.findViewById(R.id.btnLike);
            holder.imgFirstPicture = (ImageView) convertView.findViewById(R.id.imgFirstPicture);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        final Tree tree = this.objects.get(position);
        holder.txtCommonName.setText(tree.getCommonName());
        holder.txtLatinName.setText(tree.getLatinName());
        Picasso.with(context).load(new File(tree.getFirstPicture())).centerCrop().resize(120,105).into(holder.imgFirstPicture);
        animation = AnimationUtils.loadAnimation(this.context, R.anim.listvieweffect); //animation
        convertView.startAnimation(animation);
        return convertView;
    }
    @Override
    public int getCount() { //fix array index out of bound exception
        return objects.size();
    }
    @Override
    public Tree getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Tree> list = originalData;

            int count = list.size();
            final ArrayList<Tree> nlist = new ArrayList<Tree>();

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getCommonName() + list.get(i).getLatinName();
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
            objects = (ArrayList<Tree>) results.values;
            notifyDataSetChanged();
        }
    }
}

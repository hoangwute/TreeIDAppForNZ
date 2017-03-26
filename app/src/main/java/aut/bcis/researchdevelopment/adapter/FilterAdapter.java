package aut.bcis.researchdevelopment.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

import aut.bcis.researchdevelopment.model.FilterEntry;
import aut.bcis.researchdevelopment.treeidfornz.MainActivity;
import aut.bcis.researchdevelopment.treeidfornz.R;
import aut.bcis.researchdevelopment.treeidfornz.Utility;

import static aut.bcis.researchdevelopment.treeidfornz.MainActivity.markerList;


/**
 * Created by admin on 04-Mar-17.
 */

public class FilterAdapter extends ArrayAdapter<FilterEntry> {
    private Activity context;
    private List<FilterEntry> objects;
    private int resource;
    private int textViewId;

    public FilterAdapter(Activity context, int resource, int textViewId, List<FilterEntry> objects) {
        super(context, resource, textViewId, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.textViewId = textViewId;
    }

    private static class ViewHolder {
        private TextView txtFilterCommonName;
        private TextView txtFilterLatinName;
        private TextView txtFilterNumber;
        //        private ImageView imgFiltered;
//        private ImageView imgNotFiltered;
        private CheckBox chkFilter;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Object item = this.objects.get(position);
        final FilterAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            holder = new FilterAdapter.ViewHolder();
            convertView = inflater.inflate(this.resource, null);
            holder.txtFilterCommonName = (TextView) convertView.findViewById(R.id.txtFilterCommonName);
            holder.txtFilterLatinName = (TextView) convertView.findViewById(R.id.txtFilterLatinName);
            holder.txtFilterNumber = (TextView) convertView.findViewById(R.id.txtFilterNumber);
//            holder.imgFiltered = (ImageView) convertView.findViewById(R.id.imgFiltered);
//            holder.imgNotFiltered = (ImageView) convertView.findViewById(R.id.imgNotFiltered);
            holder.chkFilter = (CheckBox) convertView.findViewById(R.id.chkFilter);
            convertView.setTag(holder);
        } else {
            holder = (FilterAdapter.ViewHolder) convertView.getTag();
        }
        final FilterEntry filterEntry = (FilterEntry) item;
        holder.txtFilterCommonName.setText(filterEntry.getCommonName());
        holder.txtFilterLatinName.setText(filterEntry.getLatinName());
        holder.txtFilterNumber.setText(String.valueOf(filterEntry.getCount()));
//        Toast.makeText(context, "Position: " + String.valueOf(position) + " Filtered: " + String.valueOf(filterEntry.isFiltered()), Toast.LENGTH_SHORT).show();
        if (filterEntry.isFiltered() == 1)
            holder.chkFilter.setChecked(true);
        else if (filterEntry.isFiltered() == 0)
            holder.chkFilter.setChecked(false);
        holder.chkFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.chkFilter.isChecked()) {
//                    Toast.makeText(context, "CHECKED HERE", Toast.LENGTH_SHORT).show();
                    holder.chkFilter.setChecked(true);
                    for (Marker m : markerList) {
                        Bundle bundle = (Bundle) m.getTag();
                        if(bundle != null) {
                            String commonName = bundle.getString("Tree common name");
                            if (commonName.equals(filterEntry.getCommonName())) {
                                m.setVisible(true);
                                System.out.println(m.isVisible());
                                filterEntry.setFiltered(1);
                                Cursor cursor = MainActivity.database.rawQuery("UPDATE FilterEntry SET Filtered = 1 WHERE ID = " + filterEntry.getId(), null);
                                cursor.moveToFirst();
                                cursor.close();
                            }
                        }
                    }
                } else {
//                    Toast.makeText(context, "UNCHECKED HERE", Toast.LENGTH_SHORT).show();
                    holder.chkFilter.setChecked(false);
                    for (Marker m : markerList) {
                        Bundle bundle = (Bundle) m.getTag();
                        if (bundle != null) {
                            String commonName = bundle.getString("Tree common name");
                            if (commonName.equals(filterEntry.getCommonName())) {
                                m.setVisible(false);
                                System.out.println(m.isVisible());
                                filterEntry.setFiltered(0);
                                Cursor cursor = MainActivity.database.rawQuery("UPDATE FilterEntry SET Filtered = 0 WHERE ID = " + filterEntry.getId(), null);
                                cursor.moveToFirst();
                                cursor.close();
                            }
                        }
                    }
                }
            }
        });
//        holder.chkFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { bug boi vi doan nay dc chay toi 3 lan,
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                for(Marker m : markerList) {
//                    Bundle bundle = (Bundle) m.getTag();
//                    String commonName = bundle.getString("Tree common name");
//                    if(commonName.equals(filterEntry.getCommonName())) {
//                        if(!b) {
//                            m.setVisible(false);
//                            filterEntry.setFiltered(0);
////                            Toast.makeText(context, "UPDATED", Toast.LENGTH_SHORT).show();
//                            Utility.updateFilterStatus(b, filterEntry.getId(), context);
//                        }
//                        else {
//                            m.setVisible(true);
//                            filterEntry.setFiltered(1);
////                            Toast.makeText(context, "UPDATED", Toast.LENGTH_SHORT).show();
//                            Utility.updateFilterStatus(b, filterEntry.getId(), context);
//                        }
//                    }
//                }
//            }
//        });
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.spinner_button_layout, null);
        }
        return convertView;

    }
    //        if(filterEntry.isFiltered() == 1) {
//            holder.chkFilter.setChecked(true);
////            holder.imgFiltered.setVisibility(View.INVISIBLE);
////            holder.imgNotFiltered.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.chkFilter.setChecked(false);
////            holder.imgFiltered.setVisibility(View.INVISIBLE);
////            holder.imgNotFiltered.setVisibility(View.VISIBLE);
//        }
//        holder.imgFiltered.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.imgFiltered.setVisibility(View.INVISIBLE);
//                holder.imgNotFiltered.setVisibility(View.VISIBLE);
//                for(Marker m : markerList) {
//                    Bundle bundle = (Bundle) m.getTag();
//                    String commonName = bundle.getString("Tree common name");
//                    if(commonName.equals(filterEntry.getCommonName())) {
//                        m.setVisible(true);
//                        Cursor cursor = MainActivity.database.rawQuery("UPDATE FilterEntry SET Filtered = 1 WHERE ID = " + filterEntry.getId(), null);
//                        cursor.moveToFirst();
//                        cursor.close();
//                        filterEntry.setFiltered(1);
//                    }
//                }
//            }
//        });
//        holder.imgNotFiltered.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.imgNotFiltered.setVisibility(View.INVISIBLE);
//                holder.imgFiltered.setVisibility(View.VISIBLE);
//                for(Marker m : markerList) {
//                    Bundle bundle = (Bundle) m.getTag();
//                    String commonName = bundle.getString("Tree common name");
//                    if(commonName.equals(filterEntry.getCommonName())) {
//                        m.setVisible(true);
//                        Cursor cursor = MainActivity.database.rawQuery("UPDATE FilterEntry SET Filtered = 0 WHERE ID = " + filterEntry.getId(), null);
//                        cursor.moveToFirst();
//                        cursor.close();
//                        filterEntry.setFiltered(0);
//                    }
//                }
//            }
//        });

}

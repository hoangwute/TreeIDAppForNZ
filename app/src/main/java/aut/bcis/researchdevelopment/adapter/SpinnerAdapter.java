package aut.bcis.researchdevelopment.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;


import aut.bcis.researchdevelopment.model.FilterEntry;
import aut.bcis.researchdevelopment.treeidfornz.ListActivity;
import aut.bcis.researchdevelopment.treeidfornz.R;



/**
 * Created by admin on 04-Apr-17.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
    private Activity context;
    private List<String> objects;
    private int resource;
    private int textViewId;

    public SpinnerAdapter(Activity context, int resource, int textViewId, List<String> objects) {
        super(context, resource, textViewId, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.textViewId = textViewId;
    }
    private static class ViewHolder {
        private RadioButton radEnglishName;
        private RadioButton radMaoriName;
        private RadioButton radLatinName;
        private TextView txtRequired;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Object item = this.objects.get(position);
        final SpinnerAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = this.context.getLayoutInflater();
            holder = new SpinnerAdapter.ViewHolder();
            convertView = inflater.inflate(this.resource, null);
            holder.radEnglishName = (RadioButton) convertView.findViewById(R.id.radEnglishName);
            holder.radMaoriName = (RadioButton) convertView.findViewById(R.id.radMaoriName);
            holder.radLatinName = (RadioButton) convertView.findViewById(R.id.radLatinName);
            holder.txtRequired = (TextView) convertView.findViewById(R.id.txtRequired);
            convertView.setTag(holder);
        } else {
            holder = (SpinnerAdapter.ViewHolder) convertView.getTag();
        }
        final String testString = (String) item;
        holder.txtRequired.setText(testString);
        holder.radEnglishName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {

                }
            }
        });
        holder.radMaoriName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {


                }
            }
        });
        holder.radLatinName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {

                }
            }
        });

        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        convertView.setMinimumWidth(width);
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
}

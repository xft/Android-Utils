package com.exercise.utils;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FuncGroupAdapter extends ArrayAdapter<FuncGroup> {
	
	private Context context;
	
	public FuncGroupAdapter(Context context, List<FuncGroup> objects) {
		super(context, R.layout.icon_function_screen, objects);
		
		this.context = context;
	}
	
    public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.icon_function_screen, parent, false);
        }
        
        FuncGroup funcGroup = getItem(position);
        ImageView icon_view = (ImageView)v.findViewById(R.id.icon);
        
        TextView title_view = (TextView)v.findViewById(android.R.id.title);
        TextView summary_view = (TextView)v.findViewById(android.R.id.summary);
        icon_view.setImageResource(funcGroup.icon);
        title_view.setText(funcGroup.title);
        summary_view.setText(funcGroup.summary);
        
        return v;
    }
}

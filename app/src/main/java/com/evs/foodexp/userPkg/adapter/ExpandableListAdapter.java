package com.evs.foodexp.userPkg.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evs.foodexp.R;
import com.evs.foodexp.models.ServiceModel;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    ArrayList<ServiceModel> adapterarrayList;
    ChildViewHolder CVHolder;
    ViewHolder holder;

    public ExpandableListAdapter(Context context, ArrayList<ServiceModel> adapterarrayList) {
        this._context = context;
        this.adapterarrayList = adapterarrayList;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.adapterarrayList.get(groupPosition).getCategory().get(childPosititon);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expanded_itemview, null);
            CVHolder = new ChildViewHolder();
            CVHolder.subtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
            CVHolder.img_check = (RadioButton) convertView.findViewById(R.id.img_check);
            CVHolder.main_layout = (LinearLayout) convertView.findViewById(R.id.main_layout);

            convertView.setTag(CVHolder);
        } else {
            CVHolder = (ChildViewHolder) convertView.getTag();
        }




//
        CVHolder.subtitle.setText(adapterarrayList.get(groupPosition).getCategory().get(childPosition).getName());

            if(childPosition==adapterarrayList.get(groupPosition).getCategory().size()-1){
                CVHolder.main_layout.setBackground(_context.getResources().getDrawable(R.drawable.half_curve_layout_gray));
            }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return adapterarrayList.get(groupPosition).getCategory().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return adapterarrayList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return adapterarrayList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_group, parent, false);
            holder = new ViewHolder();

            holder.diverseAppService = (TextView) convertView.findViewById(R.id.diverseAppService);
            holder.main_layout = (RelativeLayout) convertView.findViewById(R.id.main_layout);


            convertView.setTag(holder);
        } else


        if (isExpanded) {
            holder.diverseAppService.setTypeface(null, Typeface.BOLD);
            holder.main_layout.setBackground(_context.getResources().getDrawable(R.drawable.half_curve_layout_shape));

        } else {
            holder.diverseAppService.setTypeface(null, Typeface.NORMAL);
            holder.main_layout.setBackground(_context.getResources().getDrawable(R.drawable.back_silver));
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class ViewHolder {

        private TextView diverseAppService;
        RelativeLayout main_layout;
    }

    private class ChildViewHolder {
        TextView subtitle;
        LinearLayout main_layout;
        RadioButton img_check;

    }

}

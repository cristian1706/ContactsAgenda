package com.example.cristian.contactsagenda.Utils;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cristian.contactsagenda.R;
import java.util.List;

public class ContactPropertyListAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater;
    private List<String> mProperties = null;
    private int layoutResource;
    private Context mContext;


    public ContactPropertyListAdapter(@NonNull Context context, int resource, @NonNull List<String> properties) {
        super(context, resource, properties);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mProperties = properties;
    }

    private static class ViewHolder {
        TextView property;
        ImageView leftIcon;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            /*Cambios en la vista*/
            holder.property = (TextView) convertView.findViewById(R.id.tvMiddleCardView);
            holder.leftIcon = (ImageView) convertView.findViewById(R.id.iconLeftCardView);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*Cambios en la vista*/
        final String property = getItem(position);
        holder.property.setText(property);

        /*Chequea si es un email o un phonenumber*/
        if(property.contains("@")) {
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_email", null, mContext.getPackageName()));
        }
        else if(property.length() != 0) {
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_phone", null, mContext.getPackageName()));
        }

        return convertView;

    }

}

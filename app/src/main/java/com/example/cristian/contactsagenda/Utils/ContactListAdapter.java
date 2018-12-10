package com.example.cristian.contactsagenda.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cristian.contactsagenda.R;
import com.example.cristian.contactsagenda.models.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactListAdapter extends ArrayAdapter<Contact> {

    private LayoutInflater mInflater;
    private List<Contact> mContacts = null;
    private ArrayList<Contact> arrayList; //usado para el buscador del toolbar
    private int layoutResource;
    private Context mContext;


    public ContactListAdapter(@NonNull Context context, int resource, @NonNull List<Contact> contacts) {
        super(context, resource, contacts);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mContacts = contacts;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mContacts);
    }


    private static class ViewHolder {
        TextView name;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final ContactListAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ContactListAdapter.ViewHolder();

            /*Cambios en la vista*/
            holder.name = (TextView) convertView.findViewById(R.id.contactName);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*Cambios en la vista*/
        String name_ = getItem(position).getName();
        holder.name.setText(name_);

        return convertView;

    }

    /*Filtro de búsqueda de contactos*/
    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        mContacts.clear();
        if(text.length() == 0){
            mContacts.addAll(arrayList);
        }
        else {
            mContacts.clear();
            for (Contact contact: arrayList) {
                if (contact.getName().toLowerCase(Locale.getDefault()).contains(text)){
                    mContacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

}

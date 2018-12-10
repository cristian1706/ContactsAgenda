package com.example.cristian.contactsagenda;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.contactsagenda.Utils.ConfirmDeleteContactDialog;
import com.example.cristian.contactsagenda.Utils.ContactPropertyListAdapter;
import com.example.cristian.contactsagenda.Utils.DatabaseHelper;
import com.example.cristian.contactsagenda.models.Contact;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";

    public interface OnEditContactListener {
        public void onEditContactSelected(Contact contact);
    }

    OnEditContactListener mOnEditContactListener;

    public ContactFragment() {
        super();
        setArguments(new Bundle());
    }

    private Toolbar toolbar;
    private Contact mContact;
    private TextView mContactName;
    private ListView mListView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.contactToolbar);
        mContactName = (TextView) view.findViewById(R.id.contactName);
        mListView = (ListView) view.findViewById(R.id.lvContactProperties);
        mContact = getContactFromBundle();


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        init();

        /*Comportamiento para el backarrow*/
        ImageView ivBackarrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackarrow.setOnClickListener((v) -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        /*Ir a editar contacto*/
        ImageView ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener((v) -> {
            mOnEditContactListener.onEditContactSelected(mContact);
        });

        /*Dialogo para borrar contacto*/
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        ivDelete.setOnClickListener((v) -> {
            ConfirmDeleteContactDialog dialog = new ConfirmDeleteContactDialog(mContact);
            dialog.show(getFragmentManager(), getString(R.string.deleteContactDialog));
        });


        return view;
    }

    private void init() {
        mContactName.setText(mContact.getName());

        ArrayList<String> properties = new ArrayList<>();
        properties.add(mContact.getPhonenumber());
        properties.add(mContact.getEmail());
        ContactPropertyListAdapter adapter = new ContactPropertyListAdapter(getActivity(),R.layout.cardview, properties);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.contact_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuitem_delete:
//                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
//                Cursor cursor = databaseHelper.getContactID(mContact);
//
//                int contactID = -1;
//                while(cursor.moveToNext()) {
//                    contactID = cursor.getInt(0);
//                }
//                if(contactID > -1) {
//                    if(databaseHelper.deleteContact(contactID) > 0){
//                        Toast.makeText(getActivity(), "Contacto borrado", Toast.LENGTH_SHORT).show();
//
//                        this.getArguments().clear();
//                        getActivity().getSupportFragmentManager().popBackStack();
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getContactID(mContact);

        int contactID = -1;
        while(cursor.moveToNext()) {
            contactID = cursor.getInt(0);
        }
        if(contactID > -1) {
            init();
        }
        else {
            this.getArguments().clear();//opcional
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /*Devuelve el contacto seleccionado desde el bundle, que viene de MainActivity)*/
    private Contact getContactFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.contact));
        }
        else {
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnEditContactListener = (OnEditContactListener)getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassException: " + e.getMessage());
        }
    }
}

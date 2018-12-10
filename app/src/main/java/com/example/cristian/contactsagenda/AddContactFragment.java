package com.example.cristian.contactsagenda;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.contactsagenda.Utils.DatabaseHelper;
import com.example.cristian.contactsagenda.models.Contact;

import static android.content.ContentValues.TAG;

public class AddContactFragment extends Fragment {

    private Contact mContact;
    private EditText mPhoneNumber, mName, mEmail;
    private Spinner mSelectContacttype;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addcontact, container, false);
        mPhoneNumber = (EditText) view.findViewById(R.id.etContactPhone);
        mName = (EditText) view.findViewById(R.id.etContactName);
        mEmail = (EditText) view.findViewById(R.id.etContactEmail);
        mSelectContacttype = (Spinner) view.findViewById(R.id.selectContacttype);
        toolbar = (Toolbar) view.findViewById(R.id.editContactToolbar);

        /*Titulo para el toolbar*/
        TextView heading = (TextView) view.findViewById(R.id.textContactToolbar);
        heading.setText(getString(R.string.add_contact));

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        /*Comportamiento para el backarrow*/
        ImageView ivBackarrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackarrow.setOnClickListener((v) -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });


        /*Guardando un nuevo contacto*/
        ImageView confirmNewContact = (ImageView) view.findViewById(R.id.ivCheck);
        confirmNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkStringIfNull(mName.getText().toString())){
                    Log.d(TAG, "onClick: guardando el contacto nuevo " + mName.getText().toString());
                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    Contact contact = new Contact(mName.getText().toString(),
                            mPhoneNumber.getText().toString(),
                            mEmail.getText().toString(),
                            mSelectContacttype.getSelectedItem().toString());
                    if(databaseHelper.addContact(contact)){
                        Toast.makeText(getActivity(), "Contacto guardado", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private boolean checkStringIfNull(String string) {
        if (string.equals("")) {
            return false;
        }
        else {
            return true;
        }
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
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

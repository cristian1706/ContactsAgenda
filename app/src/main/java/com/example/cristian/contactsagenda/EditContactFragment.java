package com.example.cristian.contactsagenda;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.contactsagenda.Utils.ConfirmDeleteContactDialog;
import com.example.cristian.contactsagenda.Utils.DatabaseHelper;
import com.example.cristian.contactsagenda.models.Contact;

public class EditContactFragment extends Fragment {
    private static final String TAG = "EditContactFragment";

    public EditContactFragment() {
        super();
        setArguments(new Bundle());
    }

    private Contact mContact;
    private EditText mPhoneNumber, mName, mEmail;
    private Spinner mSelectContacttype;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editcontact, container, false);
        mPhoneNumber = (EditText) view.findViewById(R.id.etContactPhone);
        mName = (EditText) view.findViewById(R.id.etContactName);
        mEmail = (EditText) view.findViewById(R.id.etContactEmail);
        mSelectContacttype = (Spinner) view.findViewById(R.id.selectContacttype);
        toolbar = (Toolbar) view.findViewById(R.id.editContactToolbar);

        /*Titulo para el toolbar*/
        TextView heading = (TextView) view.findViewById(R.id.textContactToolbar);
        heading.setText(getString(R.string.edit_contact));

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        mContact = getContactFromBundle();
        if (mContact != null){
            init();
        }

        /*Dialogo para borrar contacto*/
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        ivDelete.setOnClickListener((v) -> {
            ConfirmDeleteContactDialog dialog = new ConfirmDeleteContactDialog(mContact);
            dialog.show(getFragmentManager(), getString(R.string.deleteContactDialog));
        });

        /*Comportamiento para el backarrow*/
        ImageView ivBackarrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackarrow.setOnClickListener((v) -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        /*Guardando cambios del contacto*/
        ImageView ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
        ivCheck.setOnClickListener((v) -> {
            if(checkStringIfNull(mName.getText().toString())){
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                Cursor cursor = databaseHelper.getContactID(mContact);

                int contactID = -1;
                while(cursor.moveToNext()) {
                    contactID = cursor.getInt(0);
                }
                if(contactID > -1) {
                    mContact.setName(mName.getText().toString());
                    mContact.setPhonenumber(mPhoneNumber.getText().toString());
                    mContact.setType(mSelectContacttype.getSelectedItem().toString());
                    mContact.setEmail(mEmail.getText().toString());

                    databaseHelper.updateContact(mContact, contactID);
                    Toast.makeText(getActivity(), "Contacto actualizado", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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

    private void init() {
        mPhoneNumber.setText(mContact.getPhonenumber());
        mName.setText(mContact.getName());
        mEmail.setText(mContact.getEmail());

        /*Modificando el tipo de contacto del spinner*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.contacttype_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectContacttype.setAdapter(adapter);
        int position = adapter.getPosition(mContact.getType());
        mSelectContacttype.setSelection(position);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.contact_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
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
}

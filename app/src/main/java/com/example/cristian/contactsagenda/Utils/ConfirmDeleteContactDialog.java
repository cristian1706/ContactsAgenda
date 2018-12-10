package com.example.cristian.contactsagenda.Utils;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristian.contactsagenda.R;
import com.example.cristian.contactsagenda.models.Contact;

public class ConfirmDeleteContactDialog extends DialogFragment {

    private Contact mContact;


    @SuppressLint("ValidFragment")
    public ConfirmDeleteContactDialog(Contact mContact) {
        this.mContact = mContact;
    }

    public ConfirmDeleteContactDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_confirmdelete, container, false);


        /*Presiono confirmar*/
        TextView confirmDialog = (TextView) view.findViewById(R.id.dialogConfirmDelete);
        confirmDialog.setOnClickListener((v) -> {

            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
            Cursor cursor = databaseHelper.getContactID(mContact);

            int contactID = -1;
            while (cursor.moveToNext()) {
                contactID = cursor.getInt(0);
            }
            if (contactID > -1) {
                if (databaseHelper.deleteContact(contactID) > 0) {
                    Toast.makeText(getActivity(), "Contacto borrado", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();

                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

        });


        /*Presiono cancelar*/
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancelDelete);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }


}

package com.example.cristian.contactsagenda;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cristian.contactsagenda.Utils.ContactListAdapter;
import com.example.cristian.contactsagenda.Utils.DatabaseHelper;
import com.example.cristian.contactsagenda.models.Contact;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ViewContactsFragment extends Fragment {
    private static final String TAG = "ViewContactsFragment";

    public interface OnContactSelectedListener {
        public void OnContactSelected(Contact con);
    }
    OnContactSelectedListener mContactListener;

    public interface OnAddContactListener {
        public void onAddContact();
    }
    OnAddContactListener mOnAddContact;

    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout viewContactsBar, searchBar;
    private ContactListAdapter adapter;
    private ListView contactsList;
    private EditText mSearchContacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewcontacts, container, false);
        viewContactsBar = (AppBarLayout) view.findViewById(R.id.viewContactsToolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.searchToolbar);
        contactsList = (ListView) view.findViewById(R.id.contactsList);
        mSearchContacts = (EditText) view.findViewById(R.id.etSearchContacts);

        setAppBarState(STANDARD_APPBAR);

        setupConctactsList();

        FloatingActionButton fab = view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: FloatingActionBottom clickeado");
                mOnAddContact.onAddContact();
            }
        });

        ImageView ivSearchContact = (ImageView) view.findViewById(R.id.ivSearchIcon);
        ivSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Search icon clickeado");
                toogleToolbarState();
            }
        });

        ImageView ivBackarrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Backarrow clickeado");
                toogleToolbarState();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContactListener = (OnContactSelectedListener) getActivity();
            mOnAddContact = (OnAddContactListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException" + e.getMessage());;
        }
    }

    private void setupConctactsList() {
        final ArrayList<Contact> contacts = new ArrayList<>();
//        contacts.add(new Contact("Ezequiel Scafati", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Franco Rubinachi", "(221) 602-0066", "cristianbarretof@gmail.com", "Otro"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));
//        contacts.add(new Contact("Cristian Barreto", "(221) 602-0066", "cristianbarretof@gmail.com", "Familiar"));

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getAllContacts();

        if(!cursor.moveToNext()) {
            Toast.makeText(getActivity(), "No hay contactos para mostrar", Toast.LENGTH_SHORT).show();
        }
        while(cursor.moveToNext()) {
            contacts.add(new Contact(
                    cursor.getString(1), //name
                    cursor.getString(2), //phone number
                    cursor.getString(3), //email
                    cursor.getString(4) //type
            ));
        }

        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        adapter = new ContactListAdapter(getActivity(), R.layout.contactslistitem, contacts);

        mSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchContacts.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        contactsList.setAdapter(adapter);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                /*pasandole el contacto a la interfaz y mandandoselo a MainActivity*/
                mContactListener.OnContactSelected(contacts.get(position));


            }
        });
    }

    private void toogleToolbarState() {
        if (mAppBarState == STANDARD_APPBAR) {
            setAppBarState(SEARCH_APPBAR);
        }
        else {
            setAppBarState(STANDARD_APPBAR);
        }
    }

    private void setAppBarState(int state) {
        mAppBarState = state;
        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);
            View view = getView();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (NullPointerException e) {
                Log.d(TAG, "setAppBarState: NullPointerException: " + e.getMessage());
            }
        }
        else if (mAppBarState == SEARCH_APPBAR) {
            viewContactsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
    }
}
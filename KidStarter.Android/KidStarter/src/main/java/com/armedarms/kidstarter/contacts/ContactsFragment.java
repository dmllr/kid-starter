package com.armedarms.kidstarter.contacts;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armedarms.kidstarter.App;
import com.armedarms.kidstarter.R;
import com.armedarms.kidstarter.model.Contact;
import com.armedarms.kidstarter.model.Contacts;
import com.armedarms.kidstarter.utils.IHasParentalContent;

public class ContactsFragment extends Fragment implements IHasParentalContent {

    private RecyclerView listView;
    private View fabAddContact;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (RecyclerView)view.findViewById(android.R.id.list);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(new ContactsAdapter(getActivity()));
        setLayoutManager();

        fabAddContact = view.findViewById(R.id.fabAddContact);
        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLayoutManager();
    }

    private void addContact() {
        new ContactDialog(getActivity(), new Contact())
                .canRemove(false)
                .withListener(new ContactDialog.ResultListener() {
                    @Override
                    public void onContactDialogInsert(Contact contact) {
                        Contacts.add(contact);
                        listView.getAdapter().notifyItemInserted(Contacts.all().size());
                    }

                    @Override
                    public void onContactDialogRemove(Contact contact) {

                    }
                })
                .show();

    }

    @Override
    public void onParentalToggled() {
        fabAddContact.setVisibility(App.getApp().isUserParent ? View.VISIBLE : View.GONE);
    }

    private void setLayoutManager() {
        int iDisplayWidth = Math.max(320, getResources().getDisplayMetrics().widthPixels);
        int spanCount = Math.max(1, iDisplayWidth / 399);

        listView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
    }

}

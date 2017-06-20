package com.armedarms.kidstarter.model;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.armedarms.kidstarter.App;
import com.armedarms.kidstarter.R;

import java.util.ArrayList;
import java.util.List;

public class Contacts {
    public static List<Contact> cacheContacts;

    public static List<Contact> all() {
        if (cacheContacts == null)
            cacheContacts = new Select().from(Contact.class).execute();
        if (cacheContacts == null)
            cacheContacts = new ArrayList<>();
        if (cacheContacts.isEmpty())
            initialAdd();

        return cacheContacts;
    }

    private static void initialAdd() {
        Contact c = new Contact();
        c.no = 1;
        c.name = App.getAppContext().getString(R.string.mama);
        c.isEmergency = true;
        c.avatarPath = "mama";
        c.save();

        cacheContacts.add(c);

        c = new Contact();
        c.no = 2;
        c.name = App.getAppContext().getString(R.string.papa);
        c.isEmergency = true;
        c.avatarPath = "papa";
        c.save();

        cacheContacts.add(c);
    }

    public static void add(Contact contact) {
        contact.no = cacheContacts.size() + 1;
        cacheContacts.add(contact);
        contact.save();
    }

    public static void remove(Contact contact) {
        cacheContacts.remove(contact);
        contact.delete();
    }
}

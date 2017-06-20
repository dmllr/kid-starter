package com.armedarms.kidstarter.model;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.armedarms.kidstarter.App;
import com.armedarms.kidstarter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppAvailabilities {
    public static List<String> cacheAvailabilities;

    public static List<String> all() {
        if (cacheAvailabilities == null) {
            List<AppAvailability> avails = new Select().from(AppAvailability.class).orderBy("APPID").execute();
            if (avails != null) {
                cacheAvailabilities = new ArrayList<>(avails.size());
                for (AppAvailability a : avails)
                    cacheAvailabilities.add(a.id);
            }
        }
        if (cacheAvailabilities == null)
            cacheAvailabilities = new ArrayList<>();

        return cacheAvailabilities;
    }

    public static void add(String id) {
        AppAvailability a = new AppAvailability();
        a.id = id;
        cacheAvailabilities.add(id);
        a.save();
    }

    public static void remove(String id) {
        AppAvailability a = new Select().from(AppAvailability.class).where("APPID = '" + id + "'").executeSingle();
        if (a != null) {
            cacheAvailabilities.remove(id);
            a.delete();
        }
    }
}

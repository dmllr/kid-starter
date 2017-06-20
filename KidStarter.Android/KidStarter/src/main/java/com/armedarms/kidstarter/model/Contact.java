package com.armedarms.kidstarter.model;

import android.graphics.Bitmap;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.UUID;

@Table(name = "Contacts")
public class Contact extends Model {
    @Column(name = "UUID")
    public UUID uuid;

    @Column(name = "No")
    public int no;

    @Column(name = "Name")
    public String name;

    @Column(name = "PhoneNumber")
    public String phoneNumber;

    @Column(name = "IsEmergency")
    public boolean isEmergency;

    @Column(name = "AvatarPath")
    public String avatarPath;

    public Contact() {
        super();
        uuid = UUID.randomUUID();
    }
}

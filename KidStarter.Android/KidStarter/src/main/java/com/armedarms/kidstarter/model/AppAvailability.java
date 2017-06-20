package com.armedarms.kidstarter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "AppAvailabilities")
public class AppAvailability extends Model {
    @Column(name = "APPID")
    public String id;
}

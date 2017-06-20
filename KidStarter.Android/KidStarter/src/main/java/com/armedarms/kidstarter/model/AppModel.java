package com.armedarms.kidstarter.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

public class AppModel {
    private final Context context;
    private final ApplicationInfo applicationInfo;

    private boolean mMounted;
    private final File apkFile;

    private String label;
    private Drawable icon;

    public AppModel(Context context, ApplicationInfo applicationInfo) {
        this.context = context;
        this.applicationInfo = applicationInfo;
        apkFile = new File(applicationInfo.sourceDir);
    }

    public void loadLabel(Context context) {
        if (label == null || !mMounted) {
            if (!apkFile.exists()) {
                mMounted = false;
                label = applicationInfo.packageName;
            } else {
                mMounted = true;
                CharSequence cs = applicationInfo.loadLabel(context.getPackageManager());
                this.label = (cs != null ? cs.toString() : applicationInfo.packageName);
            }
        }
    }

    public String getId() {
        return applicationInfo.packageName;
    }

    public String getLabel() {
        return label;
    }

    public Drawable getIconDrawable() {
        if (icon == null) {
            if (apkFile.exists()) {
                icon = applicationInfo.loadIcon(context.getPackageManager());
                return icon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            if (apkFile.exists()) {
                mMounted = true;
                icon = applicationInfo.loadIcon(context.getPackageManager());
                return icon;
            }
        } else {
            return icon;
        }

        return context.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
    }
}

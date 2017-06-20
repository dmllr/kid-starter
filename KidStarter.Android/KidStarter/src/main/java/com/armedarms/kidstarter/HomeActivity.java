package com.armedarms.kidstarter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.armedarms.kidstarter.parental.ParentalFragment;
import com.armedarms.kidstarter.apps.AppsFragment;
import com.armedarms.kidstarter.contacts.ContactDialog;
import com.armedarms.kidstarter.contacts.ContactsFragment;
import com.armedarms.kidstarter.utils.IHasParentalContent;

import java.io.File;
import java.io.IOException;


public class HomeActivity extends FragmentActivity {

    private ParentalFragment fragmentParental;
    private ContactsFragment fragmentContacts;
    private AppsFragment fragmentApps;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentParental = new ParentalFragment();
        fragmentContacts = new ContactsFragment();
        fragmentApps = new AppsFragment();

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        pager.setCurrentItem(1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case ContactDialog.REQ_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (imageReturnedIntent!=null) {

                        File tempFile = getTempFile();

                        String filePath = Environment.getExternalStorageDirectory() + "/" + ContactDialog.TEMP_PHOTO_FILE;
                        System.out.println("path "+filePath);


                        Bitmap selectedImage =  BitmapFactory.decodeFile(filePath);

                        if (ContactDialog.getInstance() != null)
                            ContactDialog.getInstance().setAvatar(selectedImage);

                        if (tempFile.exists())
                            tempFile.delete();
                    }
                }
        }
    }

    private File getTempFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File file = new File(Environment.getExternalStorageDirectory(), ContactDialog.TEMP_PHOTO_FILE);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file;
        } else {

            return null;
        }
    }

    public void toggleParental() {
        App.getApp().isUserParent = !App.getApp().isUserParent;

        fragmentContacts.onParentalToggled();
        fragmentApps.onParentalToggled();
    }

    public void toSettings() {
        pager.setCurrentItem(0);
    }

    public void toContacts() {
        pager.setCurrentItem(1);
    }

    public void toApps() {
        pager.setCurrentItem(2);
    }

    public class HomePagerAdapter extends FragmentPagerAdapter {
        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return fragmentParental;
                case 1:
                    return fragmentContacts;
                case 2:
                    return fragmentApps;
                default:
                    throw new IllegalStateException("HomePagerAdapter.getItem has unreferenced position");
            }
        }

        @Override
        public float getPageWidth(int position) {
            return 1f;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

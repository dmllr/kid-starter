package com.armedarms.kidstarter.parental;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.armedarms.kidstarter.App;
import com.armedarms.kidstarter.HomeActivity;
import com.armedarms.kidstarter.R;
import com.armedarms.kidstarter.contacts.ContactDialog;
import com.armedarms.kidstarter.model.Contact;
import com.armedarms.kidstarter.model.Contacts;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ParentalFragment extends Fragment implements View.OnClickListener {

    private TextView toggleParentalText;
    private View viewDimmer;
    private AdView adView;

    public ParentalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parental, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toggleParentalText = (TextView)view.findViewById(R.id.toggleChildParentalText);
        viewDimmer = view.findViewById(R.id.viewDimmer);
        adView = (AdView)view.findViewById(R.id.adView);

        toggleParentalText.setOnClickListener(this);
        view.findViewById(R.id.viewToContacts).setOnClickListener(this);
        view.findViewById(R.id.viewToApps).setOnClickListener(this);
        view.findViewById(R.id.viewSwitchToKidMode).setOnClickListener(this);
        view.findViewById(R.id.viewClearDefaults).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshControls();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.viewToContacts)
            ((HomeActivity)getActivity()).toContacts();
        if (id == R.id.viewToApps)
            ((HomeActivity)getActivity()).toApps();
        if (id == R.id.viewClearDefaults)
            clearDefaults();
        if (id == R.id.toggleChildParentalText) {
            if (App.getApp().isUserParent)
                toggleParental();
            else
                checkAdult();
        }
        if (id == R.id.viewSwitchToKidMode)
            toggleParental();
    }

    private void toggleParental() {
        ((HomeActivity) getActivity()).toggleParental();
        refreshControls();
    }

    private void checkAdult() {
        new CheckDialog(getActivity())
                .withListener(new CheckDialog.ResultListener() {
                    @Override
                    public void onCheckPassed() {
                        toggleParental();
                    }
                })
                .show();
    }

    private void refreshControls() {
        if (isAdded()) {
                toggleParentalText.setCompoundDrawablesWithIntrinsicBounds(((App) getActivity().getApplication()).isUserParent ? R.drawable.ic_kid : R.drawable.ic_parent, 0, 0, 0);
            toggleParentalText.setText(((App) getActivity().getApplication()).isUserParent ? R.string.switch_to_kid : R.string.switch_to_parental);
            viewDimmer.setVisibility(((App) getActivity().getApplication()).isUserParent ? View.GONE : View.VISIBLE);
            adView.setVisibility(((App) getActivity().getApplication()).isUserParent ? View.VISIBLE : View.GONE);

            if (adView.getVisibility() == View.VISIBLE) {
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        }
    }

    private void clearDefaults() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String currentHomePackage = resolveInfo.activityInfo.packageName;

        if (!currentHomePackage.equals(getActivity().getPackageName())) {
            showInstalledAppDetails(currentHomePackage);
            Toast.makeText(getActivity(), R.string.toast_already_different_launcher, Toast.LENGTH_LONG).show();
            return;
        }

        getActivity().getPackageManager().clearPackagePreferredActivities(getActivity().getPackageName());

        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

    public void showInstalledAppDetails(String packageName) {
        final int apiLevel = Build.VERSION.SDK_INT;
        Intent intent = new Intent();

        if (apiLevel >= 9) {
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
        } else {
            final String appPkgName = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");

            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra(appPkgName, packageName);
        }

        startActivity(intent);
    }

}

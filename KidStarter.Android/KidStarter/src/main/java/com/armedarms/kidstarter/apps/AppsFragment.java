package com.armedarms.kidstarter.apps;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armedarms.kidstarter.App;
import com.armedarms.kidstarter.HomeActivity;
import com.armedarms.kidstarter.R;
import com.armedarms.kidstarter.contacts.ContactDialog;
import com.armedarms.kidstarter.model.AppModel;
import com.armedarms.kidstarter.model.Contact;
import com.armedarms.kidstarter.model.Contacts;
import com.armedarms.kidstarter.utils.IHasParentalContent;

import java.util.ArrayList;

public class AppsFragment extends Fragment implements IHasParentalContent, LoaderManager.LoaderCallbacks<ArrayList<AppModel>> {

    private RecyclerView listView;
    private AppsAdapter adapter;
    private View emptyView;

    public AppsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (RecyclerView)view.findViewById(android.R.id.list);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter = new AppsAdapter(getActivity()));
        setLayoutManager();

        emptyView = view.findViewById(android.R.id.empty);

        view.findViewById(R.id.switchToSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).toSettings();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
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

    @Override
    public Loader<ArrayList<AppModel>> onCreateLoader(int id, Bundle args) {
        return new AppsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AppModel>> loader, ArrayList<AppModel> data) {
        adapter.setData(data);

        setupEmptyView();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AppModel>> loader) {
        adapter.setData(null);
        setupEmptyView();
    }

    @Override
    public void onParentalToggled() {
        adapter.notifyDataSetChanged();
        setupEmptyView();
    }

    private void setLayoutManager() {
        int iDisplayWidth = Math.max(320, (int)(getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density));
        int spanCount = Math.max(1, iDisplayWidth / 149);

        listView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
    }

    private void setupEmptyView() {
        emptyView.setVisibility(listView.getAdapter().getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}

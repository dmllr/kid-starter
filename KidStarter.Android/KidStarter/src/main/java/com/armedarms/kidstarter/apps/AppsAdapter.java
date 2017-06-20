package com.armedarms.kidstarter.apps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armedarms.kidstarter.App;
import com.armedarms.kidstarter.R;
import com.armedarms.kidstarter.model.AppAvailabilities;
import com.armedarms.kidstarter.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

    private ArrayList<AppModel> apps;
    private ArrayList<AppModel> availableApps;

    private final Context context;
    private LayoutInflater inflater;

    public AppsAdapter(Context context) {
        super();
        this.context = context;
    }

    public ArrayList<AppModel> getData() {

        return (App.getApp().isUserParent ? apps : availableApps);
    }

    public void setData(ArrayList<AppModel> apps) {
        this.apps = apps;

        if (apps != null) {
            List<String> avails = AppAvailabilities.all();
            availableApps = new ArrayList<>();
            for (AppModel m : apps) {
                if (avails.contains(m.getId()))
                    availableApps.add(m);
            }
        } else {
            availableApps = null;
        }

        notifyDataSetChanged();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return new AppViewHolder(inflater.inflate(R.layout.item_app, parent, false));
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        AppModel a = getData().get(position);

        holder.text.setText(a.getLabel());
        holder.icon.setImageDrawable(a.getIconDrawable());

        if (App.getApp().isUserParent) {
            if (AppAvailabilities.all().contains(a.getId()))
                holder.iconSelected.setVisibility(View.VISIBLE);
            else
                holder.iconSelected.setVisibility(View.GONE);
        }

        holder.itemView.setTag(a);
    }

    @Override
    public int getItemCount() {
        if (getData() == null)
            return 0;

        return getData().size();
    }

    class AppViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView icon;
        ImageView iconSelected;

        public AppViewHolder(final View view) {
            super(view);

            text = (TextView)view.findViewById(android.R.id.text1);
            icon = (ImageView) view.findViewById(android.R.id.icon);
            iconSelected = (ImageView) view.findViewById(R.id.iconSelected);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppModel a = (AppModel) view.getTag();
                    if (App.getApp().isUserParent)
                        toggleAppAvailability(a);
                    else
                        startApp(a);
                }
            });
        }
    }

    private void toggleAppAvailability(AppModel a) {
        if (!AppAvailabilities.all().contains(a.getId()))
            AppAvailabilities.add(a.getId());
        else
            AppAvailabilities.remove(a.getId());

        int position = getData().indexOf(a);
        if (position > -1)
            notifyItemChanged(position);
    }

    private void startApp(AppModel a) {
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(a.getId());
        context.startActivity(LaunchIntent);
    }

}

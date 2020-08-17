package com.chipfc.android.things.athome;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private static final String TAG = AppAdapter.class.getSimpleName();
    final private List<App> appList;

    AppAdapter(List<App> appList) {
        this.appList = appList;
    }

    @Override
    public AppAdapter.AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.app_row, parent, false));
    }

    @Override
    public void onBindViewHolder(AppAdapter.AppViewHolder holder, int position) {
        holder.image.setImageDrawable(appList.get(position).getIcon());
        holder.appName.setText(appList.get(position).getName());
        holder.packageName.setText(appList.get(position).getPackageName());
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView appName;
        final TextView packageName;

        AppViewHolder(final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.app_icon_id);
            appName = itemView.findViewById(R.id.text_app_name_id);
            packageName = itemView.findViewById(R.id.text_package_name_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Click on package: " + packageName.getText());

                    try {
                        Intent LaunchIntent = itemView.getContext().getPackageManager().getLaunchIntentForPackage(packageName.getText().toString());
                        itemView.getContext().startActivity(LaunchIntent);

                    } catch (Exception ex) {
                        Log.e(TAG, "Can not launch package " + packageName.getText() + ". " + ex.getMessage());
                    }

                }
            });
        }
    }
}

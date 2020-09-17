package com.ciffelia.mutecamera.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.ciffelia.mutecamera.Preferences
import com.ciffelia.mutecamera.R
import com.ciffelia.mutecamera.installedapp.InstalledApp


class AppRecyclerViewAdapter(
    private val appList: List<InstalledApp>,
    private val preferences: Preferences
) : RecyclerView.Adapter<AppRecyclerViewAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIconView: ImageView = itemView.findViewById(R.id.appIcon)
        val appNameView: TextView = itemView.findViewById(R.id.appName)
        val appPackageNameView: TextView = itemView.findViewById(R.id.appPackageName)
        val appEnableSwitch: SwitchCompat = itemView.findViewById(R.id.appEnableSwitch)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_list_item, parent, false)

        return ViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = appList[position]

        holder.appIconView.setImageDrawable(app.icon)
        holder.appNameView.text = app.name
        holder.appPackageNameView.text = app.packageName
        holder.appEnableSwitch.isChecked = preferences.isAppEnabled(app.packageName)

        holder.appEnableSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.setAppEnabled(app.packageName, isChecked)
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.appEnableSwitch.setOnCheckedChangeListener(null)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = appList.size
}

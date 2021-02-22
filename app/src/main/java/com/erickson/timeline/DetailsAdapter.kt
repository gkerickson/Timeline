package com.erickson.timeline

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.TimelineDataModel
import com.erickson.timeline.model.ViewData

class DetailsAdapter(private val selected: LiveData<ActiveViewData>) :
    RecyclerView.Adapter<DetailViewFragment.NoteViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewFragment.NoteViewHolder {
        Log.e("GALEN", "Trying to onCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.artwork_detail_layout, parent, false)
        return DetailViewFragment.NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewFragment.NoteViewHolder, position: Int) {
        Log.e("GALEN", "Trying to bind view holder")
        if (itemCount == 0) {
            return
        }
        selected.value?.viewData?.notes?.getOrNull(position)
            .apply {
                holder.labelView.text = this?.label
                holder.contentView.text = this?.content
            }
    }

    override fun getItemCount(): Int {
        return (selected.value?.viewData?.notes?.size ?: 0).also {
            Log.e("GALEN", "Item Count: $it")
        }
    }
}
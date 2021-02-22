package com.erickson.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickson.timeline.model.TimelineDataModel

class DetailsAdapter(private val viewModel: TimelineDataModel) :
    RecyclerView.Adapter<DetailViewFragment.NoteViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewFragment.NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.artwork_detail_layout, parent, false)
        return DetailViewFragment.NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewFragment.NoteViewHolder, position: Int) {
        if (itemCount == 0) {
            return
        }
        viewModel.getSelected().value?.viewData?.notes?.get(position)
            .apply {
                holder.labelView.text = this?.label
                holder.contentView.text = this?.content
            }
    }

    override fun getItemCount(): Int {
        return viewModel.getSelected().value?.viewData?.notes?.size ?: 0
    }
}
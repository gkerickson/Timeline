package com.erickson.timeline

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.erickson.timeline.model.ActiveViewData

class DetailsAdapter(
    private val selected: LiveData<ActiveViewData>,
    private val shouldHideDates: LiveData<Boolean>
) :
    RecyclerView.Adapter<DetailViewFragment.NoteViewHolder>() {

    private fun hideDates(input: String): String {
        return Regex("\\d").replace(String(input.toCharArray()), "*")
    }

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
        selected.value?.viewData?.notes?.getOrNull(position)
            .apply {
                holder.labelView.text = this?.label
                this?.content?.let {
                    holder.contentView.text =
                        if (shouldHideDates.value == true) hideDates(it)
                        else it
                }
            }
    }

    override fun getItemCount(): Int {
        return (selected.value?.viewData?.notes?.size ?: 0)
    }
}
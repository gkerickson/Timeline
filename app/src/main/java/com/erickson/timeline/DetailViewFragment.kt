package com.erickson.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DetailViewFragment : Fragment() {
    companion object {
        val ARG_LIST_INDEX = "TEST"
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelView: TextView = itemView.findViewById(R.id.detail_label)
        var contentView: TextView = itemView.findViewById(R.id.detail)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_detail_view, container, false)

        val viewModel: DataViewModel by activityViewModels()
        val activeItem = viewModel.lastViewDataPress

        if (savedInstanceState == null) {
            container?.findViewById<RecyclerView>(R.id.recycler_view)
                ?.also { recyclerView ->
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = object : RecyclerView.Adapter<NoteViewHolder>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): NoteViewHolder {
                            val view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.artwork_detail_layout, parent, false)
                            return NoteViewHolder(view)
                        }

                        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
                            if (itemCount == 0) return Unit
                            viewModel.allViewData.value?.get(activeItem)?.notes?.get(position)
                                ?.apply {
                                    holder.labelView.text = this.label
                                    holder.contentView.text = this.content
                                }
                        }

                        override fun getItemCount(): Int {
                            val count = viewModel.allViewData.value?.run {
                                if (size > 0) get(activeItem).notes.size
                                else 0
                            } ?: 0
                            return count
                        }
                    }
                }
        }

        viewModel.allViewData.observe(this.viewLifecycleOwner) {
            container?.findViewById<RecyclerView>(R.id.recycler_view)?.adapter?.notifyDataSetChanged()
        }
        return view
    }
}
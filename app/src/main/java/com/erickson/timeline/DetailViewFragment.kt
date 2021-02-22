package com.erickson.timeline

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickson.timeline.model.DataViewModel

class DetailViewFragment : Fragment() {
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
        Log.e("GALEN", "Creating view")

        if (savedInstanceState == null) {
            Log.e("GALEN", "STATE WAS NULL")
            view?.findViewById<RecyclerView>(R.id.recycler_view)
                ?.also { recyclerView ->
                    Log.e("GALEN", "Preping recycler view")
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = DetailsAdapter(viewModel.selected)
                }

            viewModel.selected.observe(this.viewLifecycleOwner) {
                view.findViewById<ImageView>(R.id.detail_image).setImageBitmap(it.bitmap)
                view?.findViewById<RecyclerView>(R.id.recycler_view)?.adapter?.notifyDataSetChanged()
            }
        }
        return view
    }
}

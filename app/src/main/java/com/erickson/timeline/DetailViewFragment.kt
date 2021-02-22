package com.erickson.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickson.timeline.model.DataViewModel
import java.util.*

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

        view.findViewById<Button>(R.id.confirm_button).setOnClickListener {
            ChoiceDialogFragment().show(parentFragmentManager, null)
        }

        if (savedInstanceState == null) {
            view?.findViewById<RecyclerView>(R.id.recycler_view)
                ?.also { recyclerView ->
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter =
                        DetailsAdapter(viewModel.selected, viewModel.shouldShowButton)
                }

            viewModel.shouldShowButton.observe(this.viewLifecycleOwner) {
                view.findViewById<Button>(R.id.confirm_button).apply {
                    visibility = if (it) VISIBLE else GONE
                    invalidate()
                }
                view.findViewById<View>(R.id.include_date).apply {
                    visibility =
                        if (viewModel.shouldShowButton.value == true) View.GONE else View.VISIBLE
                    invalidate()
                }
            }

            viewModel.selected.observe(this.viewLifecycleOwner) {
                view.findViewById<ImageView>(R.id.detail_image).setImageBitmap(it.bitmap)
                view?.findViewById<RecyclerView>(R.id.recycler_view)?.adapter?.notifyDataSetChanged()
                view.findViewById<View>(R.id.include_date).apply {
                    findViewById<TextView>(R.id.detail).text = Calendar.getInstance().run {
                        time = it.viewData.date
                        get(Calendar.YEAR).toString()
                    }
                    findViewById<TextView>(R.id.detail_label).text = "Date"
                    invalidate()
                }
            }
        }
        return view
    }
}

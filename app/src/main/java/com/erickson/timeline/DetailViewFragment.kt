package com.erickson.timeline

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.DataViewModel

class DetailViewFragment : Fragment() {
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelView: TextView = itemView.findViewById(R.id.detail_label)
        var contentView: TextView = itemView.findViewById(R.id.detail)
    }

    lateinit var viewModel: DataViewModel

    private fun selectedMoreModern(): Boolean? {
        val selected = viewModel.selected
        val other: LiveData<ActiveViewData> =
            if (viewModel.selected.value?.viewData?.id ?: "NOT AN ID" ==
                viewModel.choiceOneViewData.value?.viewData?.id ?: "ALSO NOT AN ID"
            ) {
                viewModel.choiceTwoViewData
            } else
                viewModel.choiceOneViewData
        return if (other.value?.viewData?.date == null && selected.value?.viewData?.date == null) {
            null
        } else if (selected.value?.viewData?.date == null)
            false
        else if (other.value?.viewData?.date == null)
            true
        else
            selected.value?.viewData?.date!! > other.value?.viewData?.date!!
    }

    fun buttonOnPress() {
        val toCheck = selectedMoreModern()
        if(toCheck == true) {
            // Display congrats
        } else if(toCheck == false){
            // display so sorry
        }
        // Update viewModel
        viewModel.updateChoices()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_detail_view, container, false)
        val viewModel: DataViewModel by activityViewModels()
        this.viewModel = viewModel

        viewModel.shouldShowButton.observe(this.viewLifecycleOwner) {
            view.findViewById<Button>(R.id.confirm_button).visibility = if (it) VISIBLE else GONE
        }
        Log.e("GALEN", "Creating view")

        view.findViewById<Button>(R.id.confirm_button).setOnClickListener {
            buttonOnPress()
        }

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

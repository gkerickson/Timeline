package com.erickson.timeline

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.DataViewModel
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private fun onImageClick(listIndex: Int) {
        this.supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .add(R.id.fragment, DetailViewFragment())
            .commit()
    }

    private val imageIds = listOf(
        R.id.previewImage3,
        R.id.previewImage4,
        R.id.previewImage5,
        R.id.previewImage6,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Picasso.get().isLoggingEnabled = true

        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        fun setupImageObserver(liveData: LiveData<ActiveViewData>, view: ImageView) {
            liveData.observe(this) {
                Log.e("GALEN", "${it.viewData} with ${it.imageTarget != null} in ${view.id}")
                view.setImageBitmap(it.imageTarget)
                view.rootView.invalidate()
            }
        }

        for (i in 0 until 4)
            viewModel.timelineViewData.getOrNull(i)?.let {
                setupImageObserver(it, findViewById(imageIds[i]))
            }
        setupImageObserver(viewModel.choiceOneViewData, findViewById(R.id.selection_one))
        setupImageObserver(viewModel.choiceTwoViewData, findViewById(R.id.selection_two))

        findViewById<View>(R.id.timeline_layout).setOnClickListener {
            findViewById<Guideline>(R.id.main_guideline).apply {
                setGuidelinePercent(0.25F)
                rootView.invalidate()
            }
        }

        findViewById<View>(R.id.parent_layout).setOnClickListener {
            findViewById<Guideline>(R.id.main_guideline).apply {
                setGuidelinePercent(0.75F)
                rootView.invalidate()
            }
        }
    }
}
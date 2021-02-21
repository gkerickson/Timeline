package com.erickson.timeline

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.lifecycle.ViewModelProvider
import com.erickson.timeline.model.DataViewModel

class MainActivity : AppCompatActivity() {

    private fun onImageClick(listIndex: Int) {
        this.supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .add(R.id.fragment, DetailViewFragment())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        viewModel.timelineViewData.observe(this) { map ->
            findViewById<ImageView>(R.id.previewImage3).setImageBitmap(map[0].imageTarget.image)
            findViewById<ImageView>(R.id.previewImage4).setImageBitmap(map[1].imageTarget.image)
            findViewById<ImageView>(R.id.previewImage5).setImageBitmap(map[2].imageTarget.image)
            findViewById<ImageView>(R.id.previewImage6).setImageBitmap(map[3].imageTarget.image)
        }

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
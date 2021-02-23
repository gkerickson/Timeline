package com.erickson.timeline

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        val imageIds = listOf(
            R.id.timeline_image_1,
            R.id.timeline_image_2,
            R.id.timeline_image_3,
            R.id.timeline_image_4,
        )
    }

    private val viewModel: DataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    private val animator: AnimationDelegate by lazy {
        AnimationDelegate(this)
    }

    private fun onImageClick() {
        this.supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.slide_out
            )
            .add(R.id.fragment, DetailViewFragment())
            .commit()
    }

    private fun setupImageLoadIntoViewFromLiveData(
        liveData: LiveData<ActiveViewData>,
        view: ImageView,
        selectedId: MutableLiveData<String>
    ) {
        liveData.observe(this) {
            view.setImageBitmap(it.bitmap)
            view.rootView.invalidate()
        }
        view.isClickable = true
        view.setOnClickListener {
            liveData.value?.viewData?.id?.apply {
                selectedId.value = this
            }
            onImageClick()
        }
    }

    private fun setupImages() {
        for (i in 0 until 4)
            viewModel.timelineViewData.getOrNull(i)?.let {
                setupImageLoadIntoViewFromLiveData(it, findViewById(imageIds[i]), viewModel.selectedId)
            }
        setupImageLoadIntoViewFromLiveData(viewModel.choiceOneViewData, findViewById(R.id.selection_one), viewModel.selectedId)
        setupImageLoadIntoViewFromLiveData(viewModel.choiceTwoViewData, findViewById(R.id.selection_two), viewModel.selectedId)

        findViewById<View>(R.id.timeline_layout).setOnClickListener { animator.toTimelineFocus() }
        findViewById<View>(R.id.parent_layout).setOnClickListener { animator.toChoiceFocus() }

    }

    private fun setupTimelineYears() {
        viewModel.highestTime.observe(this) {
            findViewById<TextView>(R.id.topTime).apply {
                text = Calendar.getInstance().run {
                    this.time = it
                    get(Calendar.YEAR).toString()
                }
                invalidate()
            }
        }

        viewModel.lowestTime.observe(this) {
            findViewById<TextView>(R.id.bottomTime).apply {
                text = Calendar.getInstance().run {
                    this.time = it
                    get(Calendar.YEAR).toString()
                }
                invalidate()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupImages()
        setupTimelineYears()
        animator.toTimelineFocus()
    }
}
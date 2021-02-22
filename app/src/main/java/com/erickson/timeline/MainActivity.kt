package com.erickson.timeline

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.setPadding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.DataViewModel
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private fun onImageClick() {
        this.supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out,R.anim.slide_in, R.anim.slide_out)
            .add(R.id.fragment, DetailViewFragment())
            .commit()
    }

    private val imageIds = listOf(
        R.id.timeline_image_1,
        R.id.timeline_image_2,
        R.id.timeline_image_3,
        R.id.timeline_image_4,
    )

    private val timelineImageViews: List<ImageView> by lazy {
        imageIds.map { findViewById(it) }
    }

    private val choiceImageViews: List<ImageView> by lazy {
        listOf(
            findViewById(R.id.selection_one),
            findViewById(R.id.selection_two)
        )
    }


    // TODO: Use scenes to manage animation
    interface ToAnimate {
        val start: Float
        val end: Float
        fun setValue(value: Float)
    }
    private val animationList: List<ToAnimate> = listOf(
        object : ToAnimate {
            override val start: Float = 0.75f
            override val end: Float = 0.25f
            override fun setValue(value: Float) {
                (findViewById<Guideline>(R.id.main_guideline).layoutParams as ConstraintLayout.LayoutParams).apply {
                    guidePercent = value
                    findViewById<Guideline>(R.id.main_guideline).layoutParams = this
                }
            }
        },
        object : ToAnimate {
            override val start: Float = 0f
            override val end: Float = 10f
            override fun setValue(value: Float) {
                timelineImageViews.forEach {
                    it.setPadding(value.toInt())
                }
            }
        },
        object : ToAnimate {
            override val start: Float = 50f
            override val end: Float = 0f
            override fun setValue(value: Float) {
                choiceImageViews.forEach {
                    it.setPadding(value.toInt())
                }
            }
        }
    )
    private val animator = ValueAnimator.ofInt(0, 100).apply {
        duration = 1000
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { valueAnimator ->
            animationList.forEach {
                it.setValue(
                    (abs(it.start - it.end) *valueAnimator.animatedFraction).run {
                        if(it.start > it.end) it.start - this
                        else it.end - this
                    }
                )
            }
        }
    }
    private fun toTimelineFocus() {
        findViewById<View>(R.id.timeline_layout).apply {
            isClickable = false
        }
        timelineImageViews.forEach {
            it.isClickable = true
        }
        choiceImageViews.forEach {
            it.isClickable = false
        }
        findViewById<View>(R.id.parent_layout).apply {
            isClickable = true
        }
        animator.start()

    }
    private fun toChoiceFocus() {
        findViewById<View>(R.id.timeline_layout).apply {
            isClickable = true
        }
        timelineImageViews.forEach {
            it.isClickable = false
        }
        choiceImageViews.forEach {
            it.isClickable = true
        }
        findViewById<View>(R.id.parent_layout).apply {
            isClickable = false
        }
        animator.reverse()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Picasso.get().isLoggingEnabled = true

        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        fun setupImageObserver(liveData: LiveData<ActiveViewData>, view: ImageView) {
            liveData.observe(this) {
                Log.e("GALEN", "${it.viewData} with ${it.bitmap != null} in ${view.id}")
                view.setImageBitmap(it.bitmap)
                view.rootView.invalidate()
            }
            view.isClickable = true
            view.setOnClickListener {
                liveData.value?.viewData?.id?.apply {
                    viewModel.selectedId.value = this
                }
                onImageClick()
            }
        }

        for (i in 0 until 4)
            viewModel.timelineViewData.getOrNull(i)?.let {
                setupImageObserver(it, findViewById(imageIds[i]))
            }
        setupImageObserver(viewModel.choiceOneViewData, findViewById(R.id.selection_one))
        setupImageObserver(viewModel.choiceTwoViewData, findViewById(R.id.selection_two))
        findViewById<View>(R.id.timeline_layout).setOnClickListener { toTimelineFocus() }
        findViewById<View>(R.id.parent_layout).setOnClickListener { toChoiceFocus() }

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
        toTimelineFocus()
    }
}
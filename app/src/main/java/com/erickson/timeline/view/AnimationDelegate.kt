package com.erickson.timeline.view

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.setPadding
import com.erickson.timeline.MainActivity
import com.erickson.timeline.MainActivity.Companion.imageIds
import com.erickson.timeline.R
import kotlin.math.abs

class AnimationDelegate(
    private val activity: MainActivity
) {

    private val timelineImageViews: List<ImageView> by lazy {
        imageIds.map { activity.findViewById(it) }
    }

    private val choiceImageViews: List<ImageView> by lazy {
        listOf(
            activity.findViewById(R.id.selection_one),
            activity.findViewById(R.id.selection_two)
        )
    }

    fun toTimelineFocus() {
        activity.findViewById<View>(R.id.timeline_layout).apply {
            isClickable = false
        }
        timelineImageViews.forEach {
            it.isClickable = true
        }
        choiceImageViews.forEach {
            it.isClickable = false
        }
        activity.findViewById<View>(R.id.parent_layout).apply {
            isClickable = true
        }
        animator.start()
    }

    fun toChoiceFocus() {
        activity.findViewById<View>(R.id.timeline_layout).apply {
            isClickable = true
        }
        timelineImageViews.forEach {
            it.isClickable = false
        }
        choiceImageViews.forEach {
            it.isClickable = true
        }
        activity.findViewById<View>(R.id.parent_layout).apply {
            isClickable = false
        }
        animator.reverse()
    }

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
                (activity.findViewById<Guideline>(R.id.main_guideline).layoutParams as ConstraintLayout.LayoutParams).apply {
                    guidePercent = value
                    activity.findViewById<Guideline>(R.id.main_guideline).layoutParams = this
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
                    (abs(it.start - it.end) * valueAnimator.animatedFraction).run {
                        if (it.start > it.end) it.start - this
                        else it.end - this
                    }
                )
            }
        }
    }

}
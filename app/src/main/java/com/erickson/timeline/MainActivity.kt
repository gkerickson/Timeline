package com.erickson.timeline

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firstImage = this.findViewById<ImageView>(R.id.selection_one)
        val secImage = this.findViewById<ImageView>(R.id.selection_two)
        val viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        viewModel.potentialImages.observe(this) {
            it.subList(0, 6).sortedBy { viewData ->
                viewData.date
            }.apply {
                Picasso.get().load(this[0].imageUrl).into(firstImage)
                Picasso.get().load(this[1].imageUrl).into(secImage)
                Picasso.get().load(this[2].imageUrl).into(findViewById<ImageView>(R.id.previewImage3))
                Picasso.get().load(this[3].imageUrl).into(findViewById<ImageView>(R.id.previewImage4))
                Picasso.get().load(this[4].imageUrl).into(findViewById<ImageView>(R.id.previewImage5))
                Picasso.get().load(this[5].imageUrl).into(findViewById<ImageView>(R.id.previewImage6))
            }
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
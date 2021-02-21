package com.erickson.timeline

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Guideline
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

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

        viewModel.allViewData.observe(this) { list ->
            list.subList(0, 4).sortedBy { viewData ->
                viewData.date
            }.apply {
                Picasso.get().load(this[0].imageUrl)
                    .into(findViewById<ImageView>(R.id.previewImage3))
                Picasso.get().load(this[1].imageUrl)
                    .into(findViewById<ImageView>(R.id.previewImage4))
                Picasso.get().load(this[2].imageUrl)
                    .into(findViewById<ImageView>(R.id.previewImage5))
                Picasso.get().load(this[3].imageUrl)
                    .into(findViewById<ImageView>(R.id.previewImage6))
            }
            findViewById<ImageView>(R.id.selection_one).let { imageView ->
                Picasso.get().load(list[4].imageUrl).into(imageView)
                imageView.setOnClickListener {
                    viewModel.lastViewDataPress = 4
                    onImageClick(4)
                }
            }
            findViewById<ImageView>(R.id.selection_two).let { imageView ->
                Picasso.get().load(list[5].imageUrl).into(imageView)
                imageView.setOnClickListener {
                    viewModel.lastViewDataPress = 5
                    onImageClick(5)
                }
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
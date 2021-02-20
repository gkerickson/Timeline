package com.erickson.timeline

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
            Picasso.get().load(it[0].imageUrl).into(firstImage)
            Picasso.get().load(it[1].imageUrl).into(secImage)
            Picasso.get().load(it[2].imageUrl).into(this.findViewById<ImageView>(R.id.previewImage3))
            Picasso.get().load(it[3].imageUrl).into(this.findViewById<ImageView>(R.id.previewImage4))
            Picasso.get().load(it[4].imageUrl).into(this.findViewById<ImageView>(R.id.previewImage5))
            Picasso.get().load(it[5].imageUrl).into(this.findViewById<ImageView>(R.id.previewImage6))
        }
    }
}
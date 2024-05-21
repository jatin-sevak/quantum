package com.example.quantum
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quantum.adapters.ImageAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val imageUrls = (1..250).map {
            ImageData("https://source.unsplash.com/random/400x400?sig=$it")
        }

        val adapter = ImageAdapter(imageUrls)
        recyclerView.adapter = adapter
    }
}

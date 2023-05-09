package com.example.farm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.farm.databinding.ActivityPredictResultBinding

class PredictResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPredictResultBinding.inflate(layoutInflater)



        val predict = intent.getStringExtra("Predict")
        binding.tvPredictResult.text= "Croup You Can Plant is \n$predict !!"
        setContentView(binding.root)
    }
}
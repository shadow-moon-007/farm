package com.example.farm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.farm.daos.PostDao
import com.example.farm.databinding.ActivityCreatePostBinding

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postDao = PostDao()

        binding.postButton.setOnClickListener {
            val input = binding.postInput.text.toString().trim()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

    }
}
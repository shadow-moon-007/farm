package com.example.farm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.farm.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")


        auth = FirebaseAuth.getInstance()


        if (auth.currentUser == null) {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(Home(),lat,long)


        binding.bottomNavView.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.home -> replaceFragment(Home(),lat,long)
                R.id.predict -> replaceFragment(Predict(),null,null)
                R.id.chat -> replaceFragment(Chat(),null,null)
                R.id.setting -> replaceFragment(Setting(),null,null)

                else -> {
                }
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment,lat:String?,long:String?) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if(fragment==Home()){
            val bundle = Bundle()
            bundle.putString("lat",lat)
            bundle.putString("long",long)
            fragment.arguments = bundle
            fragmentTransaction.add(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
        }else {
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
        }

    }
}
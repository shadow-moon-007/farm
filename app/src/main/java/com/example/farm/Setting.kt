package com.example.farm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.farm.MainActivity.Companion.auth
import com.example.farm.databinding.FragmentSettingBinding

class Setting : Fragment() {
    private lateinit var bindingFragment: FragmentSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFragment = FragmentSettingBinding.inflate(inflater, container, false)

        bindingFragment.signOut.setOnClickListener {
            auth.signOut()
            bindingFragment.userDetails.text = updateData()
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
            activity?.finish()
        }
        return bindingFragment.root
    }
    override fun onResume() {
        super.onResume()
        bindingFragment.userDetails.text = updateData()
    }

    private fun updateData(): String{
        return "Email : ${auth.currentUser?.email}"
    }

}
package com.example.farm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.farm.databinding.FragmentPredictBinding
import org.json.JSONException
import org.json.JSONObject

class Predict : Fragment() {

    private lateinit var bindingFragment: FragmentPredictBinding

    var url = "https://mlapi-c8zv.onrender.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFragment = FragmentPredictBinding.inflate(inflater, container, false)

        var n = bindingFragment.etNitrogen
        var p = bindingFragment.etPotassium
        var k = bindingFragment.etPhosphorus
        var templrature = bindingFragment.etTemperature
        var humidity = bindingFragment.etHumidity
        var ph = bindingFragment.etPh
        var rainfall = bindingFragment.etRainfall

        val jsonObject = JSONObject()


        bindingFragment.btnPredict.setOnClickListener {

            jsonObject.put("N", n.text.toString().toInt())
            jsonObject.put("P", p.text.toString().toInt())
            jsonObject.put("K", k.text.toString().toInt())
            jsonObject.put("temperature", templrature.text.toString().toInt())
            jsonObject.put("humidity", humidity.text.toString().toInt())
            jsonObject.put("ph", ph.text.toString().toInt())
            jsonObject.put("rainfall", rainfall.text.toString().toInt())


            val queue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                { response ->
                    try {
                        // Handle the response
                        val message = response.getString("Predction")
//                        bindingFragment.textView2.text = message
                        val intent = Intent(requireContext(), PredictResultActivity::class.java)
                        intent.putExtra("Predict", message)
                        startActivity(intent)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        val toast = Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG)
                        toast.show()
                        // Handle error here
                    }
                },
                { error ->
                    val toast = Toast.makeText(requireContext(), "${error.localizedMessage}", Toast.LENGTH_LONG)
                        toast.show()
                }
            )
            queue.add(request)
        }
        return bindingFragment.root
    }

//    private fun setResult(response: JSONObject?) {
//        bindingFragment.textView2.text = response?.getJSONObject("Predction").toString()
//        val toast = Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_LONG)
//        toast.show()
//    }


}
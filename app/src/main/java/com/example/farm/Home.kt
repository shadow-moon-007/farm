package com.example.farm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.farm.databinding.FragmentHomeBinding
import org.json.JSONObject
import java.lang.Math.ceil

class Home : Fragment() {

    private lateinit var bindingFragment: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingFragment = FragmentHomeBinding.inflate(inflater, container, false)

        val lat = arguments?.getString("lat")
        val long = arguments?.getString("long")

//        val toast = Toast.makeText(requireContext(), " hey, $lat, $long", Toast.LENGTH_LONG)
//        toast.show()

        // getJsonData(lat,long)
        getJsonData("28.7311596","77.1328766")
        return bindingFragment.root
    }

    private fun getJsonData(lat:String?,long:String?)
    {
        val API_KEY="70990f6ef1c1a7b3c5d16f6f802c7fbc"
        val queue = Volley.newRequestQueue(requireContext())
        val url ="https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(requireContext(),"ERROR",Toast.LENGTH_LONG).show() })


        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject){
        bindingFragment.city.text=response.getString("name")
        var lat = response.getJSONObject("coord").getString("lat")
        var long=response.getJSONObject("coord").getString("lon")
        bindingFragment.coordinates.text="${lat} , ${long}"
        bindingFragment.weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr=response.getJSONObject("main").getString("temp")
        tempr=((((tempr).toFloat()-273.15)).toInt()).toString()
        bindingFragment.temp.text="${tempr}°C"


        var mintemp=response.getJSONObject("main").getString("temp_min")
        mintemp=((((mintemp).toFloat()-273.15)).toInt()).toString()
        bindingFragment.minTemp.text=mintemp+"°C"
        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
        bindingFragment.maxTemp.text=maxtemp+"°C"

        bindingFragment.pressure.text=response.getJSONObject("main").getString("pressure")
        bindingFragment.humidity.text=response.getJSONObject("main").getString("humidity")+"%"
        bindingFragment.wind.text=response.getJSONObject("wind").getString("speed")
        bindingFragment.degree.text="Degree : "+response.getJSONObject("wind").getString("deg")+"°"
//        gust.text="Gust : "+response.getJSONObject("wind").getString("gust")
    }

}
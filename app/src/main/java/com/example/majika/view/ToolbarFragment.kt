package com.example.majika.view

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.majika.R

private const val ARG_PARAM1 = "param1"

class ToolbarFragment : Fragment(), SensorEventListener {
    private var fragmentName: String? = null
    private lateinit var sensorManager: SensorManager
    private var temperature: Sensor? = null

    companion object {
        @JvmStatic
        fun newInstance(fragmentName: String) =
            ToolbarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, fragmentName)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentName = it.getString(ARG_PARAM1)
        }

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        } else {
            Log.d("NoSensor", "No Sensor Found");
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentName?.let { Log.d("FragmentName", it) }
        // Inflate the layout for this fragment
        val customLayout = when (fragmentName) {
            "Menu" -> {
                R.layout.fragment_toolbar_menu
            }
            "Payment" -> R.layout.fragment_toolbar_payment
            else -> R.layout.fragment_toolbar_normal
        }

        return inflater.inflate(customLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.action_bar_title).text = fragmentName

        if (fragmentName == "Payment") {
            view.findViewById<ImageButton>(R.id.action_bar_back).setOnClickListener {
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (fragmentName == "Menu") {
            view?.findViewById<TextView>(R.id.temperature_TV)?.text = "${event?.values?.get(0).toString()} C"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }
}
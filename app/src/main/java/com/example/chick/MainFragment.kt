package com.example.chick

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat

class MainFragment : Fragment() {

    lateinit var btnPlus : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //var view = LayoutInflater.from(activity).inflate(R.layout.fragment_main, container, false)

        /*
        btnPlus = th.findViewById(R.id.btnDelete)

        btnPlus.setOnClickListener{
            val intent = Intent(activity, EditAlarmActivity::class.java)
            startActivity(intent)
        }
*/
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)

        //return view
    }


}
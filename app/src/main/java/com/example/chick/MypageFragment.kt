package com.example.chick

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MypageFragment : Fragment() {
    lateinit var btnhelper : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_mypage, container, false)

        //바인딩
        btnhelper = view.findViewById(R.id.textView4)

        //도움말클릭시 화면 넘어가기
        btnhelper.setOnClickListener{
            val intent = Intent(activity, HelperActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}
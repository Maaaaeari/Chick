package com.example.chick

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast


class MypageFragment : Fragment() {

    lateinit var switchAlaram : Switch
    lateinit var txtAlramOnOff : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_mypage, container, false)

        // 바인딩
        switchAlaram = view.findViewById(R.id.switchAlaram)
        txtAlramOnOff = view.findViewById(R.id.txtAlramOnOff)

        // 스위치 체크 상태 변화
        switchAlaram.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked){
                Toast.makeText(context, "약 알람이 켜졌어요.", Toast.LENGTH_SHORT).show()
                txtAlramOnOff.text = "현재 전체 약 알람이 켜져있어요."
            }else{
                Toast.makeText(context, "약 알람이 꺼졌어요.", Toast.LENGTH_SHORT).show()
                txtAlramOnOff.text = "현재 전체 약 알람이 꺼져있어요."
            }
        }


        return view
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

}
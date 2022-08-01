package com.example.chick

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

class HelperActivity : AppCompatActivity() {
    private var viewpager : ViewPager? = null
    private var vpAdapter: FragmentStatePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helper)

        vpAdapter = CustomPagerAdapter(supportFragmentManager)
        viewpager = findViewById(R.id.viewpager)
        viewpager?.adapter = vpAdapter as CustomPagerAdapter

        //닫기버튼 클릭시 마이페이지 화면 전환
        val btn_helperclose = findViewById<LinearLayout>(R.id.linearClose)
        btn_helperclose.setOnClickListener{
            onBackPressed()
        }

    }

    class CustomPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val PAGENUMBER = 5

        override fun getCount(): Int {
            return PAGENUMBER
        }

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> HelperFragment1()
                1 -> HelperFragment2()
                2 -> HelperFragment3()
                3 -> HelperFragment4()
                4 -> HelperFragment5()
                else -> HelperFragment1()
            }
        }
    }
}


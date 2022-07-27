package com.example.chick

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    lateinit var btnPlus : FloatingActionButton

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_main, container, false)

        dbManager = DBManager(context, "drug", null, 1)


        btnPlus = view.findViewById(R.id.btnMainAdd)

        btnPlus.setOnClickListener{
            val intent = Intent(activity, EditAlarmActivity::class.java)
            startActivity(intent)

//            sqlDB = dbManager.writableDatabase
//            sqlDB.execSQL("INSERT INTO drugDB VALUES (1, 'vitamin', 'am', 2, 30, 'Mon', 2, 30, 0, 1);")
//            sqlDB.close()
            //Toast.makeText(applicationContext, "입력됨", Toast.LENGTH_SHORT).show()
        }

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)

        return view
    }


}
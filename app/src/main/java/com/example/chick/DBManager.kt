package com.example.chick

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View

class DBManager(
    context: Context,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE drugTBL ( medId INTEGER PRIMARY KEY, medName CHAR(20), ampm CHAR(10), alarmTime INTEGER, alarmHour INTEGER, alarmMin INTEGER, daysOfWeek CHAR(10), eatNumber INTEGER, totalNumber INTEGER, currentNumber INTEGER, medIcon INTEGER, eatDone INTEGER, goalDone INTEGER);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}
package com.example.chick
//Data코틀린에 합치기

data class ProDrugAll(
    var medId :  Int? =  null,
    var medName :  String? =  null,
    var ampm :  String? =  null,
    var alarmHour :  Int? =  null,
    var alarmMin :  Int? =  null,
    var daysOfWeek :  String? =  null,
    var eatNumber :  Int? =  null,
    var medIcon :  Int? =  null,
    var currentNumber : Int? = null,
    var totalNumber : Int? = null,
    var eatDone : Int? = null
    //var GoalDone : Int? = null
)
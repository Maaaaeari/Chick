package com.example.chick

// 전체 약 리스트
data class DrugAll(
    var medId :  Int? =  null,
    var medName :  String? =  null,
    var ampm :  String? =  null,
    var alarmHour :  Int? =  null,
    var alarmMin :  Int? =  null,
    var daysOfWeek :  String? =  null,
    var eatNumber :  Int? =  null,
    var currentNumber : Int? = null,
    var medIcon :  Int? =  null,
    var eatDone : Int? = null
)

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
    var eatDone : Int? = null,
    var goalDone : Int? = null
)
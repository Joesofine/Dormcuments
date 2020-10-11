package com.example.dormcuments.ui.calender

import android.icu.text.CaseMap

class Event {
    var title = ""
    var date = ""
    var timeStart = ""
    var timeEnd = ""
    var des = ""
    var location = ""
    var color = ""
    var allDay = ""
    var notification = ""
    var doesRepeat = ""


    constructor(title: String, date: String, timeStart: String, timeEnd: String, des: String, location: String,
                color: String, allDay: String, notification: String, doesRepeat: String){

        this.title = title
        this.timeStart = timeStart
        this.date = date
        this.timeEnd = timeEnd
        this.des = des
        this.location = location
        this.color = color
        this.allDay = allDay
        this.notification = notification
        this.doesRepeat = doesRepeat
    }
}
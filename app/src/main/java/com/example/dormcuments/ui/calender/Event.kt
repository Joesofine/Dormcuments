package com.example.dormcuments.ui.calender

import android.icu.text.CaseMap

class Event {
    var title = ""
    var dateStart = ""
    var dateEnd = ""
    var timeStart = ""
    var timeEnd = ""
    var des = ""
    var location = ""
    var color = ""
    var allDay = ""
    var notification = ""
    var doesRepeat = ""
    var createdBy = ""


    constructor(title: String, dateStart: String, dateEnd: String, timeStart: String, timeEnd: String, des: String, location: String,
                color: String, allDay: String, notification: String, doesRepeat: String, createdBy: String){

        this.title = title
        this.timeStart = timeStart
        this.dateStart = dateStart
        this.dateEnd = dateEnd
        this.timeEnd = timeEnd
        this.des = des
        this.location = location
        this.color = color
        this.allDay = allDay
        this.notification = notification
        this.doesRepeat = doesRepeat
        this.createdBy = createdBy
    }
}
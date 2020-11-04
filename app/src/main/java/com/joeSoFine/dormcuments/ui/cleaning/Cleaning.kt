package com.joeSoFine.dormcuments.ui.cleaning

class Cleaning {
    var c1 = ""
    var c2 = ""
    var date = ""
    var task = ""
    var note = ""
    var checkedBy = "Unchecked"
    var unform = ""

    constructor(c1: String, c2: String, date: String, task: String, note: String, checkedBy: String, unform: String){
        this.c1 = c1
        this.c2 = c2
        this.date = date
        this.task = task
        this.note = note
        this.checkedBy = checkedBy
        this.unform = unform
    }
}
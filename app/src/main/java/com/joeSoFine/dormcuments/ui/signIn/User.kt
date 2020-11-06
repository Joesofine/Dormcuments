package com.joeSoFine.dormcuments.ui.signIn

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(var fname: String, var number: String, var bdate: String, var from: String, var diet: String, var funfact: String, var url: String) : Parcelable{
    /*var fname = ""
    var number = ""
    var bdate = ""
    var from = ""
    var diet = ""
    var funfact = ""
    var url = ""


    constructor(fname: String, number: String, bdate: String, from: String, diet: String, funfact: String, url: String ){
        this.fname = fname
        this.number = number
        this.bdate = bdate
        this.from = from
        this.diet = diet
        this.funfact = funfact
        this.url = url
    }

     */
}
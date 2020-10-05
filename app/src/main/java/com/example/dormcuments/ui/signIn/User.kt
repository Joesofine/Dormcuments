package com.example.dormcuments.ui.signIn

class User {
    var fname = ""
    var lname = ""
    var number = ""
    var bdate = ""
    var from = ""
    var diet = ""
    var funfact = ""
    var email = ""
    var password = ""



    constructor(fname: String, lname: String, number: String, bdate: String, from: String, diet: String, funfact: String, email: String, password: String ){
        this.fname = fname
        this.lname = lname
        this.number = number
        this.bdate = bdate
        this.from = from
        this.diet = diet
        this.funfact = funfact
        this.email = email
        this.password = password
    }
}
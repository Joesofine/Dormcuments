package com.example.dormcuments

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

public class Logic {
    companion object Factory{
        fun create(): Logic = Logic()
    }
    @Throws(IOException::class)
    fun hentUrl(url: String): String {
        println("Henter data fra $url")
        val br = BufferedReader(InputStreamReader(URL(url).openStream()))
        val sb = StringBuilder()
        var linje = br.readLine()
        while (linje != null) {
            sb.append("""
    $linje
    
    """.trimIndent())
            linje = br.readLine()
        }
        return sb.toString()
    }
    /*fun getUrlAsString(url: String): String {        val client = HttpClient(Android) {
        }
        return client.get<String>(url)
    }*/

}
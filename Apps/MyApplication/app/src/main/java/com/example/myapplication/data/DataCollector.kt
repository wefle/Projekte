package com.example.myapplication.data

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File

class DataCollector {

    //download pdf file
    fun getPDFData(str: String, nr: Int, version: String, con: Context, pdfLnk: String) {
        try {
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS)
            val file = File(path, "${str}_${nr}_magdeburg_abfuhrtermine_${version}.pdf")

            if(!file.exists()) {
                val downloadManager =
                    con.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                val url = pdfLnk

                //getting file and saving in directory
                val request = DownloadManager.Request(Uri.parse(url))
                    .apply {
                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                        setTitle("Abfuhrtermine")
                        setMimeType("pdf")
                        setDescription("Bitte warten ...")
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOCUMENTS,
                            "${str}_${nr}_magdeburg_abfuhrtermine_${version}.pdf"
                        )
                    }

                downloadManager.enqueue(request)
            }
        }
        catch (e: Exception){
            Log.e("PDFCollector", "Something went wrong", e)
            e.printStackTrace()
        }

    }
}
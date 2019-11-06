package com.ginntopopin.createpdf

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Bundle
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.format.TextStyle

class MainActivity : AppCompatActivity() {
    internal lateinit var btnCreate: Button
    internal lateinit var editText: EditText
    //permission is automatically granted on sdk<23 upon installation
    val isStoragePermissionGranted: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission is granted")
                    return true
                } else {
                    Log.v(TAG, "Permission is revoked")
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                    return false
                }
            } else {
                Log.v(TAG, "Permission is granted")
                return true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnCreate = findViewById<View>(R.id.create) as Button
        editText = findViewById<View>(R.id.edittext) as EditText
        btnCreate.setOnClickListener {
            if (isStoragePermissionGranted) {
                createPdf(editText.text.toString())
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0])
            //resume tasks needing this permission
        }
    }

    private fun createPdf(sometext: String) {
        // create a new document
        val document = PdfDocument()
        // crate a page description
        var pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        // start a page
        var page: PdfDocument.Page = document.startPage(pageInfo)
        var canvas = page.canvas
        var paint = Paint()
        //paint.color = Color.RED
        //canvas.drawCircle(50f, 50f, 30f, paint)
        paint.color = Color.BLACK
        canvas.drawText(sometext, 80f, 90f, paint)
        //canvas.drawt
        // finish the page
        document.finishPage(page)
        // draw text on the graphics object of the page
        // Create Page 2
//        pageInfo = PdfDocument.PageInfo.Builder(300, 600, 2).create()
//        page = document.startPage(pageInfo)
//        canvas = page.canvas
//        paint = Paint()
//        paint.color = Color.BLUE
        //canvas.drawCircle(100f, 100f, 100f, paint)
        //document.finishPage(page)
        // write the document content
        val directory_path = Environment.getExternalStorageDirectory().absolutePath + "/mypdf/"
        val file = File(directory_path)
        if (!file.exists()) {
            file.mkdirs()
        }
        val targetPdf = directory_path + File.separator + "test-2.pdf"
        val filePath = File(targetPdf)
        try {
            document.writeTo(FileOutputStream(filePath))
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("main", "error $e")
            Toast.makeText(this, "Something wrong: $e", Toast.LENGTH_LONG).show()
        }

        // close the document
        document.close()
    }

    companion object {
        private val TAG = "some"
    }
}

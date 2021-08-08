
package com.example.sditsattendance

/*
 * Created by farras on 10/31/2020
*/


import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sditsattendance.costume.CostumeScannerView
import com.example.sditsattendance.costume.CostumeViewFinder
import com.example.sditsattendance.data.Access
import com.google.android.material.snackbar.Snackbar

import com.google.zxing.Result
import me.dm7.barcodescanner.core.BarcodeScannerView
import me.dm7.barcodescanner.core.IViewFinder
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.ArrayList

private const val TAG = "ScanActivity"

class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    lateinit var starterIntent:Intent
    lateinit var scannerView:ZXingScannerView
    lateinit var myActivity:ScanActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isFlashOn:Boolean = false;
        myActivity = this


        Log.d(TAG, "onCreate: ${TAG}")
        supportActionBar?.hide()

        starterIntent = intent
        scannerView = CostumeScannerView (this@ScanActivity)


//        untuk menyalakan flas ketika sedikit gelap
        scannerView.setOnClickListener {
            if (isFlashOn){
                Snackbar.make(scannerView,"Flash is Off",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Turn On"){
                        isFlashOn = true
                        scannerView.flash = true
                    }
                    .show()
                isFlashOn = false
                scannerView.flash = false
            }
            else {
                Snackbar.make(scannerView,"Flash is On",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Turn Off"){
                        isFlashOn = false
                        scannerView.flash = false
                    }
                    .show()
                isFlashOn = true
                scannerView.flash = true
            }

        }
        setContentView(scannerView)
    }
    override fun handleResult(rawResult: Result?) {
//        onRestart(rawResult)
        scannerView.setResultHandler (this)
        scannerView.startCamera()

        var noPeserta = rawResult.toString().split(Regex("-"),2);

        // convert list to arrayList
        var noPesertaArrayList:ArrayList<String> = ArrayList()

        for (i:Int in noPeserta.indices){
            noPesertaArrayList.add(noPeserta[i])
        }

        Log.d("te","jumlah data ${noPeserta.size}")
        // check weheter there are to element in that array
        if (noPeserta.size == 2){
            Access.activity = "scan"
            var intentConfirm = Intent(this,ConfirmActivity::class.java)
            intentConfirm.putStringArrayListExtra("dataPeserta",noPesertaArrayList)
            startActivity(intentConfirm)
            finish()
        }
        else {


            Snackbar.make(scannerView,getString(R.string.error_barcode),Snackbar.LENGTH_LONG).show()

        }
        Log.d(TAG, "handleResult: ${TAG} ${noPeserta}")

    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler (this)
        scannerView.startCamera()
        Log.d(TAG, "onResume: ${TAG}")
    }

}
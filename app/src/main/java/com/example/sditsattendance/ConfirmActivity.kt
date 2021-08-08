package com.example.sditsattendance

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.Toast
import com.example.sditsattendance.api.ResponsePeserta
import com.example.sditsattendance.api.ResponsePostAbsen
import com.example.sditsattendance.data.Access
import com.example.sditsattendance.network.DataRepository
import com.example.sditsattendance.network.ResponeService
import kotlinx.android.synthetic.main.activity_confirm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmActivity : AppCompatActivity() , View.OnClickListener {
    lateinit var myContext:Context
    lateinit var noPeserta:String
    lateinit var TAG:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)


        TAG = "ConfirmActivity"
        myContext = applicationContext

//        supportActionBar?.hide()
//        change color of action bar to yellow
        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.confirm_gradient))
        var intent = intent.getStringArrayListExtra("dataPeserta")
        var nama = intent?.get(0) ?: ""
        noPeserta = intent?.get(1) ?: ""

        // change image
        var polaPeserta = noPeserta.toString().split(Regex("-"));

        if (polaPeserta.size == 5){
            if (polaPeserta[1] == "P"){
                imageView.setImageResource(R.drawable.akhwat)
            }
            else {
                imageView.setImageResource(R.drawable.ikhwan)
            }
        }

        DataRepository.create().getResponseAbsen(noPeserta)
            .enqueue(object : Callback<List<ResponsePeserta>>{
                override fun onFailure(call: Call<List<ResponsePeserta>>, t: Throwable) {

                    // move to scan acvitiyty
                    Toast.makeText(myContext,getString(R.string.error_absen_ppdb),Toast.LENGTH_LONG).show()
                    Log.d(TAG,"Pesan kesalahan : ${t.localizedMessage}")
                    val intent = Intent(myContext,ScanActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onResponse(
                    call: Call<List<ResponsePeserta>>,
                    response: Response<List<ResponsePeserta>>
                ) {
                    Log.d(TAG,"Is Hadir ${response.body()?.get(0)?.ishadir}")
                    if (response.body()?.get(0)?.ishadir != "ok"){
//                         Belum hadir

                    }
                    else {
//                        sudah hadir
                        btn_hadir.isEnabled = false
                        textViewAsk.visibility = View.GONE
                        textWarning.visibility = View.VISIBLE
                    }
                    Log.d(TAG,"Hasil : ${response.body()}")
                }

            })


        conf_nama.text = nama
        conf_no_peserta.text = noPeserta

        btn_tdk_hadir.setOnClickListener(this)
        btn_hadir.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_tdk_hadir -> {
                if (Access.activity == "scan"){
                    val intentThis = Intent(myContext,ScanActivity::class.java)
                    startActivity(intentThis)
                    finish()
                }
                else {
                    val intentThis = Intent(myContext,FindActivity::class.java)
                    startActivity(intentThis)
                    finish()
                }
            }
            R.id.btn_hadir -> {
                // set to loading
                bg_loading.visibility = View.VISIBLE

                // post data
                DataRepository.create().postAbsen(noPeserta)
                    .enqueue(object : Callback<ResponsePostAbsen>{



                        override fun onFailure(call: Call<ResponsePostAbsen>, t: Throwable) {
                            Toast.makeText(myContext,R.string.error_absen_ppdb,Toast.LENGTH_LONG).show()

                            Log.d(TAG,"pesan kesalahan : ${t.localizedMessage}")
                            val intent = Intent(myContext,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onResponse(
                            call: Call<ResponsePostAbsen>,
                            response: Response<ResponsePostAbsen>
                        ) {
                            if (response.isSuccessful){
                                // call all body

                                if (response.body()?.status == 1){
                                    Toast.makeText(myContext,R.string.respone_post_absen,Toast.LENGTH_LONG).show()

                                    val intent = Intent(myContext,MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }

                    })
            }
        }
    }
}
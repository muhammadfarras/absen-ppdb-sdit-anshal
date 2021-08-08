package com.example.sditsattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sditsattendance.adapter.ListSelectionRecyclerViewAdapter
import com.example.sditsattendance.api.ResponsePeserta
import com.example.sditsattendance.network.DataRepository
import kotlinx.android.synthetic.main.user_peserta.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPesertaActivity : AppCompatActivity() {

    lateinit var listRecyclerView:RecyclerView
    lateinit var chose:Call<List<ResponsePeserta>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_peserta)


        listRecyclerView = findViewById(R.id.rv)

        listRecyclerView.layoutManager = LinearLayoutManager(this)


        val intent = intent
        var information = intent.getStringExtra("information")
        Toast.makeText(this,information,Toast.LENGTH_SHORT).show()

        val responeService = DataRepository.create()


        when (information){
            "ikhwan" -> {
                chose = responeService.getResponseIkhwan()

            }
            "akhwat" -> {
                chose = responeService.getResponseAkhwat()

            }
        }

        chose.enqueue(object : Callback<List<ResponsePeserta>>{
            override fun onFailure(call: Call<List<ResponsePeserta>>, t: Throwable) {
                Log.d("recycler",t.localizedMessage)
            }

            override fun onResponse(
                call: Call<List<ResponsePeserta>>,
                response: Response<List<ResponsePeserta>>
            ) {
                if (response.isSuccessful){
                    listRecyclerView.adapter = ListSelectionRecyclerViewAdapter(response.body());
                }
                else {
                    Log.d("recycler","data tidak ada")
                }

            }

        })
    }
}
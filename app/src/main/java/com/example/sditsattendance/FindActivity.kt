package com.example.sditsattendance

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sditsattendance.adapter.ListParticipantRecyclerViewAdapter
import com.example.sditsattendance.api.ResponsePeserta
import com.example.sditsattendance.holder.ListFindParticipantViewHolder
import com.example.sditsattendance.network.DataRepository
import kotlinx.android.synthetic.main.activity_find.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindActivity : AppCompatActivity() {

    lateinit var listParticipantRecyclerView : RecyclerView
    lateinit var thisActivity: Intent
    val TAG = "FindActivity - LOG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)


        thisActivity = intent
        listParticipantRecyclerView = findViewById(R.id.rv_find_nama_peserta)
        listParticipantRecyclerView.layoutManager = LinearLayoutManager (this)



        edit_text_find_name.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                DataRepository.create().getResponseFindPeserta(s.toString())
                    .enqueue(object : Callback<List<ResponsePeserta>>{
                        override fun onFailure(call: Call<List<ResponsePeserta>>, t: Throwable) {
                            Log.d(TAG,t.localizedMessage)
                        }

                        override fun onResponse(
                            call: Call<List<ResponsePeserta>>,
                            response: Response<List<ResponsePeserta>>
                        ) {
                            if (response.isSuccessful){
                                listParticipantRecyclerView.adapter = ListParticipantRecyclerViewAdapter (response.body(),this@FindActivity)

                                Log.d (TAG,response.body().toString())
                            }
                        }

                    })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                Log.d(TAG,"beforeTextChanged : ${s.toString()}")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                Log.d(TAG,"onTextChanged :  ${s.toString()}")
            }

        })

    }
}
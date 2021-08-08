package com.example.sditsattendance.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sditsattendance.ConfirmActivity
import com.example.sditsattendance.R
import com.example.sditsattendance.api.ResponsePeserta
import com.example.sditsattendance.data.Access
import com.example.sditsattendance.holder.ListFindParticipantViewHolder


class ListParticipantRecyclerViewAdapter (val listPeserta:List<ResponsePeserta>?, val context: Context) : RecyclerView.Adapter<ListFindParticipantViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListFindParticipantViewHolder {
        val view = LayoutInflater.from (parent.context)
            .inflate(R.layout.user_find,parent,false)

        return ListFindParticipantViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (listPeserta != null) {
            return listPeserta.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ListFindParticipantViewHolder, position: Int) {
        var arrayListParticipant = ArrayList<String>()
        var namaPeserta:String = listPeserta?.get(position)?.namaPanjang ?: ""
        var noPeserta: String = listPeserta?.get(position)?.noPeserta ?: ""

        arrayListParticipant.add(namaPeserta)
        arrayListParticipant.add(noPeserta)
        holder.namaSiswa.text = namaPeserta
        holder.noPeserta.text = noPeserta

//        click listener pindah ke confirm activity
        holder.cardClickFind.setOnClickListener {

            Access.activity = "find"
            val intent = Intent (context,ConfirmActivity::class.java)
            intent.putStringArrayListExtra("dataPeserta",arrayListParticipant)
            context.startActivity(intent)
            (context as Activity).finish()



        }

    }
}
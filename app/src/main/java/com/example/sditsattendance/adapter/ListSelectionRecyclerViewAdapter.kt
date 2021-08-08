package com.example.sditsattendance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sditsattendance.R
import com.example.sditsattendance.api.ResponsePeserta
import com.example.sditsattendance.holder.ListSelectionViewHolder

class ListSelectionRecyclerViewAdapter(var body: List<ResponsePeserta>?) : RecyclerView.Adapter<ListSelectionViewHolder>() {
    val listTitles = arrayOf("Shopping List", "Chores", "Android Tutorials")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {

        val view = LayoutInflater.from (parent.context)
            .inflate(R.layout.user_peserta,parent,false)

        return ListSelectionViewHolder(view)


    }

    override fun getItemCount(): Int {
        return body!!.size
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.namaSiswa.text = body?.get(position)?.namaPanjang ?: "-"
        holder.namaPanggilan.text = body?.get(position)?.namaPanggilan ?: "-"
        holder.noPeserta.text = body?.get(position)?.noPeserta ?: "-"
    }
}
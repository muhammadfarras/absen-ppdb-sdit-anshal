package com.example.sditsattendance.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sditsattendance.R
import kotlinx.android.synthetic.main.user_peserta.view.*
import org.w3c.dom.Text

class ListSelectionViewHolder (itemView :View):RecyclerView.ViewHolder (itemView) {
    val namaSiswa = itemView.findViewById(R.id.nama) as TextView
    val namaPanggilan = itemView.findViewById(R.id.nama_panggilan) as TextView
    val noPeserta = itemView.findViewById(R.id.no_peserta) as TextView
}
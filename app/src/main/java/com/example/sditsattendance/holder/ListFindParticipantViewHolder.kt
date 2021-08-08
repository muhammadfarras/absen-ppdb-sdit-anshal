package com.example.sditsattendance.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sditsattendance.R
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.user_peserta.view.*
import org.w3c.dom.Text

class ListFindParticipantViewHolder (itemView :View):RecyclerView.ViewHolder (itemView) {
    val namaSiswa = itemView.findViewById<TextView>(R.id.find_nama)
    val noPeserta = itemView.findViewById<TextView>(R.id.find_no_peserta)
    val cardClickFind = itemView.findViewById(R.id.cv_find_peserta) as MaterialCardView
}
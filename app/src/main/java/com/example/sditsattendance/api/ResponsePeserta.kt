package com.example.sditsattendance.api

import com.google.gson.annotations.SerializedName

data class ResponsePeserta(

	@field:SerializedName("hadir")
	val hadir: String? = null,

	@field:SerializedName("online_time")
	val onlineTime: Any? = null,

	@field:SerializedName("ishadir")
	val ishadir: String? = null,

	@field:SerializedName("nama_panjang")
	val namaPanjang: String? = null,

	@field:SerializedName("no_peserta")
	val noPeserta: String? = null,

	@field:SerializedName("nama_panggilan")
	val namaPanggilan: String? = null,

	@field:SerializedName("tp")
	val tp: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("wawancara")
	val wawancara: String? = null
)

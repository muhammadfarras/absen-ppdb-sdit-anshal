package com.example.sditsattendance.api

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("hasil")
	val hasil: String? = null,

	@field:SerializedName("ikhwan")
	val ikhwan: String? = null,

	@field:SerializedName("akhwat")
	val akhwat: String? = null,

	@field:SerializedName("kelSatu")
	val kelSatu: String? = null,

	@field:SerializedName("kelDua")
	val kelDua: String? = null,

	@field:SerializedName("kelTiga")
	val kelTiga: String? = null,

	@field:SerializedName("kelEmpat")
	val kelEmpat: String? = null,

	@field:SerializedName("kelLima")
	val kelLima: String? = null,

	@field:SerializedName("kelEnam")
	val kelEnam: String? = null,

	@field:SerializedName("kelTujuh")
	val kelTujuh: String? = null,

	@field:SerializedName("kelDelapan")
	val kelDelapan: String? = null,

	@field:SerializedName("kelSembilan")
	val kelSembilan: String? = null,

	@field:SerializedName("kelSepuluh")
	val kelSepuluh: String? = null

)

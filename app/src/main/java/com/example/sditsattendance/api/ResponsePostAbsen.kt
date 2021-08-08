package com.example.sditsattendance.api

import com.google.gson.annotations.SerializedName

data class ResponsePostAbsen(

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("respone")
	val respone: String? = null
)

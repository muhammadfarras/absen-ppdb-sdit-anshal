package com.example.sditsattendance.network

import com.example.sditsattendance.api.Response
import com.example.sditsattendance.api.ResponsePeserta
import com.example.sditsattendance.api.ResponsePostAbsen
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ResponeService {

    @GET("portal/home/tugas/daftar-ulang-ppdb/api/")
    fun getResponse (): Call<List<Response>>

    @GET("portal/home/tugas/daftar-ulang-ppdb/api-ikhwan/?param=laki")
    fun getResponseIkhwan (): Call<List<ResponsePeserta>>

    @GET("portal/home/tugas/daftar-ulang-ppdb/api-akhwat/")
    fun getResponseAkhwat (): Call<List<ResponsePeserta>>

    @GET ("portal/home/tugas/daftar-ulang-ppdb/api-ABSEN/")
    fun getResponseAbsen (@Query("no_peserta") no_peserta: String?): Call<List<ResponsePeserta>>

    @GET ("portal/home/tugas/daftar-ulang-ppdb/api-update-absen/")
    fun postAbsen (@Query("no_peserta") no_peserta: String?): Call<ResponsePostAbsen>

    @GET ("portal/home/tugas/daftar-ulang-ppdb/api-find-peserta/?param=find")
    fun getResponseFindPeserta (@Query("nama") nama: String?): Call<List<ResponsePeserta>>
}
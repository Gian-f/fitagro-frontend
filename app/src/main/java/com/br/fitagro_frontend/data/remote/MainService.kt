package com.br.fitagro_frontend.data.remote

import com.br.fitagro_frontend.data.remote.response.SearchFruitResponse
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path

interface MainService {
    @GET("/{culture}")
    fun getProducts(@Path("culture") culture: String): Call<List<SearchFruitResponse>>
}
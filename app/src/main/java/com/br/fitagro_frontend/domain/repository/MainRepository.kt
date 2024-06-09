package com.br.fitagro_frontend.domain.repository

import com.br.fitagro_frontend.data.remote.response.SearchFruitResponse

interface MainRepository {
   suspend fun searchFruit(culture: String):Result<List<SearchFruitResponse>?>
}
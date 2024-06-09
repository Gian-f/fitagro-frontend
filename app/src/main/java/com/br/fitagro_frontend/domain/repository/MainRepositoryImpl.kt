package com.br.fitagro_frontend.domain.repository

import com.br.fitagro_frontend.data.remote.MainService
import com.br.fitagro_frontend.data.remote.response.SearchFruitResponse
import retrofit2.awaitResponse
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val service: MainService) : MainRepository {
    override suspend fun searchFruit(culture: String): Result<List<SearchFruitResponse>?> {
        return try {
            val response =
                service.getProducts(culture).awaitResponse()
            if (response.isSuccessful) {
                val ticketResponse = response.body()
                Result.success(ticketResponse)
            } else {
                Result.failure(Exception("Failed to fetch tickets: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
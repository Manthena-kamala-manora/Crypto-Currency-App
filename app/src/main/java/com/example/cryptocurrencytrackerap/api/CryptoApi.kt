package com.example.cryptocurrencytrackerap.api

import com.example.cryptocurrencytrackerap.api.model.Crypto
import com.example.cryptocurrencytrackerap.api.model.CryptoDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CryptoApi {
    @GET("coins/markets")
    suspend fun getCryptoList(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<Crypto>

        @GET("coins/{id}")
        suspend fun getCryptoDetails(
            @Path("id") id: String,
            @Query("localization") localization: Boolean = false
        ): CryptoDetail

}

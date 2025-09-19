package com.example.cryptocurrencytrackerap.repository

import com.example.cryptocurrencytrackerap.api.model.Crypto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CryptoRepository(private val client: HttpClient) {

    suspend fun fetchCryptoList(): List<Crypto> {
        return client.get("https://api.coingecko.com/api/v3/coins/markets") {
            parameter("vs_currency", "usd")
            parameter("order", "market_cap_desc")
            parameter("per_page", 50)
            parameter("page", 1)
            parameter("sparkline", false)
        }.body()
    }
}
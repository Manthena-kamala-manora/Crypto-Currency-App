package com.example.cryptocurrencytrackerap.repository

import com.example.cryptocurrencytrackerap.api.CryptoApi
import com.example.cryptocurrencytrackerap.api.RetrofitInstance.api
import com.example.cryptocurrencytrackerap.api.model.Crypto
import com.example.cryptocurrencytrackerap.api.model.CryptoDetail

class CryptoRepository(private val client: CryptoApi) {

    suspend fun fetchCryptoList(): List<Crypto> {
        return api.getCryptoList()
    }

    suspend fun getCryptoDetails(id: String): CryptoDetail {
        return api.getCryptoDetails(id)
    }


}
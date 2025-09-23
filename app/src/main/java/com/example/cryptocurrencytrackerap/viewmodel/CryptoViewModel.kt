package com.example.cryptocurrencytrackerap.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytrackerap.api.model.Crypto
import com.example.cryptocurrencytrackerap.api.model.CryptoDetail
import com.example.cryptocurrencytrackerap.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class CryptoViewModel(
    private val repository: CryptoRepository
) : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<Crypto>>(emptyList())
    val cryptoList: StateFlow<List<Crypto>> = _cryptoList

    private val _cryptoDetail = MutableStateFlow<CryptoDetail?>(null)
    val cryptoDetail: StateFlow<CryptoDetail?> = _cryptoDetail

    init {
        getCryptoList()
        Log.d("CryptoViewModel", "ViewModel initialized")
    }

    private fun getCryptoList() {
        viewModelScope.launch {
            try {
                val result = repository.fetchCryptoList()
                _cryptoList.value = result
                Log.i("CryptoViewModel", "" + result.size)
            } catch (e: HttpException) {
                    if (e.code() == 429) {
                        Log.e("CryptoViewModel", "Rate limit exceeded. Please try again later.")
                    } else {
                        Log.e("CryptoViewModel", "Error fetching crypto list", e)
                    }
                }
         }
    }

    fun fetchCryptoDetail(id: String) {
        viewModelScope.launch {
            try {
                val result = repository.getCryptoDetails(id)
                _cryptoDetail.value = result
            } catch (e: HttpException) {
                if (e.code() == 429) {
                        Log.e("CryptoViewModel", "Rate limit exceeded. Please try again later.")
                    } else {
                        Log.e("CryptoViewModel", "Error fetching crypto list", e)
                    }
                }
           }
       }
}

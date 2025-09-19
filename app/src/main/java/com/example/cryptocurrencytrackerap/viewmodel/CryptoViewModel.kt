package com.example.cryptocurrencytrackerap.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytrackerap.api.model.Crypto
import com.example.cryptocurrencytrackerap.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val repository: CryptoRepository
) : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<Crypto>>(emptyList())
    val cryptoList: StateFlow<List<Crypto>> = _cryptoList

    init {
        getCryptoList()

        Log.d("CryptoViewModel", "ViewModel initialized")

    }

    private fun getCryptoList() {
        viewModelScope.launch {
            try {
                val result = repository.fetchCryptoList()
                _cryptoList.value = result
                Log.i("CryptoViewModel",""+ result.size )

            } catch (e: Exception) {
                Log.e("CryptoViewModel", "Error fetching crypto list", e)
            }
        }
    }
}

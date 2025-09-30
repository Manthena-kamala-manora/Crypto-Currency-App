package com.example.cryptocurrencytrackerap.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytrackerap.api.ApiResult
import com.example.cryptocurrencytrackerap.api.model.Crypto
import com.example.cryptocurrencytrackerap.api.model.CryptoDetail
import com.example.cryptocurrencytrackerap.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import retrofit2.HttpException


class CryptoViewModel(
    private val repository: CryptoRepository
) : ViewModel() {

    private val _cryptoState = MutableStateFlow<ApiResult<List<Crypto>>>(ApiResult.Loading)

    val cryptoList: StateFlow<ApiResult<List<Crypto>>> = _cryptoState

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
                _cryptoState.value = ApiResult.Success(result)
            } catch (e: HttpException) {
                 val errorMessage = when (e.code()) {
                        404 -> "Data not found"
                        500 -> "Server error"
                        else -> "Unexpected error: ${e.message()}"
                    }
                    _cryptoState.value = ApiResult.Error(errorMessage)
                } catch (e: IOException) {
                    _cryptoState.value = ApiResult.Error("Network error. Please check your connection.")
                } catch (e: Exception) {
                    _cryptoState.value = ApiResult.Error("Something went wrong: ${e.localizedMessage}")
                }
            }
        }



    fun fetchCryptoDetail(id: String) {
        viewModelScope.launch {
            try {
                val result = repository.getCryptoDetails(id)
                _cryptoDetail.value = result
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    404 -> "Data not found"
                    500 -> "Server error"
                    else -> "Unexpected error: ${e.message()}"
                }
                _cryptoState.value = ApiResult.Error(errorMessage)
            } catch (e: IOException) {
                _cryptoState.value = ApiResult.Error("Network error. Please check your connection.")
            } catch (e: Exception) {
                _cryptoState.value = ApiResult.Error("Something went wrong: ${e.localizedMessage}")
            }
        }
    }
}

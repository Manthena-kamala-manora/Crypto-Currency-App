package com.example.cryptocurrencytrackerap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.cryptocurrencytrackerap.api.RetrofitInstance
import com.example.cryptocurrencytrackerap.repository.CryptoRepository
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModel
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModelFactory

@Composable
fun CryptoDetailScreen(cryptoId: String) {
    val repository = remember { CryptoRepository(RetrofitInstance.api) }

    val viewModel: CryptoViewModel  = viewModel(
        factory = CryptoViewModelFactory(repository)
    )

    LaunchedEffect(cryptoId) {
        viewModel.fetchCryptoDetail(cryptoId)
    }

    val cryptoDetail by viewModel.cryptoDetail.collectAsState()

    cryptoDetail?.let { detail ->
        // Show UI with detail data
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(data = detail.image.large),
                contentDescription = "${detail.name} logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = detail.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Market Cap: \$${detail.market_data.market_cap["usd"]}")
            Text(text = "24h Volume: \$${detail.market_data.total_volume["usd"]}")
            Text(text = "Price Change (24h): ${detail.market_data.price_change_percentage_24h}%")
            Spacer(modifier = Modifier.height(16.dp))

            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(10.dp)
            ) {
                Text(text = detail.description.en)
            }
        }
    } ?: run {
        // Loading state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
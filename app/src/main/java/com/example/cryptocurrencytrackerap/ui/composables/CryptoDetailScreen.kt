package com.example.cryptocurrencytrackerap.ui.composables

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.cryptocurrencytrackerap.R
import com.example.cryptocurrencytrackerap.repository.CryptoRepository
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModel
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModelFactory

@Composable
fun CryptoDetailScreen(cryptoId: String) {
    val repository = remember { CryptoRepository() }

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
            CryptoImg(detail.image.large,detail.name)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = detail.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = stringResource(id = R.string.market_cap) + " \$${detail.market_data.market_cap[stringResource(id = R.string.usd)]}")
            Text(text =stringResource(id = R.string.volume) + " \$${detail.market_data.total_volume[stringResource(id = R.string.usd)]}")
            Text(text = stringResource(id = R.string.price_change) + " ${detail.market_data.price_change_percentage_24h}%")
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

@Composable
fun CryptoImg(url: String, name: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = name,
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
    )
}
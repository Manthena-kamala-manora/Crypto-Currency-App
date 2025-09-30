package com.example.cryptocurrencytrackerap.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cryptocurrencytrackerap.R
import com.example.cryptocurrencytrackerap.api.ApiResult
import com.example.cryptocurrencytrackerap.api.model.Crypto
import com.example.cryptocurrencytrackerap.navigation.NavigationDestination
import com.example.cryptocurrencytrackerap.repository.CryptoRepository
import com.example.cryptocurrencytrackerap.util.CryptoToolbar
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModel
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModelFactory


object CryptoDetailDestination : NavigationDestination {
    override val route = "list"
    override val titleRes = R.string.app_name
    const val routeArgs = "detail/"
    const val routeDetailArgs = "detail/{cryptoId}"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListScreen(
    onItemClick: (String) -> Unit) {

    val repository = remember { CryptoRepository() }

    val viewModel: CryptoViewModel  = viewModel(
        factory = CryptoViewModelFactory(repository)
    )

    val cryptoList by viewModel.cryptoList.collectAsState()

    val cryptoData: List<Crypto> = when (cryptoList) {
        is ApiResult.Success -> (cryptoList as ApiResult.Success<List<Crypto>>).data
        else -> emptyList() // fallback if not success
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CryptoToolbar(
                title = stringResource(CryptoDetailDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(cryptoData) { crypto ->
                CryptoListItem(crypto, onClick = { onItemClick(crypto.id) })
            }
        }

    }
}
@Composable
fun CryptoListItem(crypto: Crypto, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coin Logo
        CryptoImage(crypto.image, crypto.name)
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = "${crypto.name} (${crypto.symbol})", style = MaterialTheme.typography.bodyLarge)
            Text(text = stringResource(id = R.string.price) + "$${crypto.current_price}", style = MaterialTheme.typography.bodyMedium)
        }

        // Price Change %
        val priceChangeColor = if (crypto.price_change_percentage_24h >= 0) Color.Green else Color.Red
        Text(
            text = "${crypto.price_change_percentage_24h}%",
            color = priceChangeColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun CryptoImage(url: String, name: String) {
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



@Preview(showBackground = true)
@Composable
fun CryptoListPreview() {
    val mockData = listOf(
        Crypto(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
            current_price = 27000.0,
            price_change_percentage_24h = 2.5
        )
    )
    LazyColumn {
        items(mockData) { crypto ->
            CryptoListItem(crypto,onClick = { "bitcoin"})
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCryptoImage() {
    CryptoImage("https://example.com/image.png", "name")
}

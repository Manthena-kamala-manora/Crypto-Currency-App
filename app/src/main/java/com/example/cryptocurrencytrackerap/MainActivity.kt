package com.example.cryptocurrencytrackerap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.cryptocurrencytrackerap.api.RetrofitInstance
import com.example.cryptocurrencytrackerap.api.model.Crypto
import com.example.cryptocurrencytrackerap.repository.CryptoRepository
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModel
import com.example.cryptocurrencytrackerap.viewmodel.CryptoViewModelFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            MainNavHost(
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoToolbar() {

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(10.dp)
            )
        }
    )

}


@Composable
fun CryptoListScreen(
    onItemClick: (String) -> Unit) {

    val repository = remember { CryptoRepository(RetrofitInstance.api) }

    val viewModel: CryptoViewModel  = viewModel(
        factory = CryptoViewModelFactory(repository)
    )

    val cryptoList by viewModel.cryptoList.collectAsState()

    Scaffold(
        topBar = { CryptoToolbar() }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(cryptoList) { crypto ->
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
        Image(
            painter = rememberImagePainter(data = crypto.image),
            contentDescription = "${crypto.name} logo",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = "${crypto.name} (${crypto.symbol})", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Price: \$${crypto.current_price}", style = MaterialTheme.typography.bodyMedium)
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
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") {
            CryptoListScreen(
                onItemClick = { cryptoId ->
                    navController.navigate("detail/$cryptoId")
                }
            )
        }
        composable("detail/{cryptoId}") { backStackEntry ->
            val cryptoId = backStackEntry.arguments?.getString("cryptoId")
            cryptoId?.let {
                CryptoDetailScreen(cryptoId)
            }
        }
    }
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

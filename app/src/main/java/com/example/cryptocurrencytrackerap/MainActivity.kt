package com.example.cryptocurrencytrackerap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cryptocurrencytrackerap.ui.theme.CurrencyAppTheme
import com.example.cryptocurrencytrackerap.util.CryptoCurrencyApp

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CurrencyAppTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize()
                        .padding(WindowInsets.navigationBars.asPaddingValues()),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CryptoCurrencyApp()
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CryptoToolbar() {
//
//    TopAppBar(
//        title = {
//            Text(
//                text = stringResource(id = R.string.app_name),
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(10.dp)
//            )
//        }
//    )
//
//}






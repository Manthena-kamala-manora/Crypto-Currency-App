package com.example.cryptocurrencytrackerap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cryptocurrencytrackerap.ui.composables.CryptoDetailDestination
import com.example.cryptocurrencytrackerap.ui.composables.CryptoDetailScreen
import com.example.cryptocurrencytrackerap.ui.composables.CryptoListScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = CryptoDetailDestination.route, modifier = modifier
    ) {
            composable(route = CryptoDetailDestination.route) {
                CryptoListScreen(
                    onItemClick = { cryptoId ->
                        navController.navigate(CryptoDetailDestination.routeArgs + cryptoId)
                    }
                )
            }
            composable(route = CryptoDetailDestination.routeDetailArgs) { backStackEntry ->
                val cryptoId = backStackEntry.arguments?.getString("cryptoId")
                cryptoId?.let {
                    CryptoDetailScreen(cryptoId)
                }
            }
        }
    }

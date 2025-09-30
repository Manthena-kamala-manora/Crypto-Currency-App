package com.example.cryptocurrencytrackerap

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.cryptocurrencytrackerap.ui.composables.CryptoListScreen
import org.junit.Rule
import org.junit.Test

class CryptoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun cryptoListScreen_displaysTitle() {
        composeTestRule.setContent {
            CryptoListScreen(onItemClick = "")
        }

        composeTestRule.onNodeWithText("Crypto List").assertExists()
    }
}
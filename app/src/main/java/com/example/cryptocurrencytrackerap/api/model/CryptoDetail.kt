package com.example.cryptocurrencytrackerap.api.model

data class CryptoDetail(
    val id: String,
    val name: String,
    val image: ImageData,
    val description: DescriptionData,
    val market_data: MarketData
)

data class ImageData(val large: String)
data class DescriptionData(val en: String)
data class MarketData(
    val market_cap: Map<String, Double>,
    val total_volume: Map<String, Double>,
    val price_change_percentage_24h: Double
)


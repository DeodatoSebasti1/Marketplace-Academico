package com.example.frontend.model

data class StripePaymentRequest(
    val amount: Long,
    val currency: String = "eur"
)

data class StripePaymentIntentResponse(
    val clientSecret: String
)

package com.example.frontend.ui.checkout


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.FinalizarCompraRequest
import com.example.frontend.model.StripePaymentIntentResponse
import com.example.frontend.model.StripePaymentRequest
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val _clientSecret = MutableStateFlow<String?>(null)
    val clientSecret: StateFlow<String?> = _clientSecret

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun criarPagamentoStripe(amount: Long) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val response = RetrofitClient.api.createStripePaymentIntent(
                    StripePaymentRequest(amount = amount)
                )

                if (response.isSuccessful) {
                    _clientSecret.value = response.body()?.clientSecret
                } else {
                    _clientSecret.value = null
                }

            } catch (e: Exception) {
                _clientSecret.value = null
            } finally {
                _loading.value = false
            }
        }
    }
    fun registrarCompra(idProduto: Long, idComprador: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.finalizarCompra(
                    FinalizarCompraRequest(idProduto, idComprador)
                )
                if (!response.isSuccessful) {
                    println("⚠ Erro ao registrar compra: ${response.code()}")
                }
            } catch (e: Exception) {
                println("⚠ Exception ao registrar compra: ${e.localizedMessage}")
            }
        }
    }

    fun reset() {
        _clientSecret.value = null
    }
}

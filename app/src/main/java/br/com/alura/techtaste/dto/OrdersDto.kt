package br.com.alura.techtaste.dto

import br.com.alura.techtaste.models.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrdersDto(val orders: List<Order>)
package br.com.alura.techtaste.dtos

import br.com.alura.techtaste.models.Order
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
class OrderResponse(
    val orders: List<OrderDto> = emptyList()
)

@Serializable
class OrderDto(
    val name: String,
    val price: String,
    val description: String
)

fun OrderDto.toOrder(
    image: String? = null
) = Order(
    name = name,
    price = BigDecimal(price),
    description = description,
    image = image
)
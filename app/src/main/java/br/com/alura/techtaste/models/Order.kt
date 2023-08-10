package br.com.alura.techtaste.models

import br.com.alura.techtaste.serializer.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Order(
    val name: String,
    @Serializable(BigDecimalSerializer::class)
    val price: BigDecimal,
    val description: String,
    val image: String? = null
)
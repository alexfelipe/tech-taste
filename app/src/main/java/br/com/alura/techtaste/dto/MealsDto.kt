package br.com.alura.techtaste.dto

import br.com.alura.techtaste.models.Meal
import kotlinx.serialization.Serializable

@Serializable
data class MealsDto(val meals: List<Meal>)
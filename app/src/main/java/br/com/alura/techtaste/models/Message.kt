package br.com.alura.techtaste.models

import br.com.alura.techtaste.ui.viewmodels.Refeicao

data class Message(
    val text: String,
    val refeicoes: List<Refeicao> = emptyList(),
    val isAuthor: Boolean = true
)
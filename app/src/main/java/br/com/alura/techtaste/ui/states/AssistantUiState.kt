package br.com.alura.techtaste.ui.states

import br.com.alura.techtaste.models.Message

data class AssistantUiState(
    val messages: List<Message> = emptyList(),
    val isOpenOpenIaDialog: Boolean = false,
    val error: AssistanteError? = null
)

data class AssistanteError(
    val message: String,
    val throwable: Throwable
)
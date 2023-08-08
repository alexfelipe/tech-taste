package br.com.alura.techtaste.models

data class Message(
    val text: String,
    val meals: List<Meal> = emptyList(),
    val isAuthor: Boolean = true,
    val isLoading: Boolean = false,
    val error: MessageError? = null
)

data class MessageError(
    val message: String,
    val throwable: Throwable? = null
)
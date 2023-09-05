package br.com.alura.techtaste.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.techtaste.BuildConfig
import br.com.alura.techtaste.dtos.OrderResponse
import br.com.alura.techtaste.dtos.toOrder
import br.com.alura.techtaste.models.Message
import br.com.alura.techtaste.openai.OrdersOpenAiClient
import br.com.alura.techtaste.samples.sampleRandomImage
import br.com.alura.techtaste.ui.states.AssistantUiState
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class AssistantViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState = _uiState.asStateFlow()
    private var ordersOpenAiClient: OrdersOpenAiClient? = null

    init {
        ordersOpenAiClient = OrdersOpenAiClient(
            OpenAI(BuildConfig.API_KEY)
        )
        _uiState.update { currentState ->
            currentState.copy(
                onTextChange = {
                    _uiState.value = _uiState.value.copy(text = it)
                },
                onCleanText = {
                    _uiState.value = _uiState.value.copy(text = "")
                }
            )
        }
    }

    fun send(text: String) {
        _uiState.update {
            it.copy(
                messages = it.messages +
                        Message(text, isAuthor = true)
            )
        }
        viewModelScope.launch {
            val (message, orders) = ordersOpenAiClient?.getMessageAndOrders(text)
                ?: Pair("Infelizmente não encontramos o que pediu", emptyList())
            _uiState.update { currentState ->
                currentState.copy(
                    messages = _uiState.value.messages + Message(
                        text = message,
                        isAuthor = false,
                        orders = orders,
                    )
                )
            }
        }
    }

}



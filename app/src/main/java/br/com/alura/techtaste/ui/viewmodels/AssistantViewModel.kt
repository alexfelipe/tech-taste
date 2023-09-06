package br.com.alura.techtaste.ui.viewmodels

import android.util.Log
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
            OpenAI("dhjasdhkasjhr")
//            OpenAI(BuildConfig.API_KEY)
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
                        Message(text, isAuthor = true) +
                        Message("", isAuthor = false, isLoading = true)
            )
        }
        viewModelScope.launch {
                try {
                    val (message, orders) = ordersOpenAiClient?.getMessageAndOrders(text)
                        ?: Pair("Infelizmente não encontramos o que pediu", emptyList())
                    val messages = _uiState.value.messages.let { messages ->
                        if(messages.last().isLoading && !messages.last().isAuthor) {
                            messages.dropLast(1)
                        } else messages
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            messages = messages + Message(
                                text = message,
                                isAuthor = false,
                                orders = orders,
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.e("AssistantViewModel", "send: ", e)
                    val messages = _uiState.value.messages.let { messages ->
                        if(messages.last().isLoading && !messages.last().isAuthor) {
                            messages.dropLast(1)
                        } else messages
                    }
                    _uiState.update {currentState ->
                        currentState.copy(
                            messages = messages
                        )
                    }
                }
        }
    }

}



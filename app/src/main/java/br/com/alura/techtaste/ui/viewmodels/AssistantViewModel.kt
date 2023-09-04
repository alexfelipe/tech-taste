package br.com.alura.techtaste.ui.viewmodels

import android.util.Log
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.techtaste.BuildConfig
import br.com.alura.techtaste.models.Message
import br.com.alura.techtaste.models.Order
import br.com.alura.techtaste.samples.sampleOrders
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
import kotlin.random.Random

class AssistantViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState = _uiState.asStateFlow()
    private var openAI: OpenAI? = null

    init {
        openAI = OpenAI(BuildConfig.API_KEY)
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
            openAI?.let { openAI ->
                val message = openAI.chatCompletion(
                    request = ChatCompletionRequest(
                        model = ModelId("gpt-3.5-turbo"),
                        messages = listOf(
                            ChatMessage(
                                role = ChatRole.System,
                                content = "você será um assistente de restaurante"
                            ),
                            ChatMessage(
                                role = ChatRole.User,
                                content = text
                            )
                        )
                    )
                ).choices
                    .mapNotNull {
                        it.message.content
                    }.joinToString(separator = "")
                val orders = emptyList<Order>()
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

}



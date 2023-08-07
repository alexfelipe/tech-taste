package br.com.alura.techtaste.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.alura.techtaste.models.Message
import br.com.alura.techtaste.ui.states.AssistantUiState
import br.com.alura.techtaste.ui.states.AssistanteError
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AssistantViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AssistantUiState())
    private var openAI: OpenAI? = null

    @OptIn(BetaOpenAI::class)
    private val chatMessages =
        mutableListOf(ChatMessage(role = ChatRole.System, SYSTEM_MESSAGE))
    val uiState = _uiState.asStateFlow()

    init {
        openAI = OpenAI("")
    }

    @OptIn(BetaOpenAI::class)
    suspend fun send(text: String) {
        openAI?.let { openAi ->
            val chatCompletion = openAi
                .chatCompletion(request = createRequest(text))
            try {
                val message = chatCompletion.choices
                    .mapNotNull { it.message?.content }
                    .reduce { acc, item ->
                        acc + item
                    }
                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages +
                                Message(text = message, isAuthor = true)
                    )
                }

            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages -
                                currentState.messages.last(),
                        error = AssistanteError("Falha ao gerar mensagem", e)
                    )
                }
                delay(3000)
                _uiState.update { currentState ->
                    currentState.copy(
                        error = null
                    )
                }
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages
            )
        }
    }

    @OptIn(BetaOpenAI::class)
    private suspend fun sendToOpenIA(
        text: String,
        onPhraseChange: (String) -> Unit,
    ) {
        var phrase = ""
        val request = createRequest(text)
        openAI?.let {
            val chatCompletionChunk = it.chatCompletions(request)
            chatCompletionChunk.collect { chatCompletionChunk ->
                chatCompletionChunk.choices.forEach { chatChunk ->
                    chatChunk.delta?.content?.let { text ->
                        phrase += text
                    }
                    onPhraseChange(phrase)
                }
            }
        }
    }

    @OptIn(BetaOpenAI::class)
    private fun createRequest(
        text: String,
        modelId: String = "gpt-3.5-turbo"
    ): ChatCompletionRequest {
        chatMessages += ChatMessage(
            role = ChatRole.User,
            content = text
        )
        return ChatCompletionRequest(
            model = ModelId(modelId),
            messages = chatMessages
        )
    }


}

private val SYSTEM_MESSAGE = """
    Você vai ser um gerador de postagens para engajar a comunidade de tecnologia.

As postagens deve ter no máximo 255 caracteres e apenas a seguinte tag #AlexDev

A linguagem deve ser descontraída e objetiva, focando em informar o máximo possível com menos palavras de maneira informal.

Você deve apenas responder com a postagem com base no texto de entrada.
""".trimIndent()
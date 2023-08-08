package br.com.alura.techtaste.ui.viewmodels

import android.util.Log
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.ViewModel
import br.com.alura.techtaste.dto.MealsDto
import br.com.alura.techtaste.models.Message
import br.com.alura.techtaste.models.MessageError
import br.com.alura.techtaste.ui.states.AssistantUiState
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
import kotlinx.serialization.json.Json
import kotlin.random.Random

private const val SYSTEM_MESSAGE = ""

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

    suspend fun send(text: String) {
        _uiState.update {
            it.copy(
                messages = it.messages.filter { message -> message.error == null } +
                        Message(text, isAuthor = true) +
                        Message(
                            "",
                            isAuthor = false,
                            isLoading = true
                        )
            )
        }
        delay(Random.nextLong(500, 1000))
        sendMessage(text)
//        sendRequestToOpenAi(text)
    }

    private fun sendMessage(text: String) {
        try {
            if (text.isBlank()) {
                throw Exception("falha")
            }
            val jsonPattern = "\\{.*\\}".toRegex(RegexOption.DOT_MATCHES_ALL)
            val generatedResponse = generateResponse()
            val jsonMatch = jsonPattern.find(generatedResponse)
            jsonMatch?.value?.let { rawJson ->
                val json = Json { ignoreUnknownKeys = true }
                val meals = json.decodeFromString<MealsDto>(rawJson).meals
                val message = generatedResponse.substringBefore(rawJson).trim()
                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages.dropLast(1) + Message(
                            text = message,
                            isAuthor = false,
                            meals = meals
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AssistantViewModel", "sendRequestToOpenAi: ", e)
            val lastMessage = _uiState.value.messages.last()
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages.dropLast(1) + lastMessage.copy(
                        isLoading = false,
                        error = MessageError("falha ao carregar mensagem", e)
                    )
                )
            }
        }
    }

    private fun generateResponse(): String {
        return """
${LoremIpsum(Random.nextInt(10, 30)).values.first()}
            
$JSON
""".trimIndent()
    }

    @OptIn(BetaOpenAI::class)
    private suspend fun sendRequestToOpenAi(text: String) {
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
                Log.e("AssistantViewModel", "sendRequestToOpenAi: ", e)
                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages -
                                currentState.messages.last(),
                    )
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

    suspend fun retry() {
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages.dropLast(1) +
                        Message("", isLoading = true, isAuthor = false)
            )
        }
        delay(Random.nextLong(500, 1000))
        val lastMessage = _uiState.value.messages.last().text
        sendMessage(lastMessage)
    }

    fun deleteLast() {
        _uiState.update { currentState ->
            currentState.copy(messages = _uiState.value.messages.dropLast(1))
        }
    }

}

private const val JSON = """
{
  "meals": [
    {
      "name": "Hambúrguer Clássico",
      "description": "Um hambúrguer suculento com queijo, alface e molho especial.",
      "price": "12.99",
      "calories": 550
    },
    {
      "name": "Salada de Frango Grelhado",
      "description": "Uma salada fresca com frango grelhado, vegetais mistos e molho de limão.",
      "price": "9.99",
      "calories": 320
    },
    {
      "name": "Massa Carbonara",
      "description": "Massa cozida al dente com molho de creme de ovo, queijo parmesão e bacon crocante.",
      "price": "15.50",
      "calories": 720
    }
  ]
}
"""




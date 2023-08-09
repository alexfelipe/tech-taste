package br.com.alura.techtaste.ui.viewmodels

import android.util.Log
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.random.Random

private const val JSON = """
{
  "meals": [
    {
      "name": "Bife Grelhado",
      "description": "Um suculento bife grelhado acompanhado de legumes frescos.",
      "price": "35.90"
    },
    {
      "name": "Massa Carbonara",
      "description": "Massa cozida al dente com molho cremoso de queijo, bacon crocante e pimenta preta.",
      "price": "28.50"
    },
    {
      "name": "Salmão ao Molho de Maracujá",
      "description": "Filé de salmão grelhado coberto com molho agridoce de maracujá, servido com arroz selvagem.",
      "price": "42.75"
    },
    {
      "name": "Pizza Quatro Queijos",
      "description": "Pizza com uma deliciosa combinação de queijos: mussarela, gorgonzola, parmesão e provolone.",
      "price": "31.20"
    },
    {
      "name": "Salada Mediterrânea",
      "description": "Mix de folhas verdes, tomate, pepino, azeitonas, queijo feta e molho de azeite e ervas.",
      "price": "18.90"
    },
    {
      "name": "Frango Teriyaki",
      "description": "Peito de frango grelhado com molho teriyaki, acompanhado de arroz branco e legumes salteados.",
      "price": "29.75"
    },
    {
      "name": "Hambúrguer Clássico",
      "description": "Hambúrguer suculento com alface, tomate, cebola, queijo cheddar e molho especial, no pão brioche.",
      "price": "21.50"
    },
    {
      "name": "Tempura de Vegetais",
      "description": "Seleção crocante de legumes variados em massa de tempura, servido com molho de imersão.",
      "price": "16.25"
    },
    {
      "name": "Risoto de Cogumelos",
      "description": "Risoto cremoso preparado com arroz arbóreo e uma variedade de cogumelos frescos.",
      "price": "27.80"
    },
    {
      "name": "Sundae de Chocolate",
      "description": "Sorvete de baunilha coberto com calda quente de chocolate, chantilly e amêndoas torradas.",
      "price": "12.00"
    }
  ]
}
"""

private const val SYSTEM_MESSAGE = """
Você será um assistente de App de restaurante. Você receberá solicitações para sugerir refeições e deverá escolher a refeição adequada com base na solicitação.
Todas as respostas referente às refeições devem seguir o formato de JSON, seguindo esse exemplo:

{
  "meals": [
    {
      "name": "",
      "description": "",
      "price": ""
    },
    {
      "name": "",
      "description": "",
      "price": ""
    },
  ]
}

Você deve se atentar aos pedidos e apresentar o te pedirem apenas! Abaixo está a base de dados para você decidir o que deve ser sugerido:

$JSON
"""


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
    }

    private fun sendMessage(text: String) {

        viewModelScope.launch {
            try {
                sendRequestToOpenAi(text)?.let { generatedResponse ->
                    val jsonPattern = "\\{.*\\}".toRegex(RegexOption.DOT_MATCHES_ALL)
                    val jsonMatch = jsonPattern.find(generatedResponse)
                    jsonMatch?.value?.let { rawJson ->
                        val json = Json { ignoreUnknownKeys = true }
                        val meals = json.decodeFromString<MealsDto>(rawJson).meals
                        val message = generatedResponse.substringBefore(rawJson).trim()
                        _uiState.update { currentState ->
                            currentState.copy(
                                messages = currentState.messages.dropLast(1) + Message(
                                    text = generatedResponse,
                                    isAuthor = false,
                                    meals = meals
                                )
                            )
                        }
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
    }

    @OptIn(BetaOpenAI::class)
    private suspend fun sendRequestToOpenAi(text: String): String? {
        return openAI?.let { openAi ->
            val chatCompletion = openAi
                .chatCompletion(request = createRequest(text))
            try {
                chatCompletion.choices
                    .mapNotNull { it.message?.content }
                    .reduce { acc, item ->
                        acc + item
                    }.also {
                        Log.i("AssistantViewModel", "sendRequestToOpenAi: $it")
                    }
            } catch (e: Exception) {
                Log.e("AssistantViewModel", "sendRequestToOpenAi: ", e)
                _uiState.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages -
                                currentState.messages.last(),
                    )
                }
                null
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




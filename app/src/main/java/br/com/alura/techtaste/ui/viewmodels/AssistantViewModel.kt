package br.com.alura.techtaste.ui.viewmodels

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

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

        val jsonPattern = "\\{.*\\}".toRegex(RegexOption.DOT_MATCHES_ALL)
        val generatedResponse = generateResponse()
        val jsonMatch = jsonPattern.find(generatedResponse)
        jsonMatch?.value?.let { rawJson ->
            val json = Json { ignoreUnknownKeys = true }
            val refeicoesList = json.decodeFromString<RefeicoesList>(rawJson)

            val refeicoes = refeicoesList.refeicoes
            _uiState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages +
                            Message(text, isAuthor = true) +
                            Message(
                                generatedResponse.substringBefore(rawJson),
                                refeicoes = refeicoes,
                                isAuthor = false
                            )
                )
            }


        }
//        sendRequestToOpenAi(text)
    }

    private fun generateResponse(): String {
        return """
${LoremIpsum(Random.nextInt(10, 30)).values.first()}
            
$json
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

val json = """
{
  "refeicoes": [
    {
      "nome": "Hambúrguer Clássico",
      "descricao": "Um hambúrguer suculento com queijo, alface e molho especial.",
      "preco": 12.99,
      "calorias": 550
    },
    {
      "nome": "Salada de Frango Grelhado",
      "descricao": "Uma salada fresca com frango grelhado, vegetais mistos e molho de limão.",
      "preco": 9.99,
      "calorias": 320
    },
    {
      "nome": "Massa Carbonara",
      "descricao": "Massa cozida al dente com molho de creme de ovo, queijo parmesão e bacon crocante.",
      "preco": 15.50,
      "calorias": 720
    }
  ]
}
"""


private val SYSTEM_MESSAGE = """
    Você vai ser um gerador de postagens para engajar a comunidade de tecnologia.

As postagens deve ter no máximo 255 caracteres e apenas a seguinte tag #AlexDev

A linguagem deve ser descontraída e objetiva, focando em informar o máximo possível com menos palavras de maneira informal.

Você deve apenas responder com a postagem com base no texto de entrada.
""".trimIndent()

@Serializable
data class Refeicao(
    val nome: String,
    val descricao: String,
    val preco: Double,
    val calorias: Int
)

@Serializable
data class RefeicoesList(val refeicoes: List<Refeicao>)
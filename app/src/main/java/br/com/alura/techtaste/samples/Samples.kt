package br.com.alura.techtaste.samples

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.alura.techtaste.models.Message
import br.com.alura.techtaste.ui.viewmodels.Refeicao
import kotlin.random.Random

val sampleMessages = List(10) {
    Message(
        text = LoremIpsum(Random.nextInt(5, 20)).values.first(),
        isAuthor = it % 2 == 0,
        refeicoes = if (it % 2 != 0) listOf(
            Refeicao(
                nome = "arroz",
                descricao = "integral",
                preco = 20.0,
                calorias = 100
            ),
            Refeicao(
                nome = "feijão",
                descricao = "carioca",
                preco = 8.0,
                calorias = 90
            ),
        ) else emptyList()
    )
}
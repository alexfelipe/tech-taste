package br.com.alura.techtaste.samples

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.alura.techtaste.models.Message
import kotlin.random.Random

val sampleMessages = List(10) {
    Message(
        text = LoremIpsum(Random.nextInt(5, 20)).values.first(),
        isAuthor = it % 2 == 0
    )
}
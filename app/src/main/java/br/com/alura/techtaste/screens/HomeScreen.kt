package br.com.alura.techtaste.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alura.techtaste.R
import br.com.alura.techtaste.models.Meal
import br.com.alura.techtaste.ui.theme.Cinza1
import br.com.alura.techtaste.ui.theme.Cinza3
import br.com.alura.techtaste.ui.theme.LaranjaMedio
import br.com.alura.techtaste.ui.theme.TechTasteTheme
import coil.compose.AsyncImage
import java.math.BigDecimal
import java.math.MathContext
import kotlin.random.Random

val categories = mapOf(
    R.drawable.petiscos to "Petiscos",
    R.drawable.principais to "Principais",
    R.drawable.massas to "Massas",
    R.drawable.sobremesas to "Sobremesas",
    R.drawable.bebidas to "Bebidas",
)

val meals = List(10) {
    Meal(
        name = LoremIpsum(Random.nextInt(2, 10)).values.first(),
        description = LoremIpsum(Random.nextInt(10, 20)).values.first(),
        price = BigDecimal(Random.nextDouble(300.0)),
        calories = Random.nextDouble(3000.0)
    )
}

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            R.drawable.app_banner,
            contentDescription = "banner principal",
            Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(Color.Gray)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                R.drawable.app_logo,
                contentDescription = "logo do início",
                Modifier
                    .align(
                        Alignment.Center
                    )
                    .width(150.dp)
                    .padding(vertical = 24.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.Gray)
            )
        }
        CategoriesSection(categories)
        MealSection(title = {
            Text(
                text = "Mais populares",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }, Modifier.padding(24.dp)) {
            for (meal in meals.shuffled().subList(1, 3)) {
                Column(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cinza1),
                ) {
                    AsyncImage(
                        model = "https://picsum.photos/${Random.nextInt(1280, 1920)}/${
                            Random.nextInt(
                                720,
                                1920
                            )
                        }",
                        contentDescription = "imagem da refeição",
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp
                                )
                            ),
                        contentScale = ContentScale.Crop,
                        placeholder = ColorPainter(Color.Gray),
                    )
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = meal.name,
                            color = LaranjaMedio,
                            fontSize = 16.sp
                        )
                        Text(
                            text = meal.price.round(MathContext(2)).toPlainString(),
                            color = Cinza3,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = meal.description,
                            color = Cinza3,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Ver mais")
            }
        }
        Spacer(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color(0xFF49454F))
                .alpha(0.5f)
        )
        MealSection(title = {
            Text(
                text = "Delícias vegeratianas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }, Modifier.padding(24.dp)) {
            for (meal in meals.shuffled().subList(1, 3)) {
                Column(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cinza1),
                ) {
                    AsyncImage(
                        model = "https://picsum.photos/${Random.nextInt(1280, 1920)}/${
                            Random.nextInt(
                                720,
                                1920
                            )
                        }",
                        contentDescription = "imagem da refeição",
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp
                                )
                            ),
                        contentScale = ContentScale.Crop,
                        placeholder = ColorPainter(Color.Gray),
                    )
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = meal.name,
                            color = LaranjaMedio,
                            fontSize = 16.sp
                        )
                        Text(
                            text = meal.price.round(MathContext(2)).toPlainString(),
                            color = Cinza3,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = meal.description,
                            color = Cinza3,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Ver mais")
            }
        }
    }
}

@Composable
fun MealSection(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        title()
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun CategoriesSection(categories: Map<Int, String>) {
    Text(
        text = "Escolha por categoria",
        Modifier.padding(horizontal = 24.dp),
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )
    LazyRow(
        Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        categories.forEach { (image, label) ->
            item {
                Box(
                    modifier = Modifier
                        .background(
                            Cinza1,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                        .height(88.dp)
                        .widthIn(
                            min = 88.dp,
                            max = 100.dp
                        ),
                ) {
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            image,
                            contentDescription = "imagem da categoria",
                            Modifier.size(48.dp)
                        )
                        Text(text = label)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    TechTasteTheme {
        Surface(
            Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}
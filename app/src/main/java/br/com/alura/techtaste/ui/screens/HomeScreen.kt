package br.com.alura.techtaste.ui.screens

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import br.com.alura.techtaste.extensions.toBrazilianCurrency
import br.com.alura.techtaste.models.Meal
import br.com.alura.techtaste.samples.sampleCategories
import br.com.alura.techtaste.samples.sampleMeals
import br.com.alura.techtaste.samples.sampleRandomImage
import br.com.alura.techtaste.ui.components.MainBanner
import br.com.alura.techtaste.ui.theme.Gray1
import br.com.alura.techtaste.ui.theme.Gray3
import br.com.alura.techtaste.ui.theme.MediumOrange
import br.com.alura.techtaste.ui.theme.TechTasteTheme
import coil.compose.AsyncImage
import kotlin.random.Random

@Composable
fun HomeScreen(
    mealsSection: Map<String, List<Meal>>,
    categories: Map<Int, String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MainBanner()
        Spacer(modifier = Modifier.height(48.dp))
        CategoriesSection(categories)
        mealsSection.forEach { entry ->
            val title = entry.key
            val meals = entry.value
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (meal in meals) {
                        Column(
                            Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Gray1),
                        ) {
                            AsyncImage(
                                model = sampleRandomImage,
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
                                    color = MediumOrange,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = meal.price.toBrazilianCurrency(),
                                    color = Gray3,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = meal.description,
                                    color = Gray3,
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
        Spacer(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color(0xFF49454F))
                .alpha(0.5f)
        )
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
                            Gray1,
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
                            Modifier.size(48.dp),
                            placeholder = ColorPainter(Color.Gray)
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
            HomeScreen(
                mealsSection = mapOf(
                    LoremIpsum(Random.nextInt(1, 5)).values.first() to sampleMeals.shuffled(),
                    LoremIpsum(Random.nextInt(1, 5)).values.first() to sampleMeals.shuffled()
                ),
                categories = sampleCategories
            )
        }
    }
}
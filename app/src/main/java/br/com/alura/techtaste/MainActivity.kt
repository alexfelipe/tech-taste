package br.com.alura.techtaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.techtaste.navigation.Routes
import br.com.alura.techtaste.navigation.TechTasteNavHost
import br.com.alura.techtaste.ui.theme.MediumOrange
import br.com.alura.techtaste.ui.theme.TechTasteTheme
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechTasteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentBackStack by navController.currentBackStackEntryAsState()
                    val currentRoute = currentBackStack?.destination?.route
                    TechTasteApp(currentRoute, navController) {
                        TechTasteNavHost(navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun TechTasteApp(
    currentRoute: String?,
    navController: NavHostController = rememberNavController(),
    content: @Composable () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            when (currentRoute) {
                Routes.HOME -> {
                    LargeFloatingActionButton(
                        onClick = { navController.navigate(Routes.ASSISTANT) },
                        containerColor = MediumOrange
                    ) {
                        AsyncImage(
                            R.drawable.app_icon,
                            contentDescription = "ícone do floating action button",
                            Modifier.size(36.dp),
                            placeholder = painterResource(id = R.drawable.app_icon)
                        )
                    }
                }
            }
        }) {
        Box(Modifier.padding(it)) {
            content()
        }
    }
}

@Preview
@Composable
fun TechTasteAppInHomeRoutePreview() {
    TechTasteTheme {
        Surface(
            Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TechTasteApp(currentRoute = Routes.HOME) {
            }
        }
    }
}

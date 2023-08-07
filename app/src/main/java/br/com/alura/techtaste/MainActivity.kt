package br.com.alura.techtaste

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.techtaste.navigation.TechTasteNavHost
import br.com.alura.techtaste.screens.AssistantScreen
import br.com.alura.techtaste.screens.HomeScreen
import br.com.alura.techtaste.ui.theme.LaranjaMedio
import br.com.alura.techtaste.ui.theme.TechTasteTheme
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechTasteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Log.i("MainActivity", "onCreate: ${System.getenv("ALEX")}")
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
                "home" -> {
                    LargeFloatingActionButton(
                        onClick = { navController.navigate("assistant") },
                        containerColor = LaranjaMedio
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
fun TechTasteAppPreview() {
    TechTasteTheme {
        Surface(
            Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TechTasteApp(currentRoute = "") {
            }
        }
    }
}

package br.com.alura.techtaste.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alura.techtaste.R
import br.com.alura.techtaste.models.Message
import br.com.alura.techtaste.samples.sampleMessages
import br.com.alura.techtaste.ui.theme.Cinza1
import br.com.alura.techtaste.ui.theme.Cinza2
import br.com.alura.techtaste.ui.theme.LaranjaClaro
import br.com.alura.techtaste.ui.theme.LaranjaMedio
import br.com.alura.techtaste.ui.theme.TechTasteTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AssistantScreen(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onSendClick: (String) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            Row(
                Modifier
                    .height(68.dp)
                    .fillMaxWidth()
                    .background(
                        LaranjaMedio,
                        shape = RoundedCornerShape(bottomStart = 24.dp),
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    R.drawable.app_icon,
                    contentDescription = "ícone do floating action button",
                    Modifier
                        .padding(start = 24.dp)
                        .fillMaxHeight()
                        .width(36.dp),
                    placeholder = painterResource(id = R.drawable.app_icon)
                )
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    Modifier
                        .padding(end = 16.dp)
                        .clip(CircleShape)
                        .clickable {
                            onCloseClick()
                        },
                    tint = Cinza1,
                )
            }
            LazyColumn(
                Modifier.padding(bottom = 80.dp)
            ) {
                items(messages) { message ->
                    val alignment = if (message.isAuthor) {
                        Alignment.CenterEnd
                    } else {
                        Alignment.CenterStart
                    }
                    BoxWithConstraints(Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .widthIn(
                                    max = maxWidth / 1.3f
                                )
                                .align(alignment)
                                .background(
                                    Cinza1,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Text(
                                text = message.text,
                                Modifier.padding(8.dp),
                                color = LaranjaClaro
                            )
                        }
                    }
                }
            }
        }
        Row(
            Modifier
                .heightIn(80.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current
            val scope = rememberCoroutineScope()
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 16.dp
                    )
                    .weight(1f),
                placeholder = {
                    Text("Digite sua mensagem...")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = LaranjaMedio,
                    focusedBorderColor = LaranjaMedio,
                    cursorColor = LaranjaMedio,
                    focusedPlaceholderColor = Cinza2,
                    unfocusedPlaceholderColor = Cinza2,
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        LaranjaMedio,
                        shape = CircleShape
                    )
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    Modifier
                        .align(Alignment.Center)
                        .clickable {
                            scope.launch {
                                keyboardController?.hide()
                                delay(100)
                                onSendClick(text)
                                text = ""
                            }
                        },
                    tint = Cinza1
                )
            }
        }
    }
}

@Preview
@Composable
fun AssistantScreenPreview() {
    TechTasteTheme {
        Surface(
            Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box {
                AssistantScreen(
                    sampleMessages,
                    onCloseClick = {

                    },
                    onSendClick = {}
                )
            }
        }
    }
}
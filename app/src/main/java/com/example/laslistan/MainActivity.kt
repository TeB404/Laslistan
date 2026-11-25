package com.example.laslistan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.laslistan.ui.theme.LaslistanTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaslistanTheme {
                LaslistanScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LaslistanTheme {
        Greeting("Android")
    }
}

//test

data class Bok(
    val id: Int,
    val titel: String,
    val ar: Int,
    val genre: String,
    val sedd: Boolean = false,
)

val startbok = listOf(
    Bok(1, "Harry Potter: Den vises sten", 2001, "Fantasi"),
    Bok(2, "Tunnlar", 2007, "Äventyr"),
    Bok(3, "Matilda", 1988, "Fantasi")
)

@Composable
fun BildPlats(titel: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        .size(width = 80.dp, height = 120.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = titel.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun BokKort(
    bok : Bok,
    modifier: Modifier = Modifier,
) {

}

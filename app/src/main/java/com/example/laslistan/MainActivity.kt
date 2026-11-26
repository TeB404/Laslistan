package com.example.laslistan


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = modifier
        .size(width = 60.dp, height = 80.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = titel.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun BokKort(
    bok : Bok,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(1.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BildPlats(titel = bok.titel)
            Column(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .weight(1f)
            ) {
                Text(text = bok.titel, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${bok.ar} . ${bok.genre}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaslistanScreen(viewModel: LaslistanViewModel = viewModel()) {
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Läslistan")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(startbok, key = { it.id }) { bok ->
                BokKort(bok = bok)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LaslistanScreenPreview() {
    LaslistanTheme {
        LaslistanScreen(viewModel = LaslistanViewModel())
    }
}
package com.example.laslistan




import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

//test 100 år av problem senare:

data class Bok(
    val id: Int,
    val titel: String,
    val ar: Int,
    val genre: String,
    val sedd: Boolean = false,
)
@Composable
fun BildPlats(titel: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
        .size(width = 60.dp, height = 100.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color.Blue),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = titel.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Blue,
        )
    }
}

@Composable
fun BokKort(
    bok : Bok,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(1.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BildPlats(titel = bok.titel)
            Column(
                modifier = Modifier
                    .padding(start = 9.dp)
                    .weight(1f)
                    .border(4.dp, Color.Magenta)
                    .fillMaxWidth()
            ) {
                Text(text = bok.titel, style = MaterialTheme.typography.titleLargeEmphasized)
                Text(
                    text = "${bok.ar} . ${bok.genre}",
                    style = MaterialTheme.typography.bodyLargeEmphasized,
                )

                TextButton(onClick = onRemove) {
                    Text(
                        text = "Ta bort",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Checkbox(
                checked = bok.sedd,
                onCheckedChange = { onToggle() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaslistanScreen(viewModel: LaslistanViewModel = viewModel()) {
    val state = viewModel.uiState
    val attSe = state.bocker.filter { !it.sedd }
    val sedda = state.bocker.filter { it.sedd }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Läslistan")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Magenta,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            TextField(
                value = state.nyTitel,
                onValueChange = {viewModel.onNewTitleChange(it) },
                label = { Text("Titel") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.addBok() },
                enabled = state.nyTitel.isNotBlank(),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Text("Lägg till")
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 9.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text(
                        text = "Vill läsa: ",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 9.dp)
                    )
                }

                items(attSe, key = { it.id }) {bok ->
                    BokKort(
                        bok = bok,
                        onToggle = { viewModel.toggleSeen(bok.id) },
                        onRemove = { viewModel.removeBok(bok.id) },
                    )
                }

                items(sedda, key = { it.id }) {bok ->
                    BokKort(
                        bok = bok,
                        onToggle = { viewModel.toggleSeen(bok.id) },
                        onRemove = { viewModel.removeBok(bok.id) },
                    )
                }
            }
        }
        }

    }

@Preview(showBackground = true)
@Composable
fun LaslistanScreenPreview() {
    LaslistanTheme {
        LaslistanScreen()
    }
}
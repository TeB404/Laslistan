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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laslistan.ui.theme.LaslistanTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ButtonDefaults
import kotlinx.coroutines.selects.select


val TwilightBlack = Color(0xFF0A0A12)
val TwilightNavy = Color(0xFF131A2B)
val TwilightBlue = Color(0xFF3B82F6)
val TwilightPurple = Color(0xFF8B5CF6)
val TwilightMagenta = Color(0xFFD946EF)
val TwilightText = Color(0xFFE6E6FA)

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
    val kategori: String,
    val sedd: Boolean = false,
)
@Composable
fun BildPlats(titel: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
        .size(width = 60.dp, height = 100.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(TwilightBlue),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = titel.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.headlineLarge,
            color = TwilightText,
        )
    }
}

@Composable
fun BokKort(
    bok : Bok,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(1.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BildPlats(titel = bok.titel)
            Column(
                modifier = Modifier
                    .padding(start = 9.dp)
                    .weight(1f)
                    .border(
                        width = 2.dp,
                        color = TwilightPurple
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = bok.titel,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = bok.kategori,
                    style = MaterialTheme.typography.bodyLarge,
                )

                TextButton(onClick = onRemove) {
                    Text(
                        text = "Ta bort",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                TextButton(onClick = onEdit) {
                    Text("Ändra")
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val readCount = state.bocker.count { it.sedd }
    val filtered = when (state.filter) {
        "Lästa" -> state.bocker.sortedByDescending { it.sedd }
        "Olästa" -> state.bocker.sortedBy { it.sedd }
        else -> state.bocker
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Läslistan",
                        color = TwilightText
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TwilightNavy
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(TwilightBlack)
                .padding(10.dp)
        ) {
            TextField(
                value = state.nyTitel,
                onValueChange = { viewModel.onNewTitleChange(it) },
                label = { Text("Titel") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        selected = state.filter == filter,
                        onClick = { viewModel.onFilterChange(filter) },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TwilightMagenta,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                FilterChip(
                    selected = state.sortByTitle,
                    onClick = { viewModel.toggleSort() },
                    label = { Text("A-Ö") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TwilightMagenta,
                        selectedLabelColor = Color.White
                )
                )
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                kategorier.forEach { kategori ->
                    FilterChip(
                        selected = state.nyKategori == kategori,
                        onClick = { viewModel.onKategoriChange(kategori) },
                        label = { Text(kategori) }
                    )
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = TwilightPurple
                ),
                onClick = {
                    if (state.editingId == null) {
                        viewModel.addBok()
                    } else {
                        viewModel.saveEdit()
                    }
                },
                enabled = state.nyTitel.isNotBlank(),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    if (state.editingId == null)
                        "Lägg till"
                    else
                        "Spara"
                )
            }

            Text(
                text = "$readCount av ${state.bocker.size} lästa",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (state.bocker.isEmpty()) {
                Text(
                    text = "Hyllan är tom. Lägg till och börja läsa!",
                    modifier = Modifier.padding(top = 16.dp),
                )
            } else {
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

                    items(filtered, key = { it.id }) {bok ->
                        BokKort(
                            bok = bok,
                            onToggle = { viewModel.toggleSeen(bok.id)},

                            onRemove = {
                                viewModel.removeBok(bok.id)

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${bok.titel} togs bort",
                                        actionLabel = "Ångra",
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.undoRemove()
                                    }
                                }
                            },
                            onEdit = { viewModel.startEdit(bok) }
                        )
                    }
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
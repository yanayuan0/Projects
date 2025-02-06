// MainScreen.kt
package hu.ait.httpdemo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.ait.httpdemo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onRecipeAPISelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainViewModel: MainViewModel = hiltViewModel()

    val recipeSuggestions by mainViewModel.recipeSuggestions.collectAsState()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var newIngredients by rememberSaveable { mutableStateOf("") }
    var suggestionToEdit by rememberSaveable { mutableStateOf<RecipeSuggestion?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.recipe_recommendation)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.AddCircle, contentDescription = "Add")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (recipeSuggestions.isEmpty()) {
                Text(
                    stringResource(R.string.add_ingredients),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipeSuggestions) { suggestion ->
                        RecipeCard(
                            suggestion = suggestion,
                            onDelete = { mainViewModel.deleteSuggestion(suggestion) },
                            onEdit = {
                                suggestionToEdit = suggestion
                                newIngredients = suggestion.ingredients
                                showEditDialog = true
                            },
                            onClick = { onRecipeAPISelected(suggestion.ingredients) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text(stringResource(R.string.add_ing)) },
                text = {
                    Column {
                        TextField(
                            value = newIngredients,
                            onValueChange = { newIngredients = it },
                            singleLine = false,
                            placeholder = { Text(stringResource(R.string.enter_hint)) }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newIngredients.isNotBlank()) {
                            mainViewModel.addIngredients(newIngredients)
                            newIngredients = ""
                            showAddDialog = false
                        }
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showAddDialog = false
                        newIngredients = ""
                    }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

        // Edit Dialog
        if (showEditDialog && suggestionToEdit != null) {
            AlertDialog(
                onDismissRequest = {
                    showEditDialog = false
                    suggestionToEdit = null
                },
                title = { Text(stringResource(R.string.edit_ingredients)) },
                text = {
                    Column {
                        TextField(
                            value = newIngredients,
                            onValueChange = { newIngredients = it },
                            singleLine = false,
                            placeholder = { Text(stringResource(R.string.enter_new_hint)) }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newIngredients.isNotBlank()) {
                            mainViewModel.updateSuggestion(suggestionToEdit!!, newIngredients)
                            newIngredients = ""
                            suggestionToEdit = null
                            showEditDialog = false
                        }
                    }) {
                        Text(stringResource(R.string.save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showEditDialog = false
                        newIngredients = ""
                        suggestionToEdit = null
                    }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

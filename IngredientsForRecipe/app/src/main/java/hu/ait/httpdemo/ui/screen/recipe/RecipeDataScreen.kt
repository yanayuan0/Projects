// RecipeDataScreen.kt
package hu.ait.httpdemo.ui.screen.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.ait.httpdemo.data.recipe.RecipeResultItem
import hu.ait.httpdemo.data.instruction.InstructionResultItem
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import hu.ait.httpdemo.R

@Composable
fun RecipeDataScreen(
    recipe: RecipeResultItem,
    instructions: List<InstructionResultItem>,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Recipe Image and Title in Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                val imageUrl = recipe.image ?: ""
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Recipe Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recipe.title ?: "No Title",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ingredients Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.ingredients),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.used_ingredients),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                recipe.usedIngredients?.forEach { ingredient ->
                    Text("- ${ingredient?.original}", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.missed_ingredients),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                recipe.missedIngredients?.forEach { ingredient ->
                    Text("- ${ingredient?.original}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Instructions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.instructions),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))

                val steps = instructions.flatMap { it.steps ?: emptyList() }
                var number = 0
                if (steps.isNotEmpty()) {
                    steps.forEach { step ->
                        number += 1
                        Text(
                            "${number}. ${step?.step}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    Text("No instructions available", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

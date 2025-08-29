package com.example.smartpantryai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SmartPantryApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartPantryApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var userInput by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("Initializing...") }

    val tokenizer = remember(context) { SentencePieceTokenizer(context, "models/spiece.model") }

    LaunchedEffect(Unit) {
        tokenizer.initialize()
        OnDeviceRecipeGenerator.init(context)
        aiResponse = if (tokenizer.isReady() && OnDeviceRecipeGenerator.isReady()) "Ready!" else "Initialization failed."
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🥫 Smart Pantry AI", style = MaterialTheme.typography.headlineSmall)
        Text(aiResponse)
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Enter pantry items") },
            modifier = Modifier.fillMaxWidth(),
            enabled = tokenizer.isReady() && OnDeviceRecipeGenerator.isReady()
        )
        Button(
            onClick = {
                scope.launch {
                    val tokenIds = tokenizer.encode(userInput)
                    val recipeOutput = OnDeviceRecipeGenerator.generateRecipe(tokenIds)
                    val decoded = tokenizer.decode(tokenIds)
                    aiResponse = "Decoded input: $decoded\n$recipeOutput"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Get Recipe Suggestions") }
    }
}

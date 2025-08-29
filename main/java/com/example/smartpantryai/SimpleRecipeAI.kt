package com.example.smartpantryai

object SimpleRecipeAI {

    // Example mapping of ingredients to recipe suggestions
    private val recipes = mapOf(
        setOf("rice", "beans") to "🌮 Try making a simple Bean & Rice Burrito.",
        setOf("pasta", "tomato") to "🍝 Make a quick Tomato Pasta.",
        setOf("bread", "cheese") to "🧀 Grilled Cheese Sandwich is perfect.",
        setOf("egg", "milk") to "🥚 Make a delicious Omelette."
    )

    fun generateRecipe(input: String): String {
        val items = input.lowercase().split(",").map { it.trim() }.toSet()

        for ((ingredients, suggestion) in recipes) {
            if (items.containsAll(ingredients)) {
                return suggestion
            }
        }

        return "🤔 Couldn't find a perfect match, but try mixing your ingredients creatively!"
    }
}

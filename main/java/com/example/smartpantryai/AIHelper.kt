package com.example.smartpantryai

object AIHelper {
    private val recipes = mapOf(
        "milk" to "Make pancakes or add to cereal",
        "bread" to "Try a sandwich or toast",
        "eggs" to "Make an omelette or scramble",
        "cheese" to "Add to pasta or sandwiches"
    )

    private val foods = listOf("milk", "bread", "eggs", "cheese")

    fun extractFoodItems(input: String): List<String> {
        return foods.filter { input.lowercase().contains(it) }
    }

    fun getRecipe(foodName: String): String {
        return recipes[foodName.lowercase()] ?: "No recipe found"
    }
}

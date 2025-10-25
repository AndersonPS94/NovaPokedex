package com.anderson.hacksprintpokedex.data.model

import com.google.gson.annotations.SerializedName

// A language resource (e.g., "en", "es")
data class Language(
    val name: String,
    val url: String
)

// A translated name for a resource
data class Name(
    val name: String,
    val language: Language
)

// A generic response for endpoints that return a list of names
data class NamedResourceResponse(
    val names: List<Name>
)

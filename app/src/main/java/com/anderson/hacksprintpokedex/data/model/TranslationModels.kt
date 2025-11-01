package com.anderson.hacksprintpokedex.data.model

import com.google.gson.annotations.SerializedName

data class Language(
    val name: String,
    val url: String
)

data class Name(
    val name: String,
    val language: Language
)

data class NamedResourceResponse(
    val names: List<Name>
)

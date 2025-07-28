package com.example.hacksprintpokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val shinyImageUrl: String = "",
    val types: List<String>,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val ability: String = "",
    val region: String = "",
    val description: String = "",
    val genderRate: Int = 0
) : Parcelable
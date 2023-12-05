package com.github.burkclik.asplugin.ui

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


data class ABClassParams(
    val prefix: String,
    val abTestName: String,
    val packageName: String,
)

@Serializable
data class ScriptData(
    val title: String,
    val flags: List<Flags>
)

@Serializable
data class Flags(
    val name: String,
    val placeholder: String,
    val label: String,
    val hint: String,
)

@Serializable
data class Deneme(
    @SerializedName("text") val text: String
)
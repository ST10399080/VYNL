package com.example.vynl.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LastFmImage(
    @SerializedName("#text")
    val text: String?,
    val size: String?
)
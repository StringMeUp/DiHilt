package com.wla.models

import com.google.gson.annotations.SerializedName

data class ObjectId(
    val total: Int,
    @SerializedName("objectIDs")
    val artIds: List<Int>,
)
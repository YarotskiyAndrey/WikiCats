package com.task.wikicats.model

import java.io.Serializable

data class Cat(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnail: String,
    val image: String
) : Serializable
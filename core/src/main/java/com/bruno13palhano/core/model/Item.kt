package com.bruno13palhano.core.model

data class Item(
    val id: Long,
    val index: Int,
    val successorsIndices: List<Int>,
    val name: String,
    val description: String
)
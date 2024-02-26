package com.kyawzinlinn.speccomparer.domain.model.detail

data class SpecificationItem(
    val title: String,
    val specificationsColumn : List<SpecificationColumn>,
    val specificationsTable: List<SpecificationTable>
)

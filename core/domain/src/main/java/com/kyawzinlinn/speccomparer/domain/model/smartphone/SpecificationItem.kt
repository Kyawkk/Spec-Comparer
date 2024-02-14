package com.kyawzinlinn.speccomparer.domain.model.smartphone

data class SpecificationItem(
    val title: String,
    val specificationsColumn : List<SpecificationColumn>,
    val specificationsTable: List<SpecificationTable>
)

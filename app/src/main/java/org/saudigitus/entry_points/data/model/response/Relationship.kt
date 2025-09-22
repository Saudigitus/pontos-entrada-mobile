package org.saudigitus.entry_points.data.model.response


import com.fasterxml.jackson.annotation.JsonProperty

data class Relationship(
    @JsonProperty("from")
    val from: From,
    @JsonProperty("relationshipType")
    val relationshipType: String,
    @JsonProperty("to")
    val to: To
)
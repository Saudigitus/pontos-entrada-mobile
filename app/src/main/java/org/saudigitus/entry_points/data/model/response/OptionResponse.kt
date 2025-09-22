package org.saudigitus.entry_points.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class OptionResponse(
    @JsonProperty("options")
    val options: List<Option>
)
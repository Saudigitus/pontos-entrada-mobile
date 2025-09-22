package org.saudigitus.entry_points.data.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Enrollment(
    @JsonProperty("enrollment")
    val enrollment: String = "",
    @JsonProperty("events")
    val events: List<Event> = emptyList()
)
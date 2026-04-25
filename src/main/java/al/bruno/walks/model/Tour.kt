package al.bruno.walks.model

data class Tour(
    val id: String,
    val title: String,
    val description: String,
    val coordinate: Coordinate,
    val pointOfInterests: List<PointOfInterest>
)

package al.bruno.walks.model

data class PointOfInterest(
    val id: String,
    val title: String,
    val description: String,
    val coordinate: Coordinate,
    val images: List<Asset>,
    val categories: List<Category>
)

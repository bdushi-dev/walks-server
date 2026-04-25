package al.bruno.walks.model

data class Category(
    val id: String,
    val name: String,
    val key: String,
    val icon: Asset,
    val mapMarker: Asset,
    val visitedMapMarker: Asset,
    val sideBarColor: String
)

package al.bruno.walks.model

data class Audio(
    val id: String,
    val title: String,
    val description: String,
    val contentType: String,
    val size: Double,
    val url: String
)

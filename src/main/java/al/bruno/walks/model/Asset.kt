package al.bruno.walks.model

data class Asset(
    private val sys: Sys,
    val title: String,
    val description: String,
    val contentType: String,
    val size: Double,
    val url: String
) {
    val id: String get() = sys.id
}
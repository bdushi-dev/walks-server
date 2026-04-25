package al.bruno.walks.model

data class Home(
    private val sys: Sys,
    val title: String,
    val description: String,
    val banner: Asset
) {
    val id: String get() = sys.id
}
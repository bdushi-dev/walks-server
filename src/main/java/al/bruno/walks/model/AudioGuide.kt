package al.bruno.walks.model

data class AudioGuide(
    val id: String,
    val title: String,
    val text: String,
    val audios: List<Audio>,
    val image: Asset
)

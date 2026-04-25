package al.bruno.walks.model

data class Onboarding(
    val id: String,
    val title: String,
    val description: String,
    val order: Int,
    val androidImage: Asset
)
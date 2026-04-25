package al.bruno.walks.graphql

/**
 * Reusable GraphQL query fragments for Contentful.
 * Locale is injected at runtime from ContentfulProperties.
 */
object Queries {

    private const val ASSET_FIELDS = """
        sys { id }
        title
        description
        contentType
        size
        url
    """

    private const val AUDIO_FIELDS = """
        sys { id }
        title
        description
        contentType
        size
        url
    """

    private const val COORDINATE_FIELDS = """
        lat
        lon
    """

    private val CATEGORY_FIELDS = """
        sys { id }
        name
        key
        sideBarColor
        icon { $ASSET_FIELDS }
        mapMarker { $ASSET_FIELDS }
        visitedMapMarker { $ASSET_FIELDS }
    """.trimIndent()

    private val POI_FIELDS = """
        sys { id }
        title
        description
        coordinate { $COORDINATE_FIELDS }
        imagesCollection { items { $ASSET_FIELDS } }
        categoriesCollection { items { $CATEGORY_FIELDS } }
    """.trimIndent()

    // ─── Tour ────────────────────────────────────────────────────────────────

    fun tourById(locale: String) = $$"""
        query TourById($id: String!) {
            tour(locale: "$$locale", id: $id) {
                sys { id }
                title
                description
                coordinate { $$COORDINATE_FIELDS }
                pointOfInterestCollection(locale: "$$locale") {
                    items { $$POI_FIELDS }
                }
            }
        }
    """.trimIndent()

    fun toursByIds(locale: String) = $$"""
        query ToursByIds($ids: [String!]) {
            tourCollection(locale: "$$locale", where: { sys: { id_in: $ids } }) {
                items {
                    sys { id }
                    title
                    description
                    coordinate { $$COORDINATE_FIELDS }
                    pointOfInterestCollection(locale: "$$locale") {
                        items { $$POI_FIELDS }
                    }
                }
            }
        }
    """.trimIndent()

    // ─── PointOfInterest ─────────────────────────────────────────────────────

    fun poiById(locale: String) = $$"""
        query PoiById($id: String!) {
            pointOfInterest(locale: "$$locale", id: $id) {
                $$POI_FIELDS
            }
        }
    """.trimIndent()

    fun poiCollection(locale: String) = $$"""
        query PoiCollection($limit: Int, $categoryName: String) {
            pointOfInterestCollection(
                locale: "$$locale",
                limit: $limit,
                where: { categories: { name: $categoryName } }
            ) {
                items { $$POI_FIELDS }
            }
        }
    """.trimIndent()

    // ─── Region ──────────────────────────────────────────────────────────────

    fun regionById(locale: String) = $$"""
        query RegionById($id: String!) {
            region(locale: "$$locale", id: $id) {
                sys { id }
                title
                description
                coordinate { $$COORDINATE_FIELDS }
                image(locale: "$$locale") { $$ASSET_FIELDS }
                pointOfInterestCollection(locale: "$$locale") { items { $$POI_FIELDS } }
                popularPointOfInterestCollection(locale: "$$locale") { items { $$POI_FIELDS } }
                tourCollection(locale: "$$locale") { items {
                    sys { id }
                    title
                    description
                    coordinate { $$COORDINATE_FIELDS }
                    pointOfInterestCollection(locale: "$$locale") { items { $$POI_FIELDS } }
                }}
            }
        }
    """.trimIndent()

    fun regionCollection(locale: String) = """
        query RegionCollection {
            regionCollection(locale: "$locale", limit: 25) {
                items {
                    sys { id }
                    title
                    description
                    coordinate { $COORDINATE_FIELDS }
                    image(locale: "$locale") { $ASSET_FIELDS }
                    pointOfInterestCollection(locale: "$locale") { items { $POI_FIELDS } }
                    popularPointOfInterestCollection(locale: "$locale") { items { $POI_FIELDS } }
                    tourCollection(locale: "$locale") { items {
                        sys { id }
                        title
                        description
                        coordinate { $COORDINATE_FIELDS }
                        pointOfInterestCollection(locale: "$locale") { items { $POI_FIELDS } }
                    }}
                }
            }
        }
    """.trimIndent()

    // ─── Category ────────────────────────────────────────────────────────────

    fun categoryCollection(locale: String) = """
        query CategoryCollection {
            categoryCollection(locale: "$locale") {
                items { $CATEGORY_FIELDS }
            }
        }
    """.trimIndent()

    // ─── AudioGuide ──────────────────────────────────────────────────────────

    fun audioGuideById(locale: String) = """
        query AudioGuideById(${'$'}id: String!) {
            audioGuide(locale: "$locale", id: ${'$'}id) {
                sys { id }
                title
                text(locale: "$locale")
                audioCollection { items { $AUDIO_FIELDS } }
                image { $ASSET_FIELDS }
            }
        }
    """.trimIndent()

    // ─── Onboarding ──────────────────────────────────────────────────────────

    fun onboardingCollection(locale: String) = """
        query OnboardingCollection {
            onboardingCollection(locale: "$locale") {
                items {
                    sys { id }
                    title(locale: "$locale")
                    description(locale: "$locale")
                    order(locale: "$locale")
                    androidImage(locale: "$locale") { $ASSET_FIELDS }
                }
            }
        }
    """.trimIndent()

    // ─── Home ────────────────────────────────────────────────────────────────

    fun homeById(locale: String) = """
        query HomeById(${'$'}id: String!) {
            home(locale: "$locale", id: ${'$'}id) {
                sys { id }
                title(locale: "$locale")
                description(locale: "$locale")
                banner(locale: "$locale") { $ASSET_FIELDS }
            }
        }
    """.trimIndent()

    fun homeCollection(locale: String) = """
        query HomeCollection {
            homeCollection(locale: "$locale") {
                items {
                    sys { id }
                    title(locale: "$locale")
                    description(locale: "$locale")
                    banner(locale: "$locale") { $ASSET_FIELDS }
                }
            }
        }
    """.trimIndent()
}
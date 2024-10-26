package dev.mbo.springkotlintemplating

/**
 * Filename needs to have placeholder %LANG% which is replaced automatically by Templating.
 */
enum class LocalTemplate(
    private val filename: String,
) : ClasspathTemplate {
    TEST("test_%LANG%.ftl"), ;

    override fun getFilename(): String {
        return filename
    }
}

package dev.mbo.springkotlintemplating

data class TemplateInfo(
    val template: ClasspathTemplate,
    val data: Map<String, Any?> = mapOf()
)
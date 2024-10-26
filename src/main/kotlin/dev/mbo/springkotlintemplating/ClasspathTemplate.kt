package dev.mbo.springkotlintemplating

interface ClasspathTemplate {
    fun getFilename(): String
    fun getInitialBufferLength(): Int {
        return 1024
    }
}

package dev.mbo.springkotlintemplating

import dev.mbo.logging.logger
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler
import freemarker.template.TemplateNotFoundException
import freemarker.template.Version
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.Locale

@Service
class TemplatingService(
    @Value("\${app.templating.classpath-dir:/freemarker}")
    private val templateDir: String,
    @Value("\${app.templating.freemarker-version:2.3.34}")
    private val freemarkerVersion: String,
) {

    companion object {
        private val DEFAULT_LOCALE: Locale = Locale.ENGLISH
    }

    private val log = logger()
    private val config: Configuration = Configuration(Version(freemarkerVersion))

    @PostConstruct
    fun init() {
        config.setClassForTemplateLoading(
            javaClass,
            templateDir
        )
        config.defaultEncoding = StandardCharsets.UTF_8.toString()
        config.locale = DEFAULT_LOCALE
        config.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        log.info(
            "using freemarker template directory: {}",
            templateDir
        )
    }

    /**
     * Wrapper for #getText(ClasspathTemplate,Locale,Map)
     */
    fun getText(info: TemplateInfo, locale: Locale? = null): String {
        return getText(template = info.template, locale = locale, data = info.data)
    }

    /**
     * Get a text from file template with replaced values from data map.
     *
     * @param template Config of the file in the configured classpath folder including the ending.
     * @param locale The language the text should be. Requires the filename to have a %LANG% placeholder with which the
     *               language of this locale will be replaced.
     * @param data       Map of key value pairs to replace in the template.
     * @return String with replaced values.
     */
    fun getText(
        template: ClasspathTemplate,
        locale: Locale? = null,
        data: Map<String, Any?> = emptyMap()
    ): String {
        val usedLocale = locale ?: DEFAULT_LOCALE
        val freemarkerTemplate = config.getTemplate(
            template.getFilename().replace(
                "%LANG%",
                usedLocale.language
            )
        )
        return mapText(
            template.getInitialBufferLength(),
            freemarkerTemplate,
            usedLocale,
            data
        )
    }

    private fun mapText(
        bufferSize: Int,
        template: Template,
        locale: Locale,
        data: Map<String, Any?>
    ): String {
        try {
            StringWriter(bufferSize).use { writer ->
                template.process(
                    data,
                    writer
                )
                writer.flush()
                return writer.toString().dropLastWhile { it == '\n' }
            }
        } catch (exc: TemplateNotFoundException) { // fallback to default locale if we can't find requested lang
            if (DEFAULT_LOCALE == locale) { // fail if already tried default locale
                throw IllegalStateException(
                    "template not found ${template.name}",
                    exc
                )
            }
            log.warn(
                "{} - falling back to default locale {}",
                exc.message?.replace(
                    "\n",
                    " "
                ),
                DEFAULT_LOCALE
            )
            return mapText(
                bufferSize,
                template,
                DEFAULT_LOCALE,
                data
            )
        } catch (exc: IOException) {
            throw IllegalStateException(
                "could not create text from template",
                exc
            )
        } catch (exc: TemplateException) {
            throw IllegalStateException(
                "could not create text from template",
                exc
            )
        }

    }

}

package dev.mbo.springkotlintemplating

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class TemplatingServiceTest @Autowired constructor(
    private val templatingService: TemplatingService,
) {

    @Test
    fun getText() {
        val info = TemplateInfo(
            LocalTemplate.TEST, mapOf("ts" to UUID.randomUUID())
        )
        val result = templatingService.getText(
            template = info.template,
            data = info.data
        )
        assertThat(result).startsWith("This is a test @ ${info.data["ts"]}")

        val result2 = templatingService.getText(info)
        assertThat(result).isEqualTo(result2)
    }
}
package dev.mbo.springkotlintemplating

import org.assertj.core.api.Assertions
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
        val uuid = UUID.randomUUID()
        val result = templatingService.getText(
            template = LocalTemplate.TEST,
            data = mapOf("ts" to uuid)
        )
        Assertions.assertThat(result).startsWith("This is a test @ $uuid")
    }
}
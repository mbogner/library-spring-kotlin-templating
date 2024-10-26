package dev.mbo.springkotlintemplating

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [ModuleTemplatingConfig::class])
internal open class ModuleTemplatingConfig

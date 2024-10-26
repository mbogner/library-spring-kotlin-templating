package dev.mbo.springkotlintemplating

import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Import(ModuleTemplatingConfig::class)
annotation class EnableModuleTemplating

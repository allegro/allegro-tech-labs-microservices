package pl.allegro.atl

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration



@Configuration
open class ApplicationConfig {
    @Bean
    open fun kotlinModule(): KotlinModule = KotlinModule()


}
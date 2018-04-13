package pl.allegro.atl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AtlDataServerApplication

fun main(args: Array<String>) {
    runApplication<AtlDataServerApplication>(*args)
}

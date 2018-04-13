package pl.allegro.atl

import io.codearte.jfairy.Fairy
import org.apache.commons.math3.random.RandomDataGenerator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
open class DescriptionController {

    private val fairy = Fairy.create()
    private val random =  RandomDataGenerator()

    @GetMapping(path = ["/descriptions/{id}"])
    open fun getDescription(@PathVariable("id") id: String): Description =
            Description(fairy.textProducer().latinSentence(random.nextInt(100, 200)))
}

data class Description(
        val description: String
)
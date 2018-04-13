package pl.allegro.atl

import io.codearte.jfairy.Fairy
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
open class GalleryController {

    private val fairy = Fairy.create()

    @GetMapping(path = ["/galleries/{id}"])
    open fun getGallery(@PathVariable("id") id: String): Gallery {
        val min = 1
        val max = min + fairy.baseProducer().randomInt(10)

        return IntRange(min, max).map {
            fairy.textProducer().latinSentence()
        }.let { Gallery(it) }
    }

}

data class Gallery(
        val imageUrls: List<String>
)
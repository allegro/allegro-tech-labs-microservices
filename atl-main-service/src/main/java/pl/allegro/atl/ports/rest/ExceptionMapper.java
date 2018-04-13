package pl.allegro.atl.ports.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.allegro.atl.adapters.description.DescriptionNotFoundException;
import pl.allegro.atl.adapters.gallery.GalleryNotFoundException;
import pl.allegro.atl.domain.OfferNotFoundException;

@ControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler
    public ResponseEntity<String> offerNotFoundException(OfferNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> galleryNotFoundException(GalleryNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> descriptionNotFoundException(DescriptionNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}

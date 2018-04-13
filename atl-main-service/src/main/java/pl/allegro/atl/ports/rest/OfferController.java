package pl.allegro.atl.ports.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.allegro.atl.domain.Offer;
import pl.allegro.atl.domain.OfferFacade;

@RestController
public class OfferController {

    private final OfferFacade offerFacade;

    public OfferController(OfferFacade offerFacade) {
        this.offerFacade = offerFacade;
    }

    @GetMapping(path = "/offers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Offer findOffer(@PathVariable("id") String id) {
        return offerFacade.findById(id);
    }
}

package pl.allegro.atl.domain;

import org.springframework.stereotype.Component;
import pl.allegro.atl.adapters.description.DescriptionApi;
import pl.allegro.atl.adapters.gallery.GalleryApi;
import pl.allegro.atl.adapters.mongodb.CoreOfferRepository;

@Component
class AggregatingOfferFacade implements OfferFacade {

    private final CoreOfferRepository offerRepository;
    private final GalleryApi galleryApi;
    private final DescriptionApi descriptionApi;

    AggregatingOfferFacade(CoreOfferRepository offerRepository, GalleryApi galleryApi, DescriptionApi descriptionApi) {
        this.offerRepository = offerRepository;
        this.galleryApi = galleryApi;
        this.descriptionApi = descriptionApi;
    }

    @Override
    public Offer findById(String id) {
        return offerRepository.findById(id)
                .map(BackingOffer.Builder::new)
                .map(builder -> builder.withDescription(descriptionApi.findDescriptionForOffer(id)))
                .map(builder -> builder.withGallery(galleryApi.findGalleryForOffer(id)))
                .map(BackingOffer.Builder::build)
                .orElseThrow(() -> new OfferNotFoundException(id));
    }
}

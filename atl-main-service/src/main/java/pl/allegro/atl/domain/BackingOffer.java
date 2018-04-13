package pl.allegro.atl.domain;

import pl.allegro.atl.adapters.description.Description;
import pl.allegro.atl.adapters.gallery.Gallery;
import pl.allegro.atl.adapters.mongodb.CoreOffer;

import java.math.BigDecimal;
import java.util.List;

class BackingOffer implements Offer {

    private final CoreOffer coreOffer;
    private final Gallery gallery;
    private final Description description;

    private BackingOffer(CoreOffer coreOffer, Gallery gallery, Description description) {
        this.coreOffer = coreOffer;
        this.gallery = gallery;
        this.description = description;
    }

    @Override
    public String getId() {
        return coreOffer.getId();
    }

    @Override
    public String getName() {
        return coreOffer.getName();
    }

    @Override
    public BigDecimal getPrice() {
        return coreOffer.getPrice();
    }

    @Override
    public String getDescription() {
        return description.getDescription();
    }

    @Override
    public List<String> getImageUrls() {
        return gallery.getImageUrls();
    }

    static class Builder {
        private final CoreOffer coreOffer;
        private Gallery gallery;
        private Description description;

        Builder(CoreOffer coreOffer) {
            this.coreOffer = coreOffer;
        }

        Builder withGallery(Gallery gallery) {
            this.gallery = gallery;
            return this;
        }

        Builder withDescription(Description description) {
            this.description = description;
            return this;
        }

        BackingOffer build() {
            return new BackingOffer(coreOffer, gallery, description);
        }
    }
}

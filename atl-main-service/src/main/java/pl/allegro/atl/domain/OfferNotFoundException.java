package pl.allegro.atl.domain;

public class OfferNotFoundException extends RuntimeException {

    public OfferNotFoundException(String offerId) {
        super("Offer with id: " + offerId + " was not found.");
    }
}

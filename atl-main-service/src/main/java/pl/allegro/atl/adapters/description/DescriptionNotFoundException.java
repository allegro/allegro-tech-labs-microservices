package pl.allegro.atl.adapters.description;

public class DescriptionNotFoundException extends RuntimeException {

    public DescriptionNotFoundException(String offerId) {
        super("Description for offer: " + offerId + " was not found.");
    }
}

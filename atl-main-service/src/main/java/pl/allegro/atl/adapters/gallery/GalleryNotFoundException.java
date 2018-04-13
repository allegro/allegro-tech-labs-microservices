package pl.allegro.atl.adapters.gallery;

public class GalleryNotFoundException extends RuntimeException {

    public GalleryNotFoundException(String offerId) {
        super("Gallery for offer: " + offerId + " was not found.");
    }
}

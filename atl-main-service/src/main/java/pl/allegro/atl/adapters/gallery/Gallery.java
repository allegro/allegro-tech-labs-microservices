package pl.allegro.atl.adapters.gallery;

import java.util.List;

public class Gallery {
    private final List<String> imageUrls;

    public Gallery(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    @Override
    public String toString() {
        return "Gallery{" +
                "imageUrls=" + imageUrls +
                '}';
    }
}

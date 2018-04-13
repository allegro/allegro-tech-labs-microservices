package pl.allegro.atl.adapters.gallery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pl.allegro.atl.adapters.common.ExternalDependencyAdapterException;
import pl.allegro.atl.adapters.common.ExternalDependencyCommunicationException;
import pl.allegro.atl.adapters.common.ExternalDependencyErrorException;

import java.util.List;

@Component
public class GalleryAdapter implements GalleryApi {

    private final RestTemplate restTemplate;
    private final String address;

    public GalleryAdapter(
            RestTemplate restTemplate,
            @Value("${adapters.gallery.address}") String address
    ) {
        this.restTemplate = restTemplate;
        this.address = address;
    }

    @Override
    @Timed(value = "adapters.gallery.find-by-id", histogram = true)
    public Gallery findGalleryForOffer(String offerId) {
        try {
            ResponseEntity<GalleryDto> response =
                    restTemplate.getForEntity(address + "/galleries/{offerId}", GalleryDto.class, offerId);

            return new Gallery(response.getBody().getImageUrls());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GalleryNotFoundException(offerId);
            } else {
                throw new ExternalDependencyAdapterException(
                        "Unable to fetch description for offer id: " + offerId + ". Response code: " + e.getRawStatusCode()
                );
            }
        } catch (HttpServerErrorException e) {
            throw new ExternalDependencyErrorException(
                    "Unable to fetch description for offer id: " + offerId + ". Response code: " + e.getRawStatusCode(),
                    e
            );
        } catch (Exception e) {
            throw new ExternalDependencyCommunicationException("Unable to fetch description for offer id: " + offerId, e);
        }
    }
}

class GalleryDto {
    private final List<String> imageUrls;

    @JsonCreator
    public GalleryDto(@JsonProperty("imageUrls") List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
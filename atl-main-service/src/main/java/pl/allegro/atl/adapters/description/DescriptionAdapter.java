package pl.allegro.atl.adapters.description;

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

@Component
public class DescriptionAdapter implements DescriptionApi {

    private final RestTemplate restTemplate;
    private final String address;

    public DescriptionAdapter(
            RestTemplate restTemplate,
            @Value("${adapters.description.address}") String address
    ) {
        this.restTemplate = restTemplate;
        this.address = address;
    }

    @Override
    @Timed(value = "adapters.description.find-by-id", histogram = true)
    public Description findDescriptionForOffer(String offerId) {
        try {
            ResponseEntity<DescriptionDto> response =
                    restTemplate.getForEntity(address + "/descriptions/{offerId}", DescriptionDto.class, offerId);

            return new Description(response.getBody().getDescription());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new DescriptionNotFoundException(offerId);
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


class DescriptionDto {
    private final String description;

    @JsonCreator
    public DescriptionDto(@JsonProperty("description") String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
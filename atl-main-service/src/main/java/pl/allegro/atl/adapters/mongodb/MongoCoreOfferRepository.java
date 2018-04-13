package pl.allegro.atl.adapters.mongodb;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
class MongoCoreOfferRepository implements CoreOfferRepository {

    private final PersistentOfferRepository repository;

    public MongoCoreOfferRepository(PersistentOfferRepository repository) {
        this.repository = repository;
    }

    @Override
    @Timed(value = "adapters.mongo.find-by-id", histogram = true)
    public Optional<CoreOffer> findById(String id) {
        return repository.findById(id).map(it ->
                new CoreOffer(
                        it.getId(),
                        it.getName(),
                        new BigDecimal(it.getPrice())
                )
        );
    }
}

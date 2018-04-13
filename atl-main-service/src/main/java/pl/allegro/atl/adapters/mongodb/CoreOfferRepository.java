package pl.allegro.atl.adapters.mongodb;

import java.util.Optional;

public interface CoreOfferRepository {

    Optional<CoreOffer> findById(String id);
}

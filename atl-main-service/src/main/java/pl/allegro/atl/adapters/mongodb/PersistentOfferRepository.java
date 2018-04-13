package pl.allegro.atl.adapters.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PersistentOfferRepository extends MongoRepository<PersistentOffer, String> {

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    List<PersistentOffer> findUsingPhrase(String phrase);
}

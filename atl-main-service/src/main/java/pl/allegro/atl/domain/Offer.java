package pl.allegro.atl.domain;

import java.math.BigDecimal;
import java.util.List;

public interface Offer {

    String getId();
    String getName();
    BigDecimal getPrice();
    String getDescription();
    List<String> getImageUrls();
}

package db_lab.model;

import db_lab.data.Material;
import db_lab.data.Product;
import db_lab.data.ProductPreview;
import db_lab.data.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// This is a mocked implementation that uses an in-memory map of products
// instead of connecting to a database.
// This can be handy to test out our application's look and feel without having
// to setup a database connection.
//
public final class MockedModel implements Model {

    private Map<ProductPreview, Product> previews;

    public MockedModel() {
        var previews = new HashMap<ProductPreview, Product>();
        previews.put(
            new ProductPreview(1, "Ferri 7½", Set.of(new Tag("ferri"), new Tag("materiale"))),
            new Product(
                1,
                "Ferri 7½",
                "Ferri da maglia dimensione 7½ ideale per sciarpe e maglioni di lana",
                Map.ofEntries(Map.entry(new Material(1, "Legno"), 1.0f))
            )
        );
        previews.put(
            new ProductPreview(2, "Uncinetto 5mm", Set.of(new Tag("uncinetti"), new Tag("materiale"))),
            new Product(
                2,
                "Uncinetto 5mm",
                "Uncinetto dimensione 5mm perfetto per centrini",
                Map.ofEntries(Map.entry(new Material(1, "Acciaio"), 1.0f))
            )
        );
        previews.put(
            new ProductPreview(3, "Gomitolo lana merino", Set.of(new Tag("materiale"), new Tag("lana"))),
            new Product(
                3,
                "Gomitolo lana merino",
                "Gomitolo lana merino 100%, senza trattamenti",
                Map.ofEntries(Map.entry(new Material(2, "Lana merino"), 1.0f))
            )
        );
        previews.put(
            new ProductPreview(4, "Gomitolo lana misto acrilico", Set.of(new Tag("materiale"), new Tag("lana"))),
            new Product(
                3,
                "Gomitolo lana misto acrilico",
                "Gomitolo lana morbidissima, misto acrilico",
                Map.ofEntries(
                    Map.entry(new Material(3, "Lana merino"), 0.6f),
                    Map.entry(new Material(4, "Acrilico"), 0.4f)
                )
            )
        );
        this.previews = previews;
    }

    @Override
    public Optional<Product> find(int productCode) {
        return previews
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().code == productCode)
            .findFirst()
            .map(entry -> entry.getValue());
    }

    @Override
    public List<ProductPreview> previews() {
        return this.previews.keySet()
            .stream()
            .sorted((preview1, preview2) -> preview1.code - preview2.code)
            .collect(Collectors.toList());
    }

    @Override
    public boolean loadedPreviews() {
        return true;
    }

    @Override
    public List<ProductPreview> loadPreviews() {
        return this.previews();
    }
}

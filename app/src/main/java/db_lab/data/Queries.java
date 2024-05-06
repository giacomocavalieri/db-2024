package db_lab.data;

public final class Queries {

    public static final String TAGS_FOR_PRODUCT =
        """
        select e.nomeTag
        from   ETICHETTA e
        where  e.codProdotto = ?
        """;

    public static final String LIST_PRODUCTS =
        """
        select p.codProdotto, p.nome
        from   PRODOTTO p
        """;

    public static final String PRODUCT_COMPOSITION =
        """
        select m.codMateriale, m.descrizione, c.percentuale
        from   PRODOTTO p, COMPOSIZIONE c, MATERIALE m
        where  p.codProdotto = c.codProdotto
        and    c.codMateriale = m.codMaterial
        and    p.codProdotto = ?
        """;

    public static final String FIND_PRODUCT =
        """
        select p.*
        from   PRODOTTO p
        where  p.codProdotto = ?
        """;
}

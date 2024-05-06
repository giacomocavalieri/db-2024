package db_lab.data;

public final class Queries {

    public static final String TAGS_FOR_PRODUCT =
        """
        select tag_name
        from   TAGGED
        where  product_code = ?
        """;

    public static final String LIST_PRODUCTS =
        """
        select code, name
        from   PRODUCT
        """;

    public static final String PRODUCT_COMPOSITION =
        """
        select MATERIAL.code, MATERIAL.description, COMPOSITION.percent
        from   PRODUCT, COMPOSITION, MATERIAL
        where  PRODUCT.code = COMPOSITION.product_code
        and    COMPOSITION.material_code = MATERIAL.code
        and    PRODUCT.code = ?
        """;

    public static final String FIND_PRODUCT =
        """
        select code, name, description
        from   PRODUCT
        where  code = ?
        """;
}

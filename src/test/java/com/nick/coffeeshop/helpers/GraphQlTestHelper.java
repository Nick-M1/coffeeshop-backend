package com.nick.coffeeshop.helpers;

import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductSortBy;
import com.nick.coffeeshop.enums.ProductType;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GraphQlTestHelper {
    // language=GraphQL
    private static final String FindOneProductQuery = """
        query FindOneProduct($id: ID!) {
            findOneProduct(id: $id) {
                id
                name
                productSize
                productType
                price
                img
            }
        }
    """;

    // language=GraphQL
    private static final String FindAllProductsQuery = """
        query FindAllProducts($name: String, $productSize: ProductSize, $productType: ProductType, $price: BigDecimal, $img: String, $sortBy: ProductSortBy, $size: Int, $page: Int) {
            findAllProducts(example: { name: $name, productSize: $productSize, productType: $productType, price: $price, img: $img }, sortBy: $sortBy, size: $size, page: $page) {
                content {
                    id
                    name
                    productSize
                    productType
                    price
                    img
                }
                first
                last
                number
                numberOfElements
                totalElements
                totalPages
                pageable {
                    pageNumber
                    pageSize
                }
            }
        }
    """;

    // language=GraphQL
    private static final String CreateProductQuery = """
        mutation CreateProduct($name: String!, $productSize: ProductSize!, $productType: ProductType!, $price: BigDecimal!, $img: String!) {
            createProduct(name: $name, productSize: $productSize, productType: $productType, price: $price, img: $img) {
                id
                name
                productSize
                productType
                price
                img
            }
        }
    """;

    public static String getFindOneProductQuery() {
        return FindOneProductQuery;
    }
    public static String getFindAllProductsQuery() {
        return FindAllProductsQuery;
    }
    public static String getCreateProductsQuery() {
        return CreateProductQuery;
    }

    public static Map<String, ?> getFindOneProductVariables(Long id) {
        return Map.of("id", id);
    }

    public static Map<String, ?> getFindAllProductsVariables(@Nullable String name, @Nullable ProductSize productSize, @Nullable ProductType productType, @Nullable BigDecimal price, @Nullable String img, @Nullable ProductSortBy sortBy, @Nullable Integer size, @Nullable Integer page) {
        HashMap<String, String> map = new HashMap<>();

        if (name != null)
            map.put("name", name);

        if (productSize != null)
            map.put("productSize", productSize.toString());

        if (productType != null)
            map.put("productType", productType.toString());

        if (price != null)
            map.put("price", price.toString());

        if (img != null)
            map.put("img", img);

        if (sortBy != null)
            map.put("sortBy", sortBy.toString());

        if (size != null)
            map.put("size", size.toString());

        if (page != null)
            map.put("page", page.toString());

        return map;
    }

    public static Map<String, ?> getCreateProductsVariables(@NonNull String name, @NonNull ProductSize productSize, @NonNull ProductType productType, @NonNull BigDecimal price, @NonNull String img) {
        return Map.of(
                "name", name,
                "productSize", productSize.toString(),
                "productType", productType.toString(),
                "price", price,
                "img", img
        );
    }
}

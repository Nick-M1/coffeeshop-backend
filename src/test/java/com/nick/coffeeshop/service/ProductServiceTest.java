package com.nick.coffeeshop.service;

import com.nick.coffeeshop.config.properties.PageableConfigProps;
import com.nick.coffeeshop.enums.ProductSize;
import com.nick.coffeeshop.enums.ProductType;
import com.nick.coffeeshop.exception.ResourceNotFoundException;
import com.nick.coffeeshop.model.Product;
import com.nick.coffeeshop.model.filter.ProductFilter;
import com.nick.coffeeshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll();
    @Mock private PageableConfigProps pageableConfigProps = new PageableConfigProps(10, 10);

    @InjectMocks private ProductService underTest;
    private List<Product> products;


    @BeforeEach
    void setUp() {
        products = List.of(
                new Product("product1", ProductSize.SMALL, ProductType.COLD_DRINK, BigDecimal.valueOf(10), "img1"),
                new Product("product2", ProductSize.SMALL, ProductType.HOT_FOOD, BigDecimal.valueOf(3), "img2"),
                new Product("product3", ProductSize.MEDIUM, ProductType.SWEET_TREATS, BigDecimal.valueOf(1), "img3")
        );

        products.get(0).setId(1L);
        products.get(1).setId(2L);
        products.get(2).setId(3L);
    }

    @Test
    void itShouldfindAllByExample() {
        Page<Product> pagedProducts = mock(Page.class);

        when(productRepository.findAll(any(), (PageRequest) any())).thenReturn(pagedProducts);

        var foundProducts = underTest.findAllByExample(Optional.empty(), Optional.empty(), Optional.of(0), Optional.of(10));
        assertThat(foundProducts).isEqualTo(pagedProducts);

        verify(productRepository, times(1)).findAll(any(), (PageRequest) any());
    }

    @Test
    void itShouldFindOne() {
        var actualProduct = products.get(0);
        Long id = actualProduct.getId();

        when(productRepository.findById(id)).thenReturn(Optional.of(actualProduct));

        var foundProduct = underTest.findOne(id);
        assertThat(foundProduct).isEqualTo(actualProduct);

        verify(productRepository, times(1)).findById(any());
    }

    @Test
    void itShouldNotFindOne() {
        Long id = 10L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findOne(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product with id=10 does not exist");

        verify(productRepository, times(1)).findById(any());
    }

    @Test
    void itShouldCreate() {
        var name = "name";
        var productSize = ProductSize.SMALL;
        var productType = ProductType.HOT_FOOD;
        var price = BigDecimal.valueOf(3.5);
        var img = "image";

        var actualProduct = new Product(name, productSize, productType, price, img);

        when(productRepository.save(actualProduct)).thenReturn(actualProduct);

        var createdProduct = underTest.create(name, productSize, productType, price, img);
        assertThat(createdProduct).isEqualTo(actualProduct);

        verify(productRepository, times(1)).save(any());
    }

    @ParameterizedTest(name = "{index} ==> input args: name={0}, size={1}, type={2}, price={3}, img={4}")
    @CsvSource({
            "name, SMALL, HOT_FOOD, 3.5, image",
            "name, null, null, 2, null",
            "null, LARGE, COLD_FOOD, 0, null"
    })
    void itShouldUpdateFields(
            String newName,
            @ConvertWith(ToProductSizeArgumentConverter.class) ProductSize newProductSize,
            @ConvertWith(ToProductTypeArgumentConverter.class) ProductType newProductType,
            BigDecimal newPrice,
            String newImg
    ) {
        var oldProduct = products.get(0);
        var id = oldProduct.getId();

        var newProduct = new Product(
                newName == null ? oldProduct.getName() : newName,
                newProductSize == null ? oldProduct.getProductSize() : newProductSize,
                newProductType == null ? oldProduct.getProductType() : newProductType,
                newPrice == null ? oldProduct.getPrice() : newPrice,
                newImg == null ? oldProduct.getImg() : newImg
        );
        newProduct.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(oldProduct));

        var createdProduct = underTest.update(id, Optional.ofNullable(newName), Optional.ofNullable(newProductSize), Optional.ofNullable(newProductType), Optional.ofNullable(newPrice), Optional.ofNullable(newImg));
        assertThat(createdProduct).isEqualTo(newProduct);

        verify(productRepository, times(1)).findById(id);
    }


    @Test
    void itShouldNotUpdateFields() {
        var id = 10L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.update(id, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product with id=10 does not exist");

        verify(productRepository, times(1)).findById(id);
    }



    @Test
    void itShouldDelete() {
        var actualProduct = products.get(0);
        Long id = actualProduct.getId();

        when(productRepository.findById(id)).thenReturn(Optional.of(actualProduct));

        var deletedProduct = underTest.delete(id);
        assertThat(deletedProduct).isEqualTo(actualProduct);

        verify(productRepository, times(1)).findById(any());
        verify(productRepository, times(1)).delete(any());
    }

    @Test
    void itShouldNotDelete() {
        Long id = 10L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.delete(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product with id=10 does not exist");

        verify(productRepository, times(1)).findById(any());
    }



    static class ToProductSizeArgumentConverter extends SimpleArgumentConverter {
        @Override
        protected ProductSize convert(Object source, Class<?> targetType) {
            return Objects.equals(source.toString(), "null") ? null : ProductSize.valueOf((String) source);
        }
    }
    static class ToProductTypeArgumentConverter extends SimpleArgumentConverter {
        @Override
        protected ProductType convert(Object source, Class<?> targetType) {
            return Objects.equals(source.toString(), "null") ? null : ProductType.valueOf((String) source);
        }
    }
}
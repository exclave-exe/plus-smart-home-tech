package ru.yandex.practicum.shopping.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.store.dto.ProductDto;
import ru.yandex.practicum.interaction.store.enums.ProductCategory;
import ru.yandex.practicum.interaction.store.enums.ProductState;
import ru.yandex.practicum.interaction.store.enums.QuantityState;
import ru.yandex.practicum.shopping.store.exception.ProductNotFoundException;
import ru.yandex.practicum.shopping.store.mapper.ProductMapper;
import ru.yandex.practicum.shopping.store.model.Product;
import ru.yandex.practicum.shopping.store.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String SORT_PROPERTY = "productName";
    private static final String ASC_DIRECTION = "asc";
    private static final String DESC_DIRECTION = "desc";
    private static final String SORT_SEPARATOR = ",";
    private static final int SORT_PARTS_REQUIRED = 2;
    private static final int PROPERTY_INDEX = 0;
    private static final int DIRECTION_INDEX = 1;
    private static final String[] DEFAULT_SORT = {SORT_PROPERTY + SORT_SEPARATOR + ASC_DIRECTION};

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(UUID productId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден: " + productId));
        return mapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByCategory(ProductCategory category, int page, int size, String[] sort) {
        Sort sorting = parseSort(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sorting);
        Page<Product> productPage = repository.
                findByProductCategoryAndProductState(category, ProductState.ACTIVE, pageRequest);
        return productPage.map(mapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = mapper.toEntity(productDto);
        if (productDto.getProductId() == null) product.setProductId(UUID.randomUUID());
        Product saved = repository.save(product);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = mapper.toEntity(productDto);
        if (!repository.existsById(product.getProductId())) {
            throw new ProductNotFoundException("Товар не найден: " + product.getProductId());
        }
        Product saved = repository.save(product);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public Boolean setQuantityState(UUID productId, QuantityState quantityState) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден: " + productId));
        product.setQuantityState(quantityState);
        repository.save(product);
        return true;
    }

    @Override
    @Transactional
    public Boolean removeProductFromStore(UUID productId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден: " + productId));
        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        return true;
    }

    private Sort parseSort(String[] sort) {
        if (sort == null || sort.length == 0) sort = DEFAULT_SORT;
        Sort sorting = Sort.unsorted();

        for (String s : sort) {
            String[] element = s.split(SORT_SEPARATOR);
            String parameter = element[PROPERTY_INDEX].trim();
            Sort.Direction direction;

            if (element.length < SORT_PARTS_REQUIRED) {
                direction = Sort.Direction.ASC;
            } else {
                String dir = element[DIRECTION_INDEX].trim();
                direction = dir.equalsIgnoreCase(DESC_DIRECTION) ? Sort.Direction.DESC : Sort.Direction.ASC;
            }

            sorting = sorting.and(Sort.by(direction, parameter));
        }

        return sorting;
    }

}
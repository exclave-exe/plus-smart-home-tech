package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.warehouse.dto.AddressDto;
import ru.yandex.practicum.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.config.AddressConfig;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.ProductStockMapper;
import ru.yandex.practicum.warehouse.model.ProductStock;
import ru.yandex.practicum.warehouse.repository.ProductStockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductStockRepository repository;
    private final ProductStockMapper mapper;
    private final AddressConfig addressConfig;

    @Override
    @Transactional
    public void addNewProduct(NewProductInWarehouseRequest request) {
        if (repository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с таким ID уже зарегистрирован");
        }
        ProductStock stock = mapper.toEntity(request);
        repository.save(stock);
    }

    @Override
    @Transactional
    public void addQuantity(AddProductToWarehouseRequest request) {
        ProductStock stock = repository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товара с таким ID нет"));

        stock.setQuantity(request.getQuantity());
        repository.save(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkQuantity(CartDto cart) {

        Set<UUID> productIdSet = cart.getProducts().keySet();
        List<ProductStock> stocks = repository.findAllById(productIdSet);

        if (stocks.size() != productIdSet.size()) {

            Set<UUID> foundIdSet = stocks.stream()
                    .map(ProductStock::getProductId)
                    .collect(Collectors.toSet());

            Set<String> notFoundIdSet = productIdSet.stream()
                    .filter(id -> !foundIdSet.contains(id))
                    .map(UUID::toString)
                    .collect(Collectors.toSet());

            throw new ProductInShoppingCartLowQuantityInWarehouse("Товаров нет на складе: " + notFoundIdSet);
        }

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;
        List<String> errors = new ArrayList<>();

        for (ProductStock stock : stocks) {
            UUID productId = stock.getProductId();
            long requested = cart.getProducts().get(productId);
            if (stock.getQuantity() < requested) {
                errors.add(String.format("Товар %s: доступно %d, запрошено %d",
                        productId, stock.getQuantity(), requested));
                continue;
            }

            totalWeight += stock.getWeight();
            totalVolume += stock.getWidth() * stock.getHeight() * stock.getDepth();
            if (stock.isFragile()) hasFragile = true;
        }

        if (!errors.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(
                    "Недостаточно товаров: " + String.join("; ", errors));
        }

        return new BookedProductsDto(totalWeight, totalVolume, hasFragile);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return addressConfig.getWarehouseAddress();
    }

}
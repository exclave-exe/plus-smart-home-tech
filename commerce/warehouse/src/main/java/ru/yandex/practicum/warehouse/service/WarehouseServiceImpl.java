package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.ProductStockMapper;
import ru.yandex.practicum.warehouse.model.ProductStock;
import ru.yandex.practicum.warehouse.repository.ProductStockRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final ProductStockRepository repository;
    private final ProductStockMapper mapper;

    public void addNewProduct(NewProductInWarehouseRequest request) {
        if (repository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с таким ID уже зарегистрирован");
        }
        ProductStock stock = mapper.toEntity(request);
        repository.save(stock);
    }

    public void addQuantity(AddProductToWarehouseRequest request) {
        ProductStock stock = repository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товара с таким ID нет"));

        stock.setQuantity(request.getQuantity());
        repository.save(stock);
    }

    public BookedProductsDto checkAvailability(ShoppingCartDto cart) {
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (Map.Entry<String, Long> entry : cart.getProducts().entrySet()) {
            String productId = entry.getKey();
            long requested = entry.getValue();
            ProductStock stock = products.get(productId);
            if (stock == null || stock.getQuantity() < requested) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        "Not enough quantity for product " + productId);
            }
            totalWeight += stock.getWeight() * requested;
            double volume = stock.getDimension().getWidth() *
                    stock.getDimension().getHeight() *
                    stock.getDimension().getDepth();
            totalVolume += volume * requested;
            if (stock.isFragile()) {
                hasFragile = true;
            }
        }
        return new BookedProductsDto(totalWeight, totalVolume, hasFragile);
    }
}
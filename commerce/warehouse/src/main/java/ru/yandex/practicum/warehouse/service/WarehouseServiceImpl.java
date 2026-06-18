package ru.yandex.practicum.warehouse.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.cart.dto.CartDto;
import ru.yandex.practicum.interaction.delivery.dto.AddressDto;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction.order.OrderClient;
import ru.yandex.practicum.interaction.warehouse.dto.*;
import ru.yandex.practicum.warehouse.config.AddressConfig;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.ProductStockMapper;
import ru.yandex.practicum.warehouse.model.Booking;
import ru.yandex.practicum.warehouse.model.ProductStock;
import ru.yandex.practicum.warehouse.repository.BookingRepository;
import ru.yandex.practicum.warehouse.repository.ProductStockRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static final double INITIAL_TOTAL_WEIGHT = 0.0;
    private static final double INITIAL_TOTAL_VOLUME = 0.0;
    private static final boolean NO_FRAGILE_ITEMS = false;

    private final ProductStockRepository repository;
    private final BookingRepository bookingRepository;
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

        double totalWeight = INITIAL_TOTAL_WEIGHT;
        double totalVolume = INITIAL_TOTAL_VOLUME;
        boolean hasFragile = NO_FRAGILE_ITEMS;
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

    @Override
    @Transactional
    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request) {

        UUID orderId = request.getOrderId();
        Map<UUID, Long> requestedProducts = request.getProducts();

        List<ProductStock> stocks = repository.findAllById(requestedProducts.keySet());
        if (stocks.size() != requestedProducts.size()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Некоторые товары не найдены на складе");
        }

        Map<UUID, ProductStock> stockMap = stocks.stream().collect(Collectors.toMap(ProductStock::getProductId, Function.identity()));

        for (Map.Entry<UUID, Long> entry : requestedProducts.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQty = entry.getValue();
            ProductStock stock = stockMap.get(productId);
            if (stock.getQuantity() < requestedQty) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        String.format("Недостаточно товара %s: запрошено %d, доступно %d",
                                productId, requestedQty, stock.getQuantity()));
            }
        }

        double totalWeight = INITIAL_TOTAL_WEIGHT;
        double totalVolume = INITIAL_TOTAL_VOLUME;
        boolean hasFragile = NO_FRAGILE_ITEMS;

        for (Map.Entry<UUID, Long> entry : requestedProducts.entrySet()) {
            UUID productId = entry.getKey();
            long qty = entry.getValue();
            ProductStock stock = stockMap.get(productId);

            totalWeight += stock.getWeight() * qty;
            totalVolume += stock.getWidth() * stock.getHeight() * stock.getDepth() * qty;
            if (stock.isFragile()) {
                hasFragile = true;
            }
        }

        for (Map.Entry<UUID, Long> entry : requestedProducts.entrySet()) {
            ProductStock stock = stockMap.get(entry.getKey());
            stock.setQuantity(stock.getQuantity() - entry.getValue());
        }
        repository.saveAll(stockMap.values());

        Booking booking = new Booking();
        booking.setOrderId(orderId);
        booking.setProducts(new HashMap<>(requestedProducts));
        booking.setDeliveryWeight(totalWeight);
        booking.setDeliveryVolume(totalVolume);
        booking.setFragile(hasFragile);
        booking.setDeliveryId(null);
        bookingRepository.save(booking);

        return new BookedProductsDto(totalWeight, totalVolume, hasFragile);
    }

    @Override
    public void shippedProducts(ShippedToDeliveryRequest request) {
        Booking booking = bookingRepository.findByOrderId(request.getOrderId());
        booking.setDeliveryId(request.getDeliveryId());
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void returnProducts(Map<UUID, Long> products) {
        List<UUID> productIds = new ArrayList<>(products.keySet());
        List<ProductStock> stocks = repository.findAllById(productIds);

        Map<UUID, ProductStock> stockMap = stocks.stream()
                .collect(Collectors.toMap(ProductStock::getProductId, Function.identity()));

        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Long quantityToAdd = entry.getValue();

            if (quantityToAdd == null || quantityToAdd <= 0) {
                continue;
            }

            ProductStock stock = stockMap.get(productId);
            if (stock == null) {
                throw new NoSpecifiedProductInWarehouseException("Товар с id " + productId + " не найден на складе");
            }

            stock.setQuantity(stock.getQuantity() + quantityToAdd);
        }
    }
}
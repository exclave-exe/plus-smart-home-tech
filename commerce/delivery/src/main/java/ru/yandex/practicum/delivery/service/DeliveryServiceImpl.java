package ru.yandex.practicum.delivery.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.delivery.enums.DeliveryState;
import ru.yandex.practicum.interaction.order.OrderClient;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.warehouse.WarehouseClient;
import ru.yandex.practicum.interaction.warehouse.dto.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private static final BigDecimal BASE_COST = new BigDecimal("5.0");

    private static final BigDecimal ADDRESS_1_MULTIPLIER = new BigDecimal("1.0");
    private static final BigDecimal ADDRESS_2_MULTIPLIER = new BigDecimal("2.0");
    private static final BigDecimal DEFAULT_MULTIPLIER = new BigDecimal("1.0");

    private static final BigDecimal FRAGILITY_RATE = new BigDecimal("0.2");
    private static final BigDecimal WEIGHT_RATE = new BigDecimal("0.3");
    private static final BigDecimal VOLUME_RATE = new BigDecimal("0.2");
    private static final BigDecimal ADDRESS_MISMATCH_RATE = new BigDecimal("0.2");

    private final DeliveryMapper mapper;
    private final DeliveryRepository repository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto delivery) {
        Delivery newDelivery = mapper.toEntity(delivery);
        Delivery saved = repository.save(newDelivery);
        return mapper.toDto(saved);
    }

    @Override
    public DeliveryDto successfulDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        Delivery saved = repository.save(delivery);
        orderClient.deliveryOrder(saved.getOrderId());
        return mapper.toDto(saved);
    }

    @Override
    public DeliveryDto failedDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        Delivery saved = repository.save(delivery);
        orderClient.deliveryFailedOrder(saved.getOrderId());
        return mapper.toDto(saved);
    }

    @Override
    public BigDecimal costDelivery(OrderDto order) {
        Delivery delivery = getDeliveryById(order.getDeliveryId());
        Address warehouseAddress = delivery.getFromAddress();
        Address clientAddress = delivery.getToAddress();
        return calculateCost(order, warehouseAddress, clientAddress);
    }

    @Override
    public DeliveryDto pickedDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        Delivery saved = repository.save(delivery);
        warehouseClient.shippedProducts(new ShippedToDeliveryRequest(saved.getOrderId(), deliveryId));
        return mapper.toDto(saved);
    }

    private Delivery getDeliveryById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Доставка " + id + " не найдена"));
    }

    private BigDecimal calculateCost(OrderDto order, Address warehouse, Address destination) {
        BigDecimal total = BASE_COST;

        BigDecimal warehouseMultiplier = getWarehouseMultiplier(warehouse.getStreet());
        BigDecimal warehouseAddition = BASE_COST.multiply(warehouseMultiplier);
        total = total.add(warehouseAddition);

        if (Boolean.TRUE.equals(order.getFragile())) {
            BigDecimal fragilitySurcharge = total.multiply(FRAGILITY_RATE);
            total = total.add(fragilitySurcharge);
        }

        if (order.getDeliveryWeight() != null) {
            BigDecimal weightCost = BigDecimal.valueOf(order.getDeliveryWeight()).multiply(WEIGHT_RATE);
            total = total.add(weightCost);
        }

        if (order.getDeliveryVolume() != null) {
            BigDecimal volumeCost = BigDecimal.valueOf(order.getDeliveryVolume()).multiply(VOLUME_RATE);
            total = total.add(volumeCost);
        }

        if (!isSameStreet(warehouse, destination)) {
            BigDecimal mismatchSurcharge = total.multiply(ADDRESS_MISMATCH_RATE);
            total = total.add(mismatchSurcharge);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getWarehouseMultiplier(String street) {
        if (street == null) return DEFAULT_MULTIPLIER;
        if (street.contains("ADDRESS_1")) return ADDRESS_1_MULTIPLIER;
        if (street.contains("ADDRESS_2")) return ADDRESS_2_MULTIPLIER;
        return DEFAULT_MULTIPLIER;
    }

    private boolean isSameStreet(Address wh, Address cl) {
        if (wh == null || cl == null || wh.getStreet() == null || cl.getStreet() == null) {
            return false;
        }
        return wh.getStreet().trim().equalsIgnoreCase(cl.getStreet().trim());
    }
}

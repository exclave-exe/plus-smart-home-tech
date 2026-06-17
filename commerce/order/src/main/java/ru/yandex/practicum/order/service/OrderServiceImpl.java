package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.delivery.DeliveryClient;
import ru.yandex.practicum.interaction.delivery.dto.AddressDto;
import ru.yandex.practicum.interaction.delivery.dto.DeliveryDto;
import ru.yandex.practicum.interaction.delivery.enums.DeliveryState;
import ru.yandex.practicum.interaction.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.order.dto.ProductReturnRequest;
import ru.yandex.practicum.interaction.order.enums.OrderState;
import ru.yandex.practicum.interaction.payment.PaymentClient;
import ru.yandex.practicum.interaction.warehouse.WarehouseClient;
import ru.yandex.practicum.interaction.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.order.exception.OrderNotFoundException;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    public List<OrderDto> getOrders(String username) {
        List<Order> orders = repository.findByUsername(username);
        return orders.stream().map(mapper::toDto).toList();
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest createOrderRequest) {
        BookedProductsDto bookedProductsDto = warehouseClient.checkQuantity(createOrderRequest.getShoppingCart());
        Order order = mapper.toOrderFromAssembly(createOrderRequest, bookedProductsDto);

        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress();
        DeliveryDto newDelivery = new DeliveryDto(
                null,
                warehouseAddress,
                createOrderRequest.getDeliveryAddress(),
                order.getOrderId(),
                DeliveryState.CREATED
        );
        newDelivery = deliveryClient.createDelivery(newDelivery);
        order.setDeliveryId(newDelivery.getDeliveryId());

        order = repository.save(order);
        return mapper.toDto(order);
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest returnOrderRequest) {
        Order order = getOrderById(returnOrderRequest.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        Order saved = repository.save(order);
        warehouseClient.returnProducts(returnOrderRequest.getProducts());
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto paymentOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.PAID);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto paymentFailedOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.DELIVERED);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto deliveryFailedOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.COMPLETED);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto calculatedTotalOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        BigDecimal totalPrice = paymentClient.totalCostPayment(mapper.toDto(order));
        order.setTotalPrice(totalPrice);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto calculatedDeliveryOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        BigDecimal deliveryPrice = deliveryClient.costDelivery(mapper.toDto(order));
        order.setTotalPrice(deliveryPrice);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.ASSEMBLED);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto assemblyFailedOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }

    private Order getOrderById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new OrderNotFoundException("Заказ " + id + " не найдена"));
    }

}

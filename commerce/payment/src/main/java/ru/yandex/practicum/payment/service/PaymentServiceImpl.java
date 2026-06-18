package ru.yandex.practicum.payment.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.interaction.order.OrderClient;
import ru.yandex.practicum.interaction.order.dto.OrderDto;
import ru.yandex.practicum.interaction.payment.dto.PaymentDto;
import ru.yandex.practicum.interaction.payment.enums.PaymentState;
import ru.yandex.practicum.interaction.store.ShoppingStoreClient;
import ru.yandex.practicum.interaction.store.dto.ProductDto;
import ru.yandex.practicum.payment.exception.InsufficientInformationException;
import ru.yandex.practicum.payment.mapper.PaymentMapper;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.1");

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        validatePrice(
                order.getProductPrice(),
                order.getDeliveryPrice(),
                order.getTotalPrice()
        );
        Payment payment = mapper.toPayment(order);
        Payment saved = repository.save(payment);
        return mapper.toPaymentDto(saved);
    }

    @Override
    public BigDecimal totalCostPayment(OrderDto order) {
        validatePrice(
                order.getProductPrice(),
                order.getTotalPrice()
        );
        BigDecimal productsPrice = order.getProductPrice();
        BigDecimal deliveryPrice = order.getDeliveryPrice();
        return deliveryPrice.add(productsPrice).add(productsPrice.multiply(TAX_RATE));
    }

    @Override
    public BigDecimal productCostPayment(OrderDto order) {
        List<UUID> productIds = new ArrayList<>(order.getProducts().keySet());
        List<ProductDto> products = shoppingStoreClient.getProductsByIds(productIds);

        BigDecimal total = BigDecimal.ZERO;

        for (ProductDto product : products) {
            UUID productId = product.getProductId();
            BigDecimal price = product.getPrice();
            Long quantity = order.getProducts().get(productId);
            total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        return total;

    }

    @Override
    public void refundPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setPaymentState(PaymentState.SUCCESS);
        orderClient.paymentOrder(payment.getOrderId());
        repository.save(payment);
    }

    @Override
    public void failedPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setPaymentState(PaymentState.FAILED);
        orderClient.paymentFailedOrder(payment.getOrderId());
        repository.save(payment);
    }

    private Payment getPaymentById(UUID paymentId) {
        return repository.findById(paymentId).orElseThrow(() -> new NotFoundException("Платеж не найдет"));
    }

    private void validatePrice(BigDecimal... prices) {
        if (Arrays.stream(prices).anyMatch(Objects::isNull)) {
            throw new InsufficientInformationException("Недостаточно информации для расчёта");
        }
    }

}

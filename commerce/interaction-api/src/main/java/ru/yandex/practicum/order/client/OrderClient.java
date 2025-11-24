package ru.yandex.practicum.order.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.order.dto.OrderCreateRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;
import ru.yandex.practicum.utils.ValidationUtil;

import java.util.List;
import java.util.UUID;

@Validated
@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {
    @GetMapping
    List<OrderDto> getClientOrders(
            @RequestParam(name = "username")
            @NotBlank(message = ValidationUtil.VALIDATION_USERNAME_MESSAGE)
            String userName);

    @PutMapping
    OrderDto createNewOrder(@RequestBody @Valid OrderCreateRequest request);

    @PostMapping("/return")
    OrderDto returnProducts(@RequestBody @Valid ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto payOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto failPayOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/success")
    OrderDto successPayOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliverOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto failDeliverOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotalPrice(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryPrice(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly")
    OrderDto assemblyOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto failAssemblyOrder(@RequestBody @NotNull UUID orderId);

    @GetMapping("/only")
    OrderDto getOrder(@RequestBody @NotNull UUID orderId);
}

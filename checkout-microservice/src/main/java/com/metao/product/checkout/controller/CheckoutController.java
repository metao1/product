package com.metao.product.checkout.controller;

import com.metao.product.checkout.domain.OrderEntity;
import com.metao.product.checkout.exception.NotEnoughProductsInStockException;
import com.metao.product.checkout.model.CheckoutStatus;
import com.metao.product.checkout.service.impl.CheckoutServiceImplementation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/checkout")
@RestController
public class CheckoutController {

    CheckoutServiceImplementation checkoutService;

    public CheckoutController(CheckoutServiceImplementation checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping(produces = "application/json")
    public CheckoutStatus checkout() {
        String userId = "u1001";
        CheckoutStatus checkoutStatus = new CheckoutStatus();
        try {
            OrderEntity currentOrder = checkoutService.checkout(userId);
            if (currentOrder != null) {
                checkoutStatus.setOrderNumber(currentOrder.getId().toString());
                checkoutStatus.setStatus(CheckoutStatus.SUCCESS);
                checkoutStatus.setOrderDetails(currentOrder.getOrderDetails());
                log.debug("Order is : " + currentOrder.getId() + " Details: " + currentOrder.getOrderDetails());
            } else {
                checkoutStatus.setOrderNumber("");
                checkoutStatus.setStatus(CheckoutStatus.FAILURE);
                checkoutStatus.setOrderDetails("Product is Out of Stock!");
            }
        } catch (NotEnoughProductsInStockException e) {
            checkoutStatus.setOrderNumber("");
            checkoutStatus.setStatus(CheckoutStatus.FAILURE);
            return checkoutStatus;
        }
        return checkoutStatus;
    }
}

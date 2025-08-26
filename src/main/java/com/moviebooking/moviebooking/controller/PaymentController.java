package com.moviebooking.moviebooking.controller;

import static com.moviebooking.moviebooking.urlpaths.PaymentPaths.*;
import static com.moviebooking.moviebooking.urlpaths.ShowsPaths.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.moviebooking.moviebooking.model.PaymentResult;
import com.moviebooking.moviebooking.model.dto.paymentdto.PaymentRequestDto;
import com.moviebooking.moviebooking.services.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(PAYMENT_BASE_PATH)
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // This is a post mapping that will be called when the user clicks on the pay
    // button
    @PostMapping(MAKE_PAYMENT_PATH_URL)
    public String makePayment(@ModelAttribute PaymentRequestDto paymentRequest, Model model) {
        log.info("Payment request for booking ID: {}", paymentRequest.getBookingId());
        log.info("Payment Mode: {}", paymentRequest.getPaymentMode());
        log.info("User Phone Number: {}", paymentRequest.getUserPhoneNumber());

        // Process the payment
        PaymentResult paymentResult = paymentService.processPayment(paymentRequest);
        // if the payment response is null then it means the booking time expired
        if (paymentResult.getPaymentResponse() == null) {
            log.warn("Payment processing failed - booking time expired for booking ID: {}",
                    paymentRequest.getBookingId());

            return "redirect:" + SHOW_BASE_PATH + "/showseatselectionpage/" + paymentResult.getShowId()
                    + "?error=Your booking time expired";
        }

        // Here we are adding the payment response to the model so on payment success
        // we can show the payment details on the booking confirmation page
        model.addAttribute("booking", paymentResult.getPaymentResponse());
        log.info("Payment processed successfully: {}", paymentResult.getPaymentResponse());

        // Return the final booking confirmation page
        return BOOKING_CONFIRMATION_PAGE;
    }
}

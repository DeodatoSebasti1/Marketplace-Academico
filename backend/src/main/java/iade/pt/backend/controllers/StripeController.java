package iade.pt.backend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamento/stripe")
public class StripeController {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostMapping("/create")
    public Map<String, Object> createPaymentIntent(@RequestBody Map<String, Object> data) throws Exception {

        Stripe.apiKey = secretKey;

        Long amount = Long.valueOf(data.get("amount").toString());

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .build();

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        return response;
    }
}


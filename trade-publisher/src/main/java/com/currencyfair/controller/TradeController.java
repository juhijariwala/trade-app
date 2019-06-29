package com.currencyfair.controller;

import com.currencyfair.dto.TradeMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("trade-publisher/trade")
public class TradeController {
    @PostMapping()
    ResponseEntity publishTrade(@RequestBody TradeMessage tradeMessage) {
        return ResponseEntity.ok().build();
    }
}

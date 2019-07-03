package com.currencyfair.tradepublisher.controller;

import com.currencyfair.tradepublisher.dto.TradeMessage;
import com.currencyfair.tradepublisher.service.TradePublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("trade-publisher/trade")
public class TradeController {
    @Autowired
    private TradePublisherService service;

    @PostMapping()
    ResponseEntity publishTrade(@RequestBody TradeMessage tradeMessage) {
        service.publishMessage(tradeMessage);
        return ResponseEntity.ok().build();
    }
}

package ru.gpbtech.wallet.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gpbtech.wallet.model.GetWalletBalanceRequest;
import ru.gpbtech.wallet.model.GetWalletBalanceResponse;

@Log4j2
@RestController
@RequestMapping("/wallet")
public class WalletBalanceController {


    @PostMapping("/balance")
    public ResponseEntity<GetWalletBalanceResponse> getWalletBalance(@RequestBody GetWalletBalanceRequest request) {
        log.info("Запрос на получение информации по балансу для клиента {}", request.getClientId());
        return null;
    }
}
package ru.gpbtech.wallet.service;

import org.springframework.http.ResponseEntity;
import ru.gpbtech.wallet.model.GetWalletBalanceRequest;
import ru.gpbtech.wallet.model.GetWalletBalanceResponse;

public interface WalletBalanceService {
    ResponseEntity<GetWalletBalanceResponse> getWalletBalance(GetWalletBalanceRequest request);
}

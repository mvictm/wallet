package ru.gpbtech.wallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gpbtech.wallet.model.GetWalletBalanceRequest;
import ru.gpbtech.wallet.model.GetWalletBalanceResponse;
import ru.gpbtech.wallet.model.VerifiedWalletBalance;
import ru.gpbtech.wallet.persistence.entity.WalletBalance;
import ru.gpbtech.wallet.persistence.repository.WalletBalanceRepository;
import ru.gpbtech.wallet.service.WalletBalanceService;

import java.time.ZoneOffset;
import java.util.Optional;

/**
 * Сервис для управления операциями с балансом кошелька.
 */
@Service
@RequiredArgsConstructor
public class WalletBalanceServiceImpl implements WalletBalanceService {
    private final WalletBalanceRepository walletBalanceRepository;
    
    /**
     * Получает баланс кошелька на основе предоставленных параметров.
     *
     * @param request Запрос на получение баланса кошелька.
     *
     * @return Ответ с балансом кошелька и дополнительной информацией.
     */
    @Override
    public ResponseEntity<GetWalletBalanceResponse> getWalletBalance(GetWalletBalanceRequest request) {
        return Optional.ofNullable(request)
                .flatMap(VerifiedWalletBalance::create)
                .flatMap(verify -> walletBalanceRepository.findBalance(verify.getClientId(), verify.getDateFrom(), verify.getDateTo()))
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
    
    /**
     * Преобразует сущность баланса кошелька в ответ.
     *
     * @param walletBalance Сущность баланса кошелька.
     *
     * @return Ответ с балансом кошелька и дополнительной информацией.
     */
    private GetWalletBalanceResponse mapToResponse(WalletBalance walletBalance) {
        GetWalletBalanceResponse response = new GetWalletBalanceResponse();
        response.setBalance(walletBalance.getBalance());
        response.setCurrency(walletBalance.getCurrency());
        response.setLastUpdated(walletBalance.getLastUpdated().atOffset(ZoneOffset.ofHours(3)));
        return response;
    }
}

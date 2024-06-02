package ru.gpbtech.wallet.model;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

/**
 * Верифицированный объект для запроса данных баланса пользователя
 */
@Log4j2
@Getter
public class VerifiedWalletBalance {
    
    private static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");
    
    private final String clientId;
    private final LocalDateTime dateFrom;
    private final LocalDateTime dateTo;
    
    private VerifiedWalletBalance(String clientId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.clientId = clientId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
    
    /**
     * Создает экземпляр {@link VerifiedWalletBalance} на основе запроса {@link GetWalletBalanceRequest}.
     *
     * @param request объект {@link GetWalletBalanceRequest}, содержащий параметры запроса.
     *
     * @return экземпляр {@link VerifiedWalletBalance}, содержащий верифицированные данные.
     *
     * @throws IllegalArgumentException если в запросе не передан корректный clientId.
     */
    public static Optional<VerifiedWalletBalance> create(GetWalletBalanceRequest request) {
        Optional<GetWalletBalanceRequest> balanceRequest = Optional.ofNullable(request);
        
        LocalDateTime dateFrom = balanceRequest
                .map(GetWalletBalanceRequest::getDateFrom)
                .map(OffsetDateTime::toLocalDateTime)
                .orElseGet(() -> LocalDateTime.now(MOSCOW_ZONE));
        
        LocalDateTime dateTo = balanceRequest
                .map(GetWalletBalanceRequest::getDateTo)
                .map(OffsetDateTime::toLocalDateTime)
                .orElseGet(() -> LocalDateTime.now(MOSCOW_ZONE));
        
        return balanceRequest
                .map(GetWalletBalanceRequest::getClientId)
                .map(UUID::toString)
                .map(clientId -> new VerifiedWalletBalance(clientId, dateFrom, dateTo))
                .or(() -> {
                    log.error("Запрос не содержит корректного clientId");
                    return Optional.empty();
                });
    }
}

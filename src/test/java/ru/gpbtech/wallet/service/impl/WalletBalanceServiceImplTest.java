package ru.gpbtech.wallet.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.gpbtech.wallet.model.GetWalletBalanceRequest;
import ru.gpbtech.wallet.model.GetWalletBalanceResponse;
import ru.gpbtech.wallet.persistence.entity.WalletBalance;
import ru.gpbtech.wallet.persistence.repository.WalletBalanceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Пример unit-теста
 */
@ExtendWith(MockitoExtension.class)
class WalletBalanceServiceImplTest {
    
    @Mock
    private WalletBalanceRepository walletBalanceRepository;
    
    @InjectMocks
    private WalletBalanceServiceImpl walletBalanceService;
    
    @Test
    void testGetWalletBalanceSuccess() {
        GetWalletBalanceRequest request = new GetWalletBalanceRequest();
        request.setClientId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        request.setDateFrom(OffsetDateTime.parse("2024-06-01T12:00:00Z"));
        request.setDateTo(OffsetDateTime.parse("2024-06-01T13:00:00Z"));
        
        WalletBalance walletBalance = new WalletBalance();
        walletBalance.setBalance(BigDecimal.valueOf(100.00));
        walletBalance.setCurrency("RUB");
        walletBalance.setLastUpdated(LocalDateTime.now());
        
        when(walletBalanceRepository.findBalance(any(), any(), any()))
                .thenReturn(Optional.of(walletBalance));
        
        ResponseEntity<GetWalletBalanceResponse> balance = walletBalanceService.getWalletBalance(request);
        
        assertThat(balance.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(balance.getBody()).isNotNull();
    }
    
    @Test
    void testGetWalletBalanceErrorByEmptyDataBase() {
        GetWalletBalanceRequest request = new GetWalletBalanceRequest();
        request.setClientId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        request.setDateFrom(OffsetDateTime.parse("2024-06-01T12:00:00Z"));
        request.setDateTo(OffsetDateTime.parse("2024-06-01T13:00:00Z"));
        
        when(walletBalanceRepository.findBalance(any(), any(), any()))
                .thenReturn(Optional.empty());
        
        ResponseEntity<GetWalletBalanceResponse> balance = walletBalanceService.getWalletBalance(request);
        
        assertThat(balance.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(balance.getBody()).isNull();
    }
    
    @Test
    void testGetWalletBalanceErrorByCreation() {
        GetWalletBalanceRequest request = new GetWalletBalanceRequest();
        
        ResponseEntity<GetWalletBalanceResponse> balance = walletBalanceService.getWalletBalance(request);
        
        assertThat(balance.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(balance.getBody()).isNull();
        
        verify(walletBalanceRepository, never()).findBalance(any(), any(), any());
    }
}
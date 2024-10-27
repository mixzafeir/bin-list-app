package com.interview.etravli.service;

import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.dto.etraveli.ClearingCostResponseDTO;
import com.interview.etravli.dto.etraveli.UserPrincipal;
import com.interview.etravli.dto.feign.BinListFeignDTO;
import com.interview.etravli.dto.feign.CountryFeignDTO;
import com.interview.etravli.enums.Roles;
import com.interview.etravli.exceptions.EntityNotFoundException;
import com.interview.etravli.models.ClearingCost;
import com.interview.etravli.repository.ClearingCostRepository;
import com.interview.etravli.service.impl.ClearingCostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClearingCostServiceTest {

    @Mock
    private ClearingCostRepository clearingCostRepo;

    @Mock
    private BinListFeignService binListFeignService;

    @InjectMocks
    private ClearingCostServiceImpl clearingCostService;

    private ClearingCost clearingCost;
    private ClearingCostDTO clearingCostDTO;
    private UserPrincipal principal;

    @BeforeEach
    void setUp() {
        clearingCost = new ClearingCost();
        clearingCost.setId(UUID.randomUUID());
        clearingCost.setCardIssuingCountry("US");
        clearingCost.setClearingCost(new BigDecimal("20.000"));

        clearingCostDTO = new ClearingCostDTO();
        clearingCostDTO.setCardIssuingCountry("US");
        clearingCostDTO.setClearingCost(new BigDecimal("20.000"));

        principal = UserPrincipal.builder()
                .username("mock")
                .password("mock")
                .role(Roles.ROLE_ADMIN)
                .authority(new SimpleGrantedAuthority("mock"))
                .build();;
    }

    @Test
    void testSaveClearingCost() {
        when(clearingCostRepo.save(any(ClearingCost.class))).thenReturn(clearingCost);
        ClearingCostDTO savedClearingCost = clearingCostService.save(principal, clearingCostDTO);
        assertNotNull(savedClearingCost);
        assertEquals(clearingCost.getClearingCost(), savedClearingCost.getClearingCost());
    }

    @Test
    void testUpdateClearingCost() {
        UUID testUUID = UUID.randomUUID();
        clearingCost.setClearingCost(new BigDecimal("5.00"));
        when(clearingCostRepo.save(any(ClearingCost.class))).thenReturn(clearingCost);
        when(clearingCostRepo.findById(testUUID)).thenReturn(Optional.of(clearingCost));
        ClearingCostDTO savedClearingCost = clearingCostService.update(principal, testUUID, clearingCostDTO);
        assertNotNull(savedClearingCost);
        assertEquals(clearingCost.getClearingCost(), savedClearingCost.getClearingCost());
    }

    @Test
    void testGetAllClearingCosts() {
        when(clearingCostRepo.findAll()).thenReturn(List.of(clearingCost));
        List<ClearingCostDTO> result = clearingCostService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clearingCostDTO.getClearingCost(), new BigDecimal("20.000"));
    }

    @Test
    void testGetClearingCostById_Found() {
        UUID id = clearingCost.getId();
        when(clearingCostRepo.findById(id)).thenReturn(Optional.of(clearingCost));
        ClearingCostResponseDTO result = clearingCostService.getById(id);
        assertNotNull(result);
        assertEquals(clearingCostDTO.getClearingCost(), new BigDecimal("20.000"));
    }

    @Test
    void testGetClearingCostById_NotFound() {
        UUID id = UUID.randomUUID();
        when(clearingCostRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clearingCostService.getById(id));
    }

    @Test
    void testDeleteClearingCostById_Found() {
        UUID id = clearingCost.getId();
        when(clearingCostRepo.findById(id)).thenReturn(Optional.of(clearingCost));
        clearingCostService.deleteById(id);
    }

    @Test
    void testDeleteClearingCostById_NotFound() {
        UUID id = UUID.randomUUID();
        when(clearingCostRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clearingCostService.deleteById(id));
        verify(clearingCostRepo, times(0)).deleteById(id);
    }

    @Test
    void testGetClearingCostByCardNumber_Found() {
        String cardNumber = "123456789";
        BinListFeignDTO binListFeignDto = new BinListFeignDTO();
        CountryFeignDTO countryDto = new CountryFeignDTO();
        countryDto.setAlpha2("US");
        binListFeignDto.setCountry(countryDto);

        ClearingCost clearingCost = new ClearingCost();
        clearingCost.setClearingCost(new BigDecimal("20.000"));
        clearingCost.setCardIssuingCountry("US");
        when(binListFeignService.getCardInfoFromFeign(cardNumber.substring(0, 6)))
                .thenReturn(CompletableFuture.completedFuture(binListFeignDto));
        when(clearingCostRepo.findByCardIssuingCountry("US")).thenReturn(Optional.of(clearingCost));
        CompletableFuture<ClearingCostResponseDTO> resultFuture = clearingCostService.getByCardNumber(cardNumber);
        ClearingCostResponseDTO result = resultFuture.join();
        assertNotNull(result);
        assertEquals(new BigDecimal("20.000"), result.getCost());
        assertEquals("US", result.getCountry());
    }

    @Test
    void testGetClearingCostByCardNumber_DefaultCountryFound() {
        String cardNumber = "123456789";
        BinListFeignDTO binListFeignDto = new BinListFeignDTO();
        CountryFeignDTO countryDto = new CountryFeignDTO();
        countryDto.setAlpha2("SM");
        binListFeignDto.setCountry(countryDto);
        clearingCost.setClearingCost(new BigDecimal("10.000"));
        clearingCost.setCardIssuingCountry("OT");

        when(binListFeignService.getCardInfoFromFeign(cardNumber.substring(0, 6)))
                .thenReturn(CompletableFuture.completedFuture(binListFeignDto));
        when(clearingCostRepo.findByCardIssuingCountry("SM")).thenReturn(Optional.empty());
        when(clearingCostRepo.findByCardIssuingCountry("OT")).thenReturn(Optional.of(clearingCost));
        CompletableFuture<ClearingCostResponseDTO> resultFuture = clearingCostService.getByCardNumber(cardNumber);
        ClearingCostResponseDTO result = resultFuture.join();
        assertNotNull(result);
        assertEquals(new BigDecimal("10.000"), result.getCost());
    }

    @Test
    void testGetClearingCostByCardNumber_DefaultCountryNotFound() {
        String cardNumber = "123456789";
        BinListFeignDTO binListFeignDto = new BinListFeignDTO();
        CountryFeignDTO countryDto = new CountryFeignDTO();
        countryDto.setAlpha2("SM");
        binListFeignDto.setCountry(countryDto);
        clearingCost.setClearingCost(new BigDecimal("10.000"));
        clearingCost.setCardIssuingCountry("OT");

        when(binListFeignService.getCardInfoFromFeign(cardNumber.substring(0, 6)))
                .thenReturn(CompletableFuture.completedFuture(binListFeignDto));
        when(clearingCostRepo.findByCardIssuingCountry("SM")).thenReturn(Optional.empty());
        when(clearingCostRepo.findByCardIssuingCountry("OT")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> clearingCostService.getByCardNumber(cardNumber).join());
    }

}

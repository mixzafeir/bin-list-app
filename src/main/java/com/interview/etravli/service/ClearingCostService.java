package com.interview.etravli.service;

import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.dto.etraveli.ClearingCostResponseDTO;
import com.interview.etravli.dto.etraveli.UserPrincipal;
import com.interview.etravli.models.ClearingCost;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ClearingCostService {

    ClearingCostDTO save(UserPrincipal user, ClearingCostDTO clearingCostDto);

    ClearingCostDTO update(UserPrincipal user, UUID id, ClearingCostDTO clearingCostDto);

    List<ClearingCostDTO> getAll();

    ClearingCostResponseDTO getById(UUID id);

    void deleteById(UUID id);

    CompletableFuture<ClearingCostResponseDTO> getByCardNumber(String cardNumber);

}

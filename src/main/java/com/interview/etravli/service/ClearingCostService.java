package com.interview.etravli.service;

import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.dto.etraveli.ClearingCostResponseDTO;
import com.interview.etravli.dto.etraveli.UserPrincipal;
import com.interview.etravli.models.ClearingCost;

import java.util.List;
import java.util.UUID;

public interface ClearingCostService {

    ClearingCost save(UserPrincipal user, ClearingCostDTO clearingCostDto);

    ClearingCost update(UserPrincipal user, UUID id, ClearingCostDTO clearingCostDto);

    List<ClearingCostDTO> getAll();

    ClearingCostDTO getById(UUID id);

    void deleteById(UUID id);

    ClearingCostResponseDTO getByCardNumber(String cardNumber);

}

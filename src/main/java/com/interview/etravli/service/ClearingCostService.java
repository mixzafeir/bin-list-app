package com.interview.etravli.service;

import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.models.ClearingCost;

import java.util.List;
import java.util.UUID;

public interface ClearingCostService {

    ClearingCost save(ClearingCostDTO clearingCostDto);

    List<ClearingCostDTO> getAll();

    ClearingCostDTO getById(UUID id);

    void deleteById(UUID id);

    ClearingCostDTO getByCardNumber(String cardNumber);

}

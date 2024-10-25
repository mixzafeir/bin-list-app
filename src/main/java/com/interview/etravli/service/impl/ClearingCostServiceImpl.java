package com.interview.etravli.service.impl;

import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.dto.feign.BinListFeignDTO;
import com.interview.etravli.enums.ExceptionMessages;
import com.interview.etravli.exceptions.EntityNotFoundException;
import com.interview.etravli.models.ClearingCost;
import com.interview.etravli.repository.ClearingCostRepository;
import com.interview.etravli.service.BinListFeignService;
import com.interview.etravli.service.ClearingCostService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClearingCostServiceImpl implements ClearingCostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearingCostServiceImpl.class);

    private final ClearingCostRepository clearingCostRepo;
    private final BinListFeignService binListFeignService;

    @Autowired
    public ClearingCostServiceImpl(ClearingCostRepository clearingCostRepo,
                                   BinListFeignService binListFeignService){
        this.clearingCostRepo = clearingCostRepo;
        this.binListFeignService = binListFeignService;
    }

    @Override
    @Transactional
    public ClearingCost save(ClearingCostDTO clearingCostDto){
        LOGGER.warn("Staring save clearing cost");
        ClearingCost newCost = new ClearingCost();
        newCost.setClearingCost(clearingCostDto.getClearingCost());
        newCost.setCardIssuingCountry(clearingCostDto.getCardIssuingCountry().toUpperCase());
        clearingCostRepo.save(newCost);
        LOGGER.warn("Clearing cost saved");
        return newCost;
    }

    @Override
    @Transactional
    public List<ClearingCostDTO> getAll() {
        LOGGER.info("Fetching all clearing costs");
        return clearingCostRepo.findAll().stream().map(this::entityToDtoMapping).toList();
    }

    @Override
    @Transactional
    public ClearingCostDTO getById(UUID id) {
        LOGGER.info("Fetching clearing cost by id: {}", id.toString());
        ClearingCost result = clearingCostRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.CLEARING_COST_NOT_FOUND)
        );
        return entityToDtoMapping(result);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        LOGGER.info("Deleting clearing cost by id: {}", id.toString());
        clearingCostRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.CLEARING_COST_NOT_FOUND)
        );
        clearingCostRepo.deleteById(id);
    }

    @Override
    @Transactional
    public ClearingCostDTO getByCardNumber(String cardNumber){
        BinListFeignDTO feignResult = binListFeignService.getCardInfoFromFeign(cardNumber.substring(0,6));
        LOGGER.info("Fetching clearing cost by country code: {}", feignResult.getCountry());
        ClearingCost result = clearingCostRepo.findByCardIssuingCountry(feignResult.getCountry().getAlpha2()).orElseGet(
                () -> clearingCostRepo.findByCardIssuingCountry("OT")
                        .orElseThrow(RuntimeException::new));
        return entityToDtoMapping(result);
    }

    private ClearingCostDTO entityToDtoMapping(ClearingCost clearingCost){
        ClearingCostDTO dto = new ClearingCostDTO();
        dto.setClearingCost(clearingCost.getClearingCost());
        dto.setCardIssuingCountry(clearingCost.getCardIssuingCountry());
        return dto;
    }

}

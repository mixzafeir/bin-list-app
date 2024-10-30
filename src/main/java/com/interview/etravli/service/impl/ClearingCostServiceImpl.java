package com.interview.etravli.service.impl;

import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.dto.etraveli.ClearingCostResponseDTO;
import com.interview.etravli.dto.etraveli.UserPrincipal;
import com.interview.etravli.enums.ExceptionMessages;
import com.interview.etravli.exceptions.EntityNotFoundException;
import com.interview.etravli.exceptions.ValidationException;
import com.interview.etravli.models.ClearingCost;
import com.interview.etravli.repository.ClearingCostRepository;
import com.interview.etravli.service.BinListFeignService;
import com.interview.etravli.service.ClearingCostService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    public ClearingCostDTO save(UserPrincipal principal, ClearingCostDTO clearingCostDto){
        LOGGER.warn("Staring save clearing cost");
        ClearingCost newCost = new ClearingCost();
        newCost.setClearingCost(clearingCostDto.getClearingCost());
        newCost.setCardIssuingCountry(clearingCostDto.getCardIssuingCountry().toUpperCase());
        newCost.setCreatedBy(principal.getUsername());
        clearingCostRepo.save(newCost);
        LOGGER.warn("Clearing cost saved");
        return entityToDtoMapping(newCost);
    }

    @Override
    @Transactional
    public ClearingCostDTO update(UserPrincipal principal, UUID id, ClearingCostDTO clearingCostDto){
        LOGGER.warn("Updating clearing cost");
        if(id == null){
            throw new ValidationException(ExceptionMessages.ID_CANNOT_BE_NULL);
        }
        ClearingCost cost = clearingCostRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.CLEARING_COST_NOT_FOUND)
        );
        cost.setClearingCost(clearingCostDto.getClearingCost());
        cost.setCardIssuingCountry(clearingCostDto.getCardIssuingCountry().toUpperCase());
        cost.setModifiedBy(principal.getUsername());
        clearingCostRepo.save(cost);
        LOGGER.warn("Clearing cost updated");
        return entityToDtoMapping(cost);
    }

    @Override
    @Transactional
    public List<ClearingCostDTO> getAll() {
        LOGGER.info("Fetching all clearing costs");
        return clearingCostRepo.findAll().stream().map(this::entityToDtoMapping).toList();
    }

    @Override
    @Transactional
    public ClearingCostResponseDTO getById(UUID id) {
        LOGGER.info("Fetching clearing cost by id: {}", id.toString());
        ClearingCost result = clearingCostRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.CLEARING_COST_NOT_FOUND)
        );
        return entityToResponseDtoMapping(result);
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
    public CompletableFuture<ClearingCostResponseDTO> getByCardNumber(String cardNumber){
        SecurityContext sc = SecurityContextHolder.getContext();
        return binListFeignService.getCardInfoFromFeign(cardNumber.substring(0, 6))
                .thenApplyAsync(feignResult -> {
                    SecurityContextHolder.setContext(sc);
                    LOGGER.info("Fetching clearing cost by country code: {}", feignResult.getCountry());
                    ClearingCost result = clearingCostRepo.findByCardIssuingCountry(feignResult.getCountry().getA2())
                            .orElseGet(() -> clearingCostRepo.findByCardIssuingCountry("OT")
                                    .orElseThrow(RuntimeException::new));
                    LOGGER.info(SecurityContextHolder.getContext().toString());
                    return entityToResponseDtoMapping(result);
                });
    }

    private ClearingCostResponseDTO entityToResponseDtoMapping(ClearingCost clearingCost){
        ClearingCostResponseDTO dto = new ClearingCostResponseDTO();
        dto.setCost(clearingCost.getClearingCost());
        dto.setCountry(clearingCost.getCardIssuingCountry());
        return dto;
    }

    private ClearingCostDTO entityToDtoMapping(ClearingCost clearingCost){
        ClearingCostDTO dto = new ClearingCostDTO();
        dto.setId(clearingCost.getId());
        dto.setClearingCost(clearingCost.getClearingCost());
        dto.setCardIssuingCountry(clearingCost.getCardIssuingCountry());
        return dto;
    }

}

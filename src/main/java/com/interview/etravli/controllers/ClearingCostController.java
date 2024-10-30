package com.interview.etravli.controllers;

import com.interview.etravli.dto.etraveli.CardNumberDTO;
import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.dto.etraveli.ClearingCostResponseDTO;
import com.interview.etravli.dto.etraveli.UserPrincipal;
import com.interview.etravli.models.ClearingCost;
import com.interview.etravli.service.ClearingCostService;
import com.interview.etravli.service.impl.ClearingCostServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/clearing-cost")
@Validated
public class ClearingCostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearingCostController.class);

    private final ClearingCostService clearingCostService;

    @Autowired
    public ClearingCostController(ClearingCostService clearingCostService){
        this.clearingCostService = clearingCostService;
    }

    @PostMapping("/save")
    public ResponseEntity<ClearingCostDTO> createClearingCost(@AuthenticationPrincipal UserPrincipal principal,
                                                                   @Valid @RequestBody ClearingCostDTO dto) {
        ClearingCostDTO newClearingCost = clearingCostService.save(principal, dto);
        return new ResponseEntity<>(newClearingCost, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClearingCostDTO> updateClearingCost(@PathVariable UUID id,
                                                           @AuthenticationPrincipal UserPrincipal principal,
                                                           @Valid @RequestBody ClearingCostDTO dto) {
        ClearingCostDTO newClearingCost = clearingCostService.update(principal, id, dto);
        return new ResponseEntity<>(newClearingCost, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ClearingCostDTO>> getAllClearingCosts() {
        List<ClearingCostDTO> clearingCosts = clearingCostService.getAll();
        return new ResponseEntity<>(clearingCosts, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ClearingCostResponseDTO> getClearingCostById(@PathVariable UUID id) {
        ClearingCostResponseDTO clearingCost = clearingCostService.getById(id);
        return new ResponseEntity<>(clearingCost, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClearingCost(@PathVariable UUID id) {
        clearingCostService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/payment-cards-cost")
    public CompletableFuture<ResponseEntity<ClearingCostResponseDTO>> getClearingCostById(@Valid @RequestBody CardNumberDTO dto) {
        SecurityContext sc = SecurityContextHolder.getContext();
        return clearingCostService.getByCardNumber(dto.getCard_number())
                .thenApply(response -> {
                    SecurityContextHolder.setContext(sc);
                    LOGGER.info(SecurityContextHolder.getContext().toString());
                    return ResponseEntity.ok(response);
                });
    }

//    @PostMapping("/payment-cards-cost")
//    public DeferredResult<ResponseEntity<ClearingCostResponseDTO>> getClearingCostById(@Valid @RequestBody CardNumberDTO dto) {
//        SecurityContext sc = SecurityContextHolder.getContext();
//        DeferredResult<ResponseEntity<ClearingCostResponseDTO>> deferredResult = new DeferredResult<>();
//
//        clearingCostService.getByCardNumber(dto.getCard_number())
//                .thenApply(response -> {
//                    SecurityContextHolder.setContext(sc);
//                    LOGGER.info(SecurityContextHolder.getContext().toString());
//                    return ResponseEntity.ok(response);
//                })
//                .whenComplete((response, throwable) -> {
//                    if (throwable != null) {
//                        LOGGER.error("Error processing request", throwable);
//                        deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
//                    } else {
//                        deferredResult.setResult(response);
//                    }
//                });
//
//        return deferredResult;
//    }


}

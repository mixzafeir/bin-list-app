package com.interview.etravli.controllers;

import com.interview.etravli.dto.etraveli.ClearingCostDTO;
import com.interview.etravli.models.ClearingCost;
import com.interview.etravli.service.ClearingCostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clearing-cost")
@Validated
public class ClearingCostController {

    private final ClearingCostService clearingCostService;

    @Autowired
    public ClearingCostController(ClearingCostService clearingCostService){
        this.clearingCostService = clearingCostService;
    }

    @PostMapping("/save")
    public ResponseEntity<ClearingCost> createOrUpdateClearingCost(@Valid @RequestBody ClearingCostDTO dto) {
        ClearingCost newClearingCost = clearingCostService.save(dto);
        return new ResponseEntity<>(newClearingCost, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ClearingCostDTO>> getAllClearingCosts() {
        List<ClearingCostDTO> clearingCosts = clearingCostService.getAll();
        return new ResponseEntity<>(clearingCosts, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ClearingCostDTO> getClearingCostById(@PathVariable UUID id) {
        ClearingCostDTO clearingCost = clearingCostService.getById(id);
        return new ResponseEntity<>(clearingCost, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClearingCost(@PathVariable UUID id) {
        clearingCostService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/card-number/{cardNumber}")
    public ResponseEntity<ClearingCostDTO> getClearingCostById(@PathVariable
                                                                   @Size(min=8,
                                                                         max=19,
                                                                         message="Wrong Card Number") String cardNumber) {
        ClearingCostDTO clearingCost = clearingCostService.getByCardNumber(cardNumber);
        return new ResponseEntity<>(clearingCost, HttpStatus.OK);
    }


}

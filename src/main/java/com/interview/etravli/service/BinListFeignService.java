package com.interview.etravli.service;

import com.interview.etravli.dto.feign.BinListFeignDTO;

import java.util.concurrent.CompletableFuture;

public interface BinListFeignService {

    CompletableFuture<BinListFeignDTO> getCardInfoFromFeign(String cardNumber);

}

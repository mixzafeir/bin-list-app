package com.interview.etravli.service;

import com.interview.etravli.dto.feign.BinListFeignDTO;

public interface BinListFeignService {

    BinListFeignDTO getCardInfoFromFeign(String cardNumber);

}

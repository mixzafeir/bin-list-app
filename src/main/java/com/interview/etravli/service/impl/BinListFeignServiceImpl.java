package com.interview.etravli.service.impl;

import com.interview.etravli.dto.feign.BinListFeignDTO;
import com.interview.etravli.feign.BinListFeignClient;
import com.interview.etravli.service.BinListFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BinListFeignServiceImpl implements BinListFeignService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinListFeignServiceImpl.class);

    private final BinListFeignClient binListFeignClient;

    @Autowired
    public BinListFeignServiceImpl(BinListFeignClient binListFeignClient){
        this.binListFeignClient = binListFeignClient;
    }

    @Override
    @Cacheable(value = "cardNumberCache", key = "#cardNumber", unless = "#result == null")
    public BinListFeignDTO getCardInfoFromFeign(String cardNumber) {
        LOGGER.info("Fetching BIN LIST for card number from feign: {}", cardNumber);
        return binListFeignClient.getCardInfoBinList(cardNumber, "3");
    }

}

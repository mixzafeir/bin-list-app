package com.interview.etravli.service.impl;

import com.interview.etravli.dto.feign.BinListFeignDTO;
import com.interview.etravli.feign.BinListFeignClient;
import com.interview.etravli.service.BinListFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.stream.Task;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class BinListFeignServiceImpl implements BinListFeignService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinListFeignServiceImpl.class);

    private final BinListFeignClient binListFeignClient;

    private final Executor taskExecutor;

    @Autowired
    public BinListFeignServiceImpl(BinListFeignClient binListFeignClient, Executor taskExecutor){
        this.binListFeignClient = binListFeignClient;
        this.taskExecutor = taskExecutor;
    }

    @Override
    @Async
    @Cacheable(value = "cardNumberCache", key = "#cardNumber", unless = "#result == null")
    public CompletableFuture<BinListFeignDTO> getCardInfoFromFeign(String cardNumber) {
        LOGGER.info("Fetching BIN LIST for card number from feign: {}", cardNumber);
        SecurityContext sc = SecurityContextHolder.getContext();
        return CompletableFuture.supplyAsync(() -> {
            try{
                return binListFeignClient.getCardInfoBinList(cardNumber);
            } finally {
                SecurityContextHolder.setContext(sc);
            }
        }, taskExecutor);
    }

}

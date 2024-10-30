package com.interview.etravli.feign;

import com.interview.etravli.dto.feign.BinListFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="${app-feign-names.bin-list.name}", url="${app-feign-names.bin-list.url}")
public interface BinListFeignClient {

    @GetMapping("/{card-number}")
    BinListFeignDTO getCardInfoBinList(@PathVariable("card-number") String cardNumber);

}

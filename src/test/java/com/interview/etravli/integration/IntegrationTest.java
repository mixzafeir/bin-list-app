package com.interview.etravli.integration;

import com.interview.etravli.dto.etraveli.LoginDTO;
import com.interview.etravli.dto.feign.BinListFeignDTO;
import com.interview.etravli.dto.feign.CountryFeignDTO;
import com.interview.etravli.enums.Roles;
import com.interview.etravli.models.ClearingCost;
import com.interview.etravli.repository.ClearingCostRepository;
import com.interview.etravli.service.BinListFeignService;
import com.interview.etravli.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql({"/schema.sql", "/data.sql"})
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BinListFeignService binListFeignService;

    @Autowired
    private ClearingCostRepository clearingCostRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        adminToken = "Bearer " + jwtUtil.generateToken("demo", "ROLE_ADMIN");
        userToken = "Bearer " + jwtUtil.generateToken("demo2", "ROLE_USER");
    }

    @Test
    @Transactional
    void getClearingCostByCardNumberTest() throws Exception {
        BinListFeignDTO mockResponse = new BinListFeignDTO();
        CountryFeignDTO mockCountry = new CountryFeignDTO();
        mockCountry.setAlpha2("US");
        mockResponse.setCountry(mockCountry);
        when(binListFeignService.getCardInfoFromFeign("123456"))
                .thenReturn(mockResponse);
        String requestBody = "{ \"card_number\": \"1234567812345678\" }";
        mockMvc.perform(post("/api/clearing-cost/payment-cards-cost")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cost").value(new BigDecimal("20.0")));
    }

    @Test
    @Transactional
    void crudClearingCostTest_withCorrectAuth() throws Exception {
        String cardIssuingCountry = "GG";
        BigDecimal clearingCost = new BigDecimal("20.000");
        String requestBody = String.format(
                "{ \"cardIssuingCountry\": \"%s\", \"clearingCost\": %.3f }",
                cardIssuingCountry, clearingCost
        );
        mockMvc.perform(post("/api/clearing-cost/save")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is(201));
        List<ClearingCost> clearingCosts = clearingCostRepo.findAll();
        assertThat(clearingCosts).hasSize(5);

        mockMvc.perform(delete("/api/clearing-cost/delete/323e4567-e89b-12d3-a456-426614174002")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
        clearingCosts = clearingCostRepo.findAll();
        assertThat(clearingCosts).hasSize(4);

        mockMvc.perform(get("/api/clearing-cost/get-all")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        clearingCosts = clearingCostRepo.findAll();
        assertThat(clearingCosts).hasSize(4);

        mockMvc.perform(get("/api/clearing-cost/id/223e4567-e89b-12d3-a456-426614174001")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.clearingCost").value(new BigDecimal("15.0")));

        mockMvc.perform(put("/api/clearing-cost/update/223e4567-e89b-12d3-a456-426614174001")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    void crudClearingCostTest_withFalseAuth() throws Exception {
        mockMvc.perform(get("/api/clearing-cost/get-all")
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void loginTest() throws Exception {
        String requestBody = "{ \"username\": \"demo\", \"password\": \"test\" }";
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.role").value(Roles.ROLE_ADMIN.toString()));
    }

}

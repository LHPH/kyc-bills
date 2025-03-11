package com.kyc.bills.controllers;

import com.kyc.bills.delegate.BillsDelegate;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BillsController.class})
@AutoConfigureMockMvc(addFilters = false)
public class BillsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BillsDelegate delegate;

    @Test
    public void getAllBills_processRequest_returnSuccessfulResponse() throws Exception{

        given(delegate.getAllBills(any(RequestData.class)))
                .willReturn(ResponseData.of(new ArrayList<>()).toResponseEntity());

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAllCustomerBills_processRequest_returnSuccessfulResponse() throws Exception{

        given(delegate.executiveQueryCustomerBills(any(RequestData.class)))
                .willReturn(ResponseData.of(new ArrayList<>()).toResponseEntity());

        mockMvc.perform(get("/customer/{id}","1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getBillById_processRequest_returnSuccessfulResponse() throws Exception{

        given(delegate.getBillById(any(RequestData.class)))
                .willReturn(ResponseData.of(new ArrayList<>()).toResponseEntity());

        mockMvc.perform(get("/bill/{id}","1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}

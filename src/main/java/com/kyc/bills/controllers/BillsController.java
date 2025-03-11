package com.kyc.bills.controllers;

import com.kyc.bills.delegate.BillsDelegate;
import com.kyc.bills.model.BillData;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.kyc.core.constants.GeneralConstants.PARAM_ID;
import static com.kyc.core.constants.GeneralConstants.PARAM_PAGE;

@RestController
public class BillsController {

    @Autowired
    private BillsDelegate delegate;

    @GetMapping("/")
    public ResponseEntity<ResponseData<List<BillData>>> getAllBills(@RequestParam(value = "page",defaultValue = "0",required = false) String page){

        RequestData<Void> requestData = RequestData.<Void>builder()
                .queryParams(Collections.singletonMap(PARAM_PAGE,page))
                .build();

        return delegate.getAllBills(requestData);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<ResponseData<List<BillData>>> getAllCustomerBills(@RequestParam(value = "page",defaultValue = "0",required = false) String page,
                                                                            @PathVariable String id){

        RequestData<Void> requestData = RequestData.<Void>builder()
                .queryParams(Collections.singletonMap(PARAM_PAGE,page))
                .pathParams(Collections.singletonMap(PARAM_ID,id))
                .build();

        return delegate.executiveQueryCustomerBills(requestData);
    }

    @GetMapping("/bill/{id}")
    public ResponseEntity<ResponseData<BillData>> getBillById(@PathVariable String id){

        RequestData<Void> requestData = RequestData.<Void>builder()
                .pathParams(Collections.singletonMap(PARAM_ID,id))
                .build();

        return delegate.getBillById(requestData);
    }
}

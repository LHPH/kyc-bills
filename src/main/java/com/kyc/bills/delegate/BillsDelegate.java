package com.kyc.bills.delegate;

import com.kyc.bills.model.BillData;
import com.kyc.bills.service.BillsService;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillsDelegate {

    @Autowired
    private BillsService billsService;

    public ResponseEntity<ResponseData<List<BillData>>> getAllBills(RequestData<Void> requestData){

        return billsService.getAllBills(requestData).toResponseEntity();
    }

    public ResponseEntity<ResponseData<BillData>> getBillById(RequestData<Void> requestData){

        return billsService.getBillById(requestData).toResponseEntity();
    }
}

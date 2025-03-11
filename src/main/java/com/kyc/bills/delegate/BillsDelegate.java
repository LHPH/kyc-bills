package com.kyc.bills.delegate;

import com.kyc.bills.model.BillData;
import com.kyc.bills.service.BillsService;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.util.GeneralUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.kyc.core.constants.GeneralConstants.PARAM_ID;
import static com.kyc.core.constants.GeneralConstants.PARAM_PAGE;

@Component
public class BillsDelegate {

    @Autowired
    private BillsService billsService;

    public ResponseEntity<ResponseData<List<BillData>>> getAllBills(RequestData<Void> requestData){

        Long customerId = 5L;
        Map<String,String> params = requestData.getQueryParams();

        return billsService.getAllBills(customerId,params.get(PARAM_PAGE)).toResponseEntity();
    }

    public ResponseEntity<ResponseData<List<BillData>>> executiveQueryCustomerBills(RequestData<Void> requestData){

        Long customerId = GeneralUtil.convertOrNull(requestData.getPathParams().get(PARAM_ID),Long.class);
        Map<String,String> params = requestData.getQueryParams();

        return billsService.getAllBills(customerId,params.get(PARAM_PAGE)).toResponseEntity();
    }

    public ResponseEntity<ResponseData<BillData>> getBillById(RequestData<Void> requestData){

        return billsService.getBillById(requestData).toResponseEntity();
    }
}

package com.kyc.bills.delegate;

import com.kyc.bills.model.BillData;
import com.kyc.bills.service.BillsService;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kyc.core.constants.GeneralConstants.PARAM_ID;
import static com.kyc.core.constants.GeneralConstants.PARAM_PAGE;
import static com.kyc.core.util.TestsUtil.getJwt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BillDelegateTest {

    @Mock
    private BillsService service;

    @InjectMocks
    private BillsDelegate delegate;

    @Test
    public void getAllBills_processRequestToService_returnResponseService(){

        RequestData<Void> req = RequestData.<Void>builder()
                .queryParams(Collections.singletonMap(PARAM_PAGE,"1"))
                .build();

        loadMethodSecurity();
        given(service.getAllBills(anyLong(),anyString()))
                .willReturn(ResponseData.of(new ArrayList<>()));

        ResponseEntity<ResponseData<List<BillData>>> result =  delegate.getAllBills(req);
        Assertions.assertNotNull(result);
    }

    @Test
    public void executiveQueryCustomerBills_processRequestToService_returnResponseService(){

        RequestData<Void> req = RequestData.<Void>builder()
                .queryParams(Collections.singletonMap(PARAM_PAGE,"1"))
                .pathParams(Collections.singletonMap(PARAM_ID,"1"))
                .build();

        given(service.getAllBills(anyLong(),anyString()))
                .willReturn(ResponseData.of(new ArrayList<>()));

        ResponseEntity<ResponseData<List<BillData>>> result =  delegate.executiveQueryCustomerBills(req);
        Assertions.assertNotNull(result);
    }

    @Test
    public void getBillById_processRequestToService_returnResponseService(){

        RequestData<Void> req = RequestData.<Void>builder()
                .pathParams(Collections.singletonMap(PARAM_ID,"1"))
                .build();

        loadMethodSecurity();
        given(service.getBillById(any(RequestData.class)))
                .willReturn(ResponseData.of(new BillData()));

        ResponseEntity<ResponseData<BillData>> result =  delegate.getBillById(req);
        Assertions.assertNotNull(result);
    }

    private void loadMethodSecurity(){

        JwtAuthenticationToken jwtAuthenticationToken = Mockito.mock(JwtAuthenticationToken.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        given(securityContext.getAuthentication()).willReturn(jwtAuthenticationToken);
        given(jwtAuthenticationToken.getToken()).willReturn(getJwt());

        SecurityContextHolder.setContext(securityContext);
    }
}

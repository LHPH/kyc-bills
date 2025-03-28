package com.kyc.bills.service;

import com.kyc.bills.entity.BillEntity;
import com.kyc.bills.mappers.BillMapper;
import com.kyc.bills.mappers.BillMapperImpl;
import com.kyc.bills.model.BillData;
import com.kyc.bills.repositories.BillRepository;
import com.kyc.core.enums.KycUserTypeEnum;
import com.kyc.core.exception.KycRestException;
import com.kyc.core.model.MessageData;
import com.kyc.core.model.jwt.JwtData;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.kyc.bills.constants.AppConstants.MESSAGE_002;
import static com.kyc.bills.constants.AppConstants.MESSAGE_004;
import static com.kyc.core.constants.GeneralConstants.PARAM_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BillsServiceTest {

    @Mock
    private BillRepository repository;

    @Spy
    private BillMapper billMapper = new BillMapperImpl();

    @Mock
    private KycMessages kycMessages;

    @InjectMocks
    private BillsService service;

    @BeforeEach
    public void setUp(){

        ReflectionTestUtils.setField(service,"pageSize",1);
    }

    @Test
    public void getAllBills_getCustomerBillsWithProvidedPage_returnListOfBills(){

        given(repository.findByIdCustomerOrderByIdCustomer(anyLong(),any(Pageable.class)))
                .willReturn(new PageImpl<>(Collections.singletonList(new BillEntity())));

        ResponseData<List<BillData>> result = service.getAllBills(1L,"3");

        Assertions.assertFalse(result.getData().isEmpty());
    }

    @Test
    public void getAllBills_getCustomerBillsWhenDatabaseException_throwsKycRestException(){

        Assertions.assertThrows(KycRestException.class,()->{

            given(repository.findByIdCustomerOrderByIdCustomer(anyLong(),any(Pageable.class)))
                    .willThrow(new InvalidDataAccessResourceUsageException("db exception"));
            given(kycMessages.getMessage(MESSAGE_002))
                    .willReturn(new MessageData());

            service.getAllBills(1L,"-3");
        });
    }

    @Test
    public void getBillById_getBillFromDatabaseAndCustomerIsOwner_returnSelectedBill(){

        JwtData jwtData = JwtData.builder()
                .owner(1L)
                .role(KycUserTypeEnum.CUSTOMER.name())
                .build();

        RequestData<Void> req = RequestData.<Void>builder()
                .pathParams(Collections.singletonMap(PARAM_ID,"1"))
                .auth(jwtData)
                .build();

        BillEntity billEntity = new BillEntity();
        billEntity.setIdCustomer(1L);

        given(repository.findById(anyLong()))
                .willReturn(Optional.of(billEntity));

        ResponseData<BillData> result = service.getBillById(req);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
    }

    @Test
    public void getBillById_getBillFromDatabaseAndExecutiveIsRequestor_returnSelectedBill(){

        JwtData jwtData = JwtData.builder()
                .owner(2L)
                .role(KycUserTypeEnum.EXECUTIVE.name())
                .build();

        RequestData<Void> req = RequestData.<Void>builder()
                .pathParams(Collections.singletonMap(PARAM_ID,"1"))
                .auth(jwtData)
                .build();

        BillEntity billEntity = new BillEntity();
        billEntity.setIdCustomer(1L);

        given(repository.findById(anyLong()))
                .willReturn(Optional.of(billEntity));

        ResponseData<BillData> result = service.getBillById(req);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getData());
    }

    @Test
    public void getBillById_invalidId_throwKycRestException(){

        Assertions.assertThrows(KycRestException.class,()->{

            JwtData jwtData = JwtData.builder()
                    .owner(1L)
                    .role(KycUserTypeEnum.CUSTOMER.name())
                    .build();

            RequestData<Void> req = RequestData.<Void>builder()
                    .pathParams(Collections.singletonMap(PARAM_ID,"a"))
                    .auth(jwtData)
                    .build();

            given(kycMessages.getMessage(MESSAGE_004))
                    .willReturn(new MessageData());

            service.getBillById(req);
        });
    }

    @Test
    public void getBillById_billNotFoundInDatabase_throwKycRestException(){

        Assertions.assertThrows(KycRestException.class,()->{

            JwtData jwtData = JwtData.builder()
                    .owner(1L)
                    .role(KycUserTypeEnum.CUSTOMER.name())
                    .build();

            RequestData<Void> req = RequestData.<Void>builder()
                    .pathParams(Collections.singletonMap(PARAM_ID,"1"))
                    .auth(jwtData)
                    .build();

            given(repository.findById(anyLong()))
                    .willReturn(Optional.empty());
            given(kycMessages.getMessage(MESSAGE_004))
                    .willReturn(new MessageData());

            service.getBillById(req);
        });
    }

    @Test
    public void getBillById_billDoesNotBelongCustomer_throwKycRestException(){

        Assertions.assertThrows(KycRestException.class,()->{

            JwtData jwtData = JwtData.builder()
                    .owner(2L)
                    .role(KycUserTypeEnum.CUSTOMER.name())
                    .build();

            RequestData<Void> req = RequestData.<Void>builder()
                    .pathParams(Collections.singletonMap(PARAM_ID,"1"))
                    .auth(jwtData)
                    .build();

            BillEntity billEntity = new BillEntity();
            billEntity.setIdCustomer(1L);

            given(repository.findById(anyLong()))
                    .willReturn(Optional.of(billEntity));
            given(kycMessages.getMessage(MESSAGE_004))
                    .willReturn(new MessageData());

            service.getBillById(req);
        });
    }

    @Test
    public void getBillById_exceptionInDatabase_throwKycRestException(){

        Assertions.assertThrows(KycRestException.class,()->{

            RequestData<Void> req = RequestData.<Void>builder()
                    .pathParams(Collections.singletonMap(PARAM_ID,"1"))
                    .build();

            given(repository.findById(anyLong()))
                    .willThrow(new InvalidDataAccessResourceUsageException("db error"));
            given(kycMessages.getMessage(MESSAGE_002))
                    .willReturn(new MessageData());

            service.getBillById(req);
        });
    }
}

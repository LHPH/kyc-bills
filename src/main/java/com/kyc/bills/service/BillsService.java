package com.kyc.bills.service;

import com.kyc.bills.entity.BillEntity;
import com.kyc.bills.mappers.BillMapper;
import com.kyc.bills.model.BillData;
import com.kyc.bills.repositories.BillRepository;
import com.kyc.core.enums.KycUserTypeEnum;
import com.kyc.core.exception.KycRestException;
import com.kyc.core.model.web.RequestData;
import com.kyc.core.model.web.ResponseData;
import com.kyc.core.properties.KycMessages;
import com.kyc.core.util.GeneralUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kyc.bills.constants.AppConstants.MESSAGE_002;
import static com.kyc.bills.constants.AppConstants.MESSAGE_004;
import static com.kyc.core.constants.GeneralConstants.PARAM_ID;
import static com.kyc.core.constants.GeneralConstants.PARAM_PAGE;


@Service
public class BillsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillsService.class);

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private KycMessages kycMessages;

    @Value("${kyc-config.page-size}")
    private int pageSize;

    public ResponseData<List<BillData>> getAllBills(Long customerId, String strPage){

        try{

            int page = NumberUtils.toInt(strPage,0);
            if(page < 0){
                page = 0;
            }

            Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
            Page<BillEntity> pageEntity = billRepository.findByIdCustomerOrderByIdCustomer(customerId, pageable);

            return ResponseData.of(pageEntity.stream()
                    .map(billMapper::toModel)
                    .toList());
        }
        catch(DataAccessException ex){

            throw KycRestException.builderRestException()
                    .errorData(kycMessages.getMessage(MESSAGE_002))
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .inputData(customerId)
                    .build();
        }
    }

    public ResponseData<BillData> getBillById(RequestData<Void> requestData){

        try{
            Long id = GeneralUtil.convertOrNull(requestData.getPathParams().get(PARAM_ID),Long.class);

            if(id!=null){

                Optional<BillEntity> opEntity = billRepository.findById(id);
                Long owner = requestData.getAuth().getOwner();
                KycUserTypeEnum role = KycUserTypeEnum.getInstance(requestData.getAuth().getRole());

                if(opEntity.isPresent() &&
                        (opEntity.get().getIdCustomer().equals(owner) || KycUserTypeEnum.EXECUTIVE.equals(role))){

                    return ResponseData.of(billMapper.toModel(opEntity.get()));
                }
            }

            throw KycRestException.builderRestException()
                    .errorData(kycMessages.getMessage(MESSAGE_004))
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .inputData(requestData)
                    .build();
        }
        catch(DataAccessException ex){

            throw KycRestException.builderRestException()
                    .errorData(kycMessages.getMessage(MESSAGE_002))
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .inputData(requestData)
                    .build();
        }
    }
}

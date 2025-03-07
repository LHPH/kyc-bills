package com.kyc.bills.mappers;

import com.kyc.bills.entity.BillEntity;
import com.kyc.bills.model.BillData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BillMapper {

    @Mapping(target = "status",source = "source",qualifiedByName = "getStatus")
    BillData toModel(BillEntity source);

    @Named("getStatus")
    static String getStatus(BillEntity source){
        Integer idStatus = source.getIdStatus();
        return "status";
    }
}

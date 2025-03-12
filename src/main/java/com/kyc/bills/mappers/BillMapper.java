package com.kyc.bills.mappers;

import com.kyc.bills.entity.BillEntity;
import com.kyc.bills.model.BillData;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BillMapper {

    @Mapping(target = "status",source = "source",qualifiedByName = "getStatus")
    BillData toModel(BillEntity source);

    @Named("getStatus")
    static String getStatus(BillEntity source){

        Integer idStatus = ObjectUtils.defaultIfNull(source.getIdStatus(),0);
        return switch(idStatus){
            case 1 -> "PAID";
            case 2 -> "CANCELED";
            case 3 -> "EXPIRED";
            case 4 -> "VALID";
            default -> "UNKNOWN";
        };
    }
}

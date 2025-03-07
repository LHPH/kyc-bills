package com.kyc.bills.repositories;

import com.kyc.bills.entity.BillEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BillRepository extends PagingAndSortingRepository<BillEntity, Long>, CrudRepository<BillEntity,Long> {

    Page<BillEntity> findByIdCustomerOrderByIdCustomer(Long idCustomer, Pageable pageable);
}

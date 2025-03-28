package com.kyc.bills.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Table("KYC_CUSTOMER_BILL_CONTROL")
public class BillEntity {

    @Id
    private Long id;

    @Column(value = "TAXES")
    private Double taxes;

    @Column(value = "SUBTOTAL_AMOUNT")
    private Double subtotal;

    @Column(value = "TOTAL_AMOUNT")
    private Double total;

    @Column(value = "ID_CUSTOMER")
    private Long idCustomer;

    @Column(value = "SETTLED")
    private boolean settled;

    @Column(value = "ID_STATUS")
    private Integer idStatus;

    @Column(value = "ISSUE_DATE")
    private LocalDateTime issueDate;

    @Column(value = "BILLING_START_DATE")
    private LocalDate billingStartDate;

    @Column(value = "BILLING_FINISH_DATE")
    private LocalDate billingFinishDate;

    @Column(value = "PAYMENT_DUE_DATE")
    private LocalDate paymentDueDate;

    @Column(value = "SETTLEMENT_DATE")
    private LocalDateTime settlementDate;

}

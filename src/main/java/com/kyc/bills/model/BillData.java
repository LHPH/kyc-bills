package com.kyc.bills.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class BillData {

    private Long id;
    private Double taxes;
    private Double subtotal;
    private Double total;
    private boolean settled;
    private String status;
    private LocalDateTime issueDate;
    private LocalDate billingStartDate;
    private LocalDate billingFinishDate;
    private LocalDate paymentDueDate;
    private LocalDateTime settlementDate;
}

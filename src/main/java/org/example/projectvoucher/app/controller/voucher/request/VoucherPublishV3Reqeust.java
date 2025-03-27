package org.example.projectvoucher.app.controller.voucher.request;

import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;

public record VoucherPublishV3Reqeust(
    RequesterType requesterType,
    String requesterId, // 주문 번호
    String contractCode,
    VoucherAmountType amountType

) {}

package org.example.projectvoucher.app.controller.voucher.request;

import org.example.projectvoucher.common.type.RequesterType;

public record VoucherDisableV2Reqeust(
    RequesterType requesterType,
    String requesterId, // 주문 번호
    String code) {

}

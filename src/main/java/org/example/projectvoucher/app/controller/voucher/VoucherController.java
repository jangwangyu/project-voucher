package org.example.projectvoucher.app.controller.voucher;

import java.time.LocalDate;
import org.example.projectvoucher.app.controller.voucher.request.VoucherDisableV2Reqeust;
import org.example.projectvoucher.app.controller.voucher.request.VoucherPublishReqeust;
import org.example.projectvoucher.app.controller.voucher.request.VoucherPublishV2Reqeust;
import org.example.projectvoucher.app.controller.voucher.request.VoucherUseV2Reqeust;
import org.example.projectvoucher.app.controller.voucher.response.VoucherDisableV2Response;
import org.example.projectvoucher.app.controller.voucher.response.VoucherPublishResponse;
import org.example.projectvoucher.app.controller.voucher.response.VoucherPublishV2Response;
import org.example.projectvoucher.app.controller.voucher.response.VoucherUseV2Response;
import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.domain.service.VoucherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoucherController {
  private final VoucherService voucherService;


  public VoucherController(VoucherService voucherService) {
    this.voucherService = voucherService;
  }

  // 상품권 발행
  @PostMapping("api/v1/voucher")
  public VoucherPublishResponse publish(@RequestBody final VoucherPublishReqeust request){
    final String publishedvoucherCode = voucherService.publish(LocalDate.now(), LocalDate.now().plusDays(1830L),
        request.amountType());
    return new VoucherPublishResponse(publishedvoucherCode);
  }
  // 상품권 사용
  @PutMapping("api/v1/voucher/use")
  public void use(@RequestBody final String code){
    voucherService.use(code);
  }

  // 상품권 폐기
  @PutMapping("api/v1/voucher/disable")
  public void disable(@RequestBody final String code){
    voucherService.disable(code);
  }

  // 상품권 발행
  @PostMapping("api/v2/voucher")
  public VoucherPublishV2Response publishV2(@RequestBody final VoucherPublishV2Reqeust request){
    final String publishedvoucherCode = voucherService.publishV2(new RequestContext(request.requesterType(),request.requesterId()),LocalDate.now(), LocalDate.now().plusDays(1830L),
        request.amountType());
    final String orderId = java.util.UUID.randomUUID().toString().toUpperCase().replace("-", "");
    return new VoucherPublishV2Response(orderId, publishedvoucherCode);
  }
  // 상품권 사용
  @PutMapping("api/v2/voucher/use")
  public VoucherUseV2Response useV2(@RequestBody final VoucherUseV2Reqeust request){
    System.out.println("### request.code = + " + request.code());

    final String orderId = java.util.UUID.randomUUID().toString().toUpperCase().replace("-", "");
    voucherService.useV2(new RequestContext(request.requesterType(),request.requesterId()), request.code());
    return new VoucherUseV2Response(orderId);
  }

  // 상품권 폐기
  @PutMapping("api/v2/voucher/disable")
  public VoucherDisableV2Response disableV2(@RequestBody VoucherDisableV2Reqeust request){
    final String orderId = java.util.UUID.randomUUID().toString().toUpperCase().replace("-", "");
    voucherService.disableV2(new RequestContext(request.requesterType(),request.requesterId()), request.code());
    return new VoucherDisableV2Response(orderId);
  }
}

package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.AgreementOrderDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.model.enums.EAgreementOrder;
import pl.com.app.service.AgreementOrderService;
import pl.com.app.service.AgreementService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agreements/order")
@RequiredArgsConstructor
public class AgreementOrderController {

    private final AuthenticationFacade authenticationFacade;
    private final AgreementOrderService agreementOrderService;
    private final AgreementService agreementService;

    @GetMapping("/extend/confirmation")
    public ResponseEntity<ResponseMessage<AgreementOrderDTO>> confirmExtendAgreementToUser(@RequestParam String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<AgreementOrderDTO>builder().data(agreementOrderService.confirmAgreementOrder(token, EAgreementOrder.EXTEND)).build());

    }

    @GetMapping("/resign/confirmation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<AgreementOrderDTO>> confirmResignAgreementToUser(@RequestParam String token) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<AgreementOrderDTO>builder().data(agreementOrderService.confirmAgreementOrder(token, EAgreementOrder.RESIGN)).build());
    }


    @GetMapping("/not-confirm")
    public ResponseEntity<ResponseMessage<List<AgreementOrderDTO>>> findAllNotConfirm() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<AgreementOrderDTO>>builder().data(agreementOrderService.findAllNotConfirm()).build());
    }

    @PatchMapping("/confirm/order/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<AgreementOrderDTO>> confirmOrder(@PathVariable Long id,
                                                                           @RequestBody Map<String, Object> params) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<AgreementOrderDTO>builder().data(agreementOrderService.confirmOrder(id, params)).build());
    }


}

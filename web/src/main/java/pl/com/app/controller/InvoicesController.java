package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.InfoDTO;
import pl.com.app.dto.InvoiceDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.InvoiceService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoicesController {

    private final AuthenticationFacade authenticationFacade;
    private final InvoiceService invoiceService;

    @GetMapping("/user")
    public ResponseEntity<ResponseMessage<List<InvoiceDTO>>> findAllByUser() {

        UserDTO loggedUser = authenticationFacade.getLoggedUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<InvoiceDTO>>builder().data(invoiceService.findAllByUser(loggedUser)).build());
    }

    @GetMapping("/payment")
    public ResponseEntity<ResponseMessage<InfoDTO>> invoicePayment(@RequestParam("token") String invoiceNumber) {

        invoiceService.checkPayment(invoiceNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(InfoDTO.builder().info("Payment OK.").build()).build());
    }


    @GetMapping("/check-payment")
    public ResponseEntity<ResponseMessage<List<InvoiceDTO>>> findAllToCheckPayment() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<InvoiceDTO>>builder().data(invoiceService.findAllToCheckPayment()).build());
    }

    @PatchMapping("/check-payment/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<InvoiceDTO>> setPaymentStatusOk(@PathVariable("id") Long id, @RequestBody Map<String, Object> params) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InvoiceDTO>builder().data(invoiceService.setPaymentStatus(id, params)).build());
    }

}

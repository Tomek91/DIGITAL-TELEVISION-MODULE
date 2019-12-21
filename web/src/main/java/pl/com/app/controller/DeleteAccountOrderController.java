package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.DeleteAccountOrderDTO;
import pl.com.app.dto.InfoDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.DeleteAccountOrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/delete-account")
@RequiredArgsConstructor
public class DeleteAccountOrderController {

    private final AuthenticationFacade authenticationFacade;
    private final DeleteAccountOrderService deleteAccountOrderService;


    @PostMapping("/user/{id}")
    public ResponseEntity<ResponseMessage<InfoDTO>> sendMailConfirmation(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(deleteAccountOrderService.sendMailConfirmation(id)).build());
    }

    @GetMapping("/confirmation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<DeleteAccountOrderDTO>> confirmResignAgreementToUser(@RequestParam String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<DeleteAccountOrderDTO>builder().data(deleteAccountOrderService.confirmDeleteAccountOrder(token)).build());
    }


    @GetMapping("/not-confirm")
    public ResponseEntity<ResponseMessage<List<DeleteAccountOrderDTO>>> findAllNotConfirm() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<DeleteAccountOrderDTO>>builder().data(deleteAccountOrderService.findAllNotConfirm()).build());
    }

    @PatchMapping("/confirm/order/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<InfoDTO>> confirmOrder(@PathVariable Long id,
                                                                 @RequestBody Map<String, Object> params) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(deleteAccountOrderService.confirmDeleteAccount(id, params)).build());
    }


}

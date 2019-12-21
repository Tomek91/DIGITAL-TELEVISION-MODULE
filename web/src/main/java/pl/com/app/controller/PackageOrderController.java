package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.InfoDTO;
import pl.com.app.dto.PackageOrderDTO;
import pl.com.app.dto.TvPackageDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.model.enums.OperationOrder;
import pl.com.app.service.PackageOrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/package-orders")
@RequiredArgsConstructor
public class PackageOrderController {

    private final PackageOrderService packageOrderService;
    private final AuthenticationFacade authenticationFacade;


    @PostMapping("/add/{id}")
    public ResponseEntity<ResponseMessage<InfoDTO>> addPackageToUser(@PathVariable Long id,
                                                                     HttpServletRequest request) {
         UserDTO loggedUser = authenticationFacade.getLoggedUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(packageOrderService.sendOrderConfirmationMail(id, loggedUser, request)).build());
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<ResponseMessage<InfoDTO>> cancelPackageToUser(@PathVariable Long id,
                                                                        HttpServletRequest request) {
         UserDTO loggedUser = authenticationFacade.getLoggedUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(packageOrderService.sendCancelConfirmationMail(id, loggedUser, request)).build());
    }

    @GetMapping("/add/{id}/confirmation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<PackageOrderDTO>> confirmAddPackageToUser(@RequestParam("token") String token,
                                                                                    @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<PackageOrderDTO>builder().data(packageOrderService.confirmUserOrder(token, OperationOrder.ADD, id)).build());
    }

    @GetMapping("/cancel/{id}/confirmation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<PackageOrderDTO>> confirmCancelPackageToUser(@RequestParam("token") String token,
                                                                                       @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<PackageOrderDTO>builder().data(packageOrderService.confirmUserOrder(token, OperationOrder.CANCEL, id)).build());
    }

   @GetMapping("/not-confirm")
    public ResponseEntity<ResponseMessage<List<PackageOrderDTO>>> findAllNotConfirm() {
       return ResponseEntity
               .status(HttpStatus.OK)
               .headers(null)
               .body(ResponseMessage.<List<PackageOrderDTO>>builder().data(packageOrderService.findAllNotConfirm()).build());
    }

    @PostMapping("/confirm/order/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<PackageOrderDTO>> confirmOrder(@PathVariable Long id,
                                                                         RequestEntity<TvPackageDTO> requestEntity) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<PackageOrderDTO>builder().data(packageOrderService.confirmOrder(id, requestEntity.getBody())).build());
    }

}

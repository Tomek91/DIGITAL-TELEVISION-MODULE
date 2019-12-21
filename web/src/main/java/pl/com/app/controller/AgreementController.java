package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.AgreementDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.AgreementService;

import java.util.List;

@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final AuthenticationFacade authenticationFacade;
    private final AgreementService agreementService;

    @GetMapping
    public ResponseEntity<ResponseMessage<List<AgreementDTO>>> findAll() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<AgreementDTO>>builder().data(agreementService.findAll()).build());

    }

    @GetMapping("/user")
    public ResponseEntity<ResponseMessage<AgreementDTO>> findAllByUser() {
        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<AgreementDTO>builder().data(agreementService.findUserPackages(loggedUser)).build());

    }


}

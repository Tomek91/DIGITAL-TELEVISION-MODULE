package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.com.app.dto.InfoDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.RegistrationService;
import pl.com.app.validators.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @InitBinder
    private void intBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(userValidator);
    }

    @PostMapping("/new")
    public ResponseEntity<ResponseMessage<InfoDTO>> addUser(@Valid @RequestBody UserDTO userDTO,
                                                            BindingResult bindingResult,
                                                            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(null)
                    .body(ResponseMessage.<InfoDTO>builder().data(InfoDTO.builder().info("add user validation errors: " + errors).build()).build());
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(registrationService.registerNewUser(userDTO, request)).build());
    }


    @GetMapping("/registerConfirmation")
    public ResponseEntity<ResponseMessage<InfoDTO>> registrationConfirmation(@RequestParam("token") String token) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(registrationService.confirmRegistration(token)).build());
    }
}

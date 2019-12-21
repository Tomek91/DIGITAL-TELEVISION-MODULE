package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.TvPackageDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.PackageService;

import java.util.List;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;
    private final AuthenticationFacade authenticationFacade;


    @GetMapping
    public ResponseEntity<ResponseMessage<List<TvPackageDTO>>> findAll() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<TvPackageDTO>>builder().data(packageService.findAll()).build());
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseMessage<List<TvPackageDTO>>> findAllUserPackages() {
       UserDTO loggedUser = authenticationFacade.getLoggedUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<TvPackageDTO>>builder().data(packageService.findAllUserPackages(loggedUser)).build());
    }

    @GetMapping("/not-user")
    public ResponseEntity<ResponseMessage<List<TvPackageDTO>>> findAllNotUserPackages() {
         UserDTO loggedUser = authenticationFacade.getLoggedUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<TvPackageDTO>>builder().data(packageService.findAllNotUserPackages(loggedUser)).build());
    }



}

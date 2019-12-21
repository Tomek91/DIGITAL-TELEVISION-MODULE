package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.app.dto.InfoDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.DataInitializerService;

@RestController
@RequestMapping("/init-data")
@RequiredArgsConstructor
public class DataInitializerController {

    private final DataInitializerService dataInitializerService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<InfoDTO>> dataInit() {

        HttpHeaders httpHeaders = new HttpHeaders();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(dataInitializerService.initData()).build());

    }

}

package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.PreferenceDTO;
import pl.com.app.dto.TvPackageInfoDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.PreferenceService;

import java.util.List;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;
    private final AuthenticationFacade authenticationFacade;


    @GetMapping("/category")
    public ResponseEntity<ResponseMessage<List<String>>> findAllCategoryPreferences() {
        HttpHeaders httpHeaders = new HttpHeaders();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(ResponseMessage.<List<String>>builder().data(preferenceService.findAllCategoryPreferences()).build());
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseMessage<List<PreferenceDTO>>> findAllUserPreferences() {
        HttpHeaders httpHeaders = new HttpHeaders();
        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(ResponseMessage.<List<PreferenceDTO>>builder().data(preferenceService.findAllUserCategoryPreferences(loggedUser)).build());
    }

    @PostMapping
    public ResponseEntity<ResponseMessage<List<PreferenceDTO>>> addCategoryPreferencesToUser(RequestEntity<List<String>> requestEntity) {
        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<PreferenceDTO>>builder().data(preferenceService.addCategoryPreferencesToUser(loggedUser, requestEntity.getBody())).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<PreferenceDTO>> deleteCategoryPreferencesToUser(@PathVariable Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<PreferenceDTO>builder().data(preferenceService.deleteCategoryPreferencesToUser(id)).build());
    }

    @GetMapping("/tv-packages")
    public ResponseEntity<ResponseMessage<List<TvPackageInfoDTO>>> findAllTvPackageByUserCategoryPreferences() {
        HttpHeaders httpHeaders = new HttpHeaders();
        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(ResponseMessage.<List<TvPackageInfoDTO>>builder().data(preferenceService.findAllTvPackageByUserCategoryPreferences(loggedUser)).build());
    }

}

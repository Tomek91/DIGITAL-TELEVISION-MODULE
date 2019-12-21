package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;

    @GetMapping("/locked-users")
    public ResponseEntity<ResponseMessage<List<UserDTO>>> findLockedUsers() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<UserDTO>>builder().data(userService.findLockedUsers()).build());

    }

    @PatchMapping("/locked-users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<UserDTO>> setUserLockStatus(@PathVariable("id") Long id, @RequestBody Map<String, Object> params) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<UserDTO>builder().data(userService.setUserLockStatus(id, params)).build());
    }

}

package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.InfoDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.FileService;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final AuthenticationFacade authenticationFacade;
    private final FileService fileService;

    @PostMapping("/upload-to-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage<InfoDTO>> uploadFiles(
          //  @RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadFiles) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<InfoDTO>builder().data(fileService.getFilesAndSendMail(uploadFiles)).build());

    }


}

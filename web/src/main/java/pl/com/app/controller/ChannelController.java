package pl.com.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.app.dto.ChannelDTO;
import pl.com.app.dto.TvPackageInfoDTO;
import pl.com.app.dto.rest.ResponseMessage;
import pl.com.app.service.ChannelService;

import java.util.List;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/packages")
    public ResponseEntity<ResponseMessage<List<TvPackageInfoDTO>>> findAllGroupByTvPackage() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<TvPackageInfoDTO>>builder().data(channelService.findAllGroupByTvPackage()).build());
    }

    @GetMapping
    public ResponseEntity<ResponseMessage<List<ChannelDTO>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(null)
                .body(ResponseMessage.<List<ChannelDTO>>builder().data(channelService.findAll()).build());
    }


}

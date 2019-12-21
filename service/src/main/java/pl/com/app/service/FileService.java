package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.com.app.dto.InfoDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.User;
import pl.com.app.repository.UserRepository;
import pl.com.app.service.listeners.*;

import java.io.File;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    @Value("${imgPath}")
    private String imgPath;

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Environment environment;


    private String createFilename(MultipartFile file) {
        final String originalFilename = file.getOriginalFilename();
        final String[] arr = originalFilename.split("\\.");
        final String extension = arr[arr.length - 1];
        final String filename = Base64.getEncoder().encodeToString(
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
                        .getBytes()
        );
        return String.join(".", filename, extension);
    }

    public String addFile(MultipartFile file) {
        try {

            if (file == null) {
                throw new NullPointerException("FILE IS NULL");
            }

            final String filename = createFilename(file);
            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(file.getBytes(), new File(fullPath));
            return filename;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public String updateFile(MultipartFile file, String filename) {
        try {

            if (file == null || file.getBytes().length == 0) {
                return filename;
            }

            if (filename == null) {
                throw new NullPointerException("FILENAME IS NULL");
            }

            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(file.getBytes(), new File(fullPath));
            return filename;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public String deleteImg(String filename) {
        try {

            if (filename == null) {
                throw new NullPointerException("FILENAME IS NULL");
            }
            if (!filename.startsWith("pic")) {
                final String fullPath = imgPath + filename;
                new File(fullPath).delete();
            }
            return filename;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public void deleteFile(String fullPath) {
        try {

            if (fullPath == null) {
                throw new NullPointerException("PATH IS NULL");
            }
            new File(fullPath).delete();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }


    public InfoDTO getFilesAndSendMail(MultipartFile[] uploadFiles) {
        try {
            if (uploadFiles == null) {
                throw new NullPointerException("UPLOAD FILES IS NULL");
            }

            String uploadedFileName =
                    Arrays.stream(uploadFiles)
                            .map(MultipartFile::getOriginalFilename)
                            .filter(x -> x != null && !x.isBlank())
                            .collect(Collectors.joining(" , "));


            List<User> userList = userRepository.findAll();

            final String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("local.server.port") + "/";
            userList.forEach(x -> eventPublisher.publishEvent(new OnFileAttachEvenData(url, x, uploadFiles)));

            return InfoDTO.builder().info("Successfully uploaded - " + uploadedFileName).build();

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }

    public void saveUploadedFiles(List<MultipartFile> uploadFiles) {
        try {
            for (MultipartFile file : uploadFiles) {
                if (file.isEmpty()) {
                    continue;
                }
                addFile(file);
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.FILE, e.getMessage());
        }
    }
}

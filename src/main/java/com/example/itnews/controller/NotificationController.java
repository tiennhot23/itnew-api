package com.example.itnews.controller;

import com.example.itnews.config.MailSenderService;
import com.example.itnews.entity.Account;
import com.example.itnews.entity.Notification;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.payloads.response.MResponse;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.security.jwt.JwtTokenProvider;
import com.example.itnews.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/notification")
@Slf4j(topic = "NotificationController")
public class NotificationController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FollowTagService followTagService;
    @Autowired
    private FollowAccountService followAccountService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private PostService postService;
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private LockAccountService lockAccountService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private DriveService driveService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DeleteMapping("/{id_notification}")
    @PreAuthorize("permitAll()")
    public MResponse<?> deleteNotification(@PathVariable("id_notification") Integer idNotification,
                                           @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (!notificationService.isExist(idNotification)) {
                throw new MException("Th??ng b??o kh??ng t???n t???i", HttpStatus.NOT_FOUND);
            }

            Notification notification = notificationService.get(idNotification);
            if (!account.getIdAccount().equals(notification.getAccount().getIdAccount())) {
                throw new MException("B???n kh??ng c?? quy???n xo?? th??ng b??o n??y", HttpStatus.FORBIDDEN);
            }

            notificationService.delete(idNotification);
            return new MResponse<>("Xo?? th??ng b??o th??nh c??ng");
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @GetMapping("/count")
    @PreAuthorize("permitAll()")
    public MResponse<?> countUnreadNotification(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<Notification> list = notificationService.listUnreadNotification(account.getIdAccount());
            return new MResponse<>("L???y s??? l?????ng th??ng b??o ch??a ?????c", list.size());
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/all")
    @PreAuthorize("permitAll()")
    public MResponse<?> getAllUnreadNotification(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<Notification> list = notificationService.listUnreadNotification(account.getIdAccount());
            return new MResponse<>("L???y danh s??ch th??ng b??o ch??a ?????c th??nh c??ng", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @GetMapping("/list")
    @PreAuthorize("permitAll()")
    public MResponse<?> getAllNotification(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<Notification> list = notificationService.listAllNotification(account.getIdAccount());
            return new MResponse<>("L???y danh s??ch to??n b??? th??ng b??o th??nh c??ng", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @GetMapping("/read_all")
    @PreAuthorize("permitAll()")
    public MResponse<?> markAllNotificationAsRead(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            notificationService.readAllNotification(account.getIdAccount());
            return new MResponse<>("????nh d???u ?????c t???t c??? th??ng b??o th??nh c??ng");
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @DeleteMapping("/delete_all")
    @PreAuthorize("permitAll()")
    public MResponse<?> deleteAllNotification(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            notificationService.deleteAllNotification(account.getIdAccount());
            return new MResponse<>("X??a t???t c??? th??ng b??o th??nh c??ng");
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id_notification}")
    @PreAuthorize("permitAll()")
    public MResponse<?> getNotificationNotMarkAsRead(@PathVariable("id_notification") Integer idNotification,
                                           @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (!notificationService.isExist(idNotification)) {
                throw new MException("Th??ng b??o kh??ng t???n t???i", HttpStatus.NOT_FOUND);
            }

            Notification notification = notificationService.get(idNotification);
            return new MResponse<>("L???y th??ng b??o th??nh c??ng", notification);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id_notification}/read")
    @PreAuthorize("permitAll()")
    public MResponse<?> markNotificationAsRead(@PathVariable("id_notification") Integer idNotification,
                                                  @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (!notificationService.isExist(idNotification)) {
                throw new MException("Th??ng b??o kh??ng t???n t???i", HttpStatus.NOT_FOUND);
            }

            Notification notification = notificationService.get(idNotification);
            if (!account.getIdAccount().equals(notification.getAccount().getIdAccount())) {
                throw new MException("B???n kh??ng c?? quy???n ?????c th??ng b??o c???a ng?????i kh??c!", HttpStatus.FORBIDDEN);
            }
            notificationService.readNotification(idNotification);
            return new MResponse<>("?????c th??ng b??o th??nh c??ng", notification);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw new MException(e.getMessage(), ((MException) e).getHttpStatus());
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

}

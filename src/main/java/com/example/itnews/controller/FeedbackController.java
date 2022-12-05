package com.example.itnews.controller;

import com.example.itnews.config.MailSenderService;
import com.example.itnews.dto.sqlmapping.IFeedbackDTO;
import com.example.itnews.entity.Account;
import com.example.itnews.entity.Feedback;
import com.example.itnews.entity.Image;
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
@RequestMapping("/api/v2/feedback")
@Slf4j(topic = "FeedbackController")
public class FeedbackController {

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
    private ImageService imageService;
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

    @GetMapping("/{id_feedback}")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> getFeedback(@PathVariable("id_feedback") Integer idFeedback,
                                    @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            IFeedbackDTO feedback = feedbackService.selectID(idFeedback);
            return new MResponse<>("Thao tác thành công" ,feedback);
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

    @GetMapping("/information/{id_feedback}")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> getFeedbackMarkAsRead(@PathVariable("id_feedback") Integer idFeedback,
                                    @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            feedbackService.readFeedback(idFeedback);
            IFeedbackDTO feedback = feedbackService.selectID(idFeedback);
            return new MResponse<>("Thao tác thành công" ,feedback);
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

    @GetMapping("/unread")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> getUnreadFeedback(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<IFeedbackDTO> list = feedbackService.selectUnread();
            return new MResponse<>("Thao tác thành công" ,list);
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
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> getAllFeedback(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<IFeedbackDTO> list = feedbackService.selectAll();
            return new MResponse<>("Thao tác thành công" ,list);
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

    @GetMapping("/all/amount")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> countFeedback(@RequestParam(value = "is_unread", required = false) Integer isUnread,
                                       @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            int amount = 0;
            if (isUnread == null || isUnread == 0) {
                amount = feedbackService.selectAll().size();
            } else {
                amount = feedbackService.selectUnread().size();
            }
            return new MResponse<>("Thao tác thành công, trả về số lượng" ,amount);
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

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public MResponse<?> createFeedback(@RequestBody Feedback body,
                                       @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (body.getContent() == null || body.getContent().length() == 0
                || body.getSubject() == null || body.getSubject().length() == 0) {
                throw new MException("Thiếu body", HttpStatus.BAD_REQUEST);
            } else {
                Feedback feedback = new Feedback();
                feedback.setContent(body.getContent());
                feedback.setSubject(body.getSubject());
                feedback.setAccount(account);
                feedbackService.save(feedback);
                return new MResponse<>("Thêm phản hồi thành công");
            }
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

    @DeleteMapping("/{id_feedback}")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> deleteFeedback(@PathVariable(value = "id_feedback") Integer idFeedback,
                                       @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (feedbackService.isExist(idFeedback)) {
                feedbackService.delete(idFeedback);
                return new MResponse<>("Thao tác thành công");
            } else {
                throw new MException("Không có phản hồi này", HttpStatus.NOT_FOUND);
            }
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

    @PutMapping("/{id_feedback}/read")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> markAsRead(@PathVariable(value = "id_feedback") Integer idFeedback,
                                       @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (feedbackService.isExist(idFeedback)) {
                feedbackService.readFeedback(idFeedback);
                return new MResponse<>("Đánh dấu đã đọc phản hồi thành công!");
            } else {
                throw new MException("Không có phản hồi này", HttpStatus.NOT_FOUND);
            }
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

    @PutMapping("/{id_feedback}/unread")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> markAsUnread(@PathVariable(value = "id_feedback") Integer idFeedback,
                                   @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (feedbackService.isExist(idFeedback)) {
                feedbackService.unreadFeedback(idFeedback);
                return new MResponse<>("Đánh dấu chưa đọc phản hồi thành công!");
            } else {
                throw new MException("Không có phản hồi này", HttpStatus.NOT_FOUND);
            }
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

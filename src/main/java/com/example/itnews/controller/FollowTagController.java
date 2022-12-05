package com.example.itnews.controller;

import com.example.itnews.config.MailSenderService;
import com.example.itnews.entity.*;
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

@RestController
@RequestMapping("/api/v2/follow_tag")
@Slf4j(topic = "FollowTagController")
public class FollowTagController {
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

    @PostMapping("/{id_tag}")
    @PreAuthorize("permitAll()")
    public MResponse<?> followTag(@PathVariable("id_tag") Integer idTag,
                                     @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (tagService.hasId(idTag)) {
                if (followTagService.isExist(account.getIdAccount(), idTag)) {
                    throw new MException("Bạn đã theo dõi thẻ này rồi", HttpStatus.BAD_REQUEST);
                } else {
                    followTagService.save(new FollowTag(new FollowTagId(account.getIdAccount(), idTag), null));
                    return new MResponse<>("Theo dõi thẻ thành công");
                }
            } else {
                throw new MException("Thẻ không tồn tại", HttpStatus.NOT_FOUND);
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

    @DeleteMapping("/{id_tag}")
    @PreAuthorize("permitAll()")
    public MResponse<?> unfollowTag(@PathVariable("id_tag") Integer idTag,
                                   @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (tagService.hasId(idTag)) {
                if (followTagService.isExist(account.getIdAccount(), idTag)) {
                    followTagService.delete(account.getIdAccount(), idTag);
                    return new MResponse<>("Bỏ theo dõi thẻ thành công");
                } else {
                    throw new MException("Bạn chưa theo dõi thẻ này", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new MException("Thẻ không tồn tại", HttpStatus.NOT_FOUND);
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

    @DeleteMapping("")
    @PreAuthorize("permitAll()")
    public MResponse<?> unfollowAllTag(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            followTagService.deleteAll(account.getIdAccount());
            return new MResponse<>("Bỏ theo dõi tất cả các tag thành công");
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

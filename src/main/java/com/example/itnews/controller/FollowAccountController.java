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
@RequestMapping("/api/v2/follow_account")
@Slf4j(topic = "FollowAccountController")
public class FollowAccountController {
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

    @PostMapping("/{id_following}")
    @PreAuthorize("permitAll()")
    public MResponse<?> followAccount(@PathVariable("id_following") Integer idFollowing,
                                  @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (idFollowing.equals(account.getIdAccount())) {
                throw new MException("Kh??ng th??? t??? theo d??i b???n th??n", HttpStatus.BAD_REQUEST);
            }
            if (accountService.isAccountExisted(idFollowing)) {
                if (followAccountService.isExist(account.getIdAccount(), idFollowing)) {
                    throw new MException("B???n ???? theo d??i t??i kho???n n??y", HttpStatus.BAD_REQUEST);
                } else {
                    followAccountService.save(new FollowAccount(new FollowAccountId(account.getIdAccount(), idFollowing), null));
                    return new MResponse<>("Theo d??i t??i kho???n th??nh c??ng");
                }
            } else {
                throw new MException("T??i kho???n theo d??i kh??ng t???n t???i", HttpStatus.NOT_FOUND);
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

    @DeleteMapping("/{id_following}")
    @PreAuthorize("permitAll()")
    public MResponse<?> unfollowAccount(@PathVariable("id_following") Integer idFollowing,
                                    @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (accountService.isAccountExisted(idFollowing)) {
                if (followAccountService.isExist(account.getIdAccount(), idFollowing)) {
                    followAccountService.delete(account.getIdAccount(), idFollowing);
                    return new MResponse<>("B??? theo d??i t??i kho???n th??nh c??ng");
                } else {
                    throw new MException("B???n ch??a theo d??i t??i kho???n n??y", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new MException("T??i kho???n kh??ng t???n t???i", HttpStatus.NOT_FOUND);
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
    public MResponse<?> unfollowAllAccount(@RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            followAccountService.deleteAll(account.getIdAccount());
            return new MResponse<>("B??? theo d??i t???t c??? c??c account th??nh c??ng");
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

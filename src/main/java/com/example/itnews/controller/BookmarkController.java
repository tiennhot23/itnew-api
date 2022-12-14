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
@RequestMapping("/api/v2/bookmark")
@Slf4j(topic = "BookmarkController")
public class BookmarkController {
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

    @PostMapping("/{id_post}")
    @PreAuthorize("permitAll()")
    public MResponse<?> bookmarkPost(@PathVariable("id_post") Integer idPost,
                                 @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (bookmarkService.isExist(account.getIdAccount(), idPost)) {
                throw new MException("B???n ???? bookmark b??i vi???t n??y r???i", HttpStatus.BAD_REQUEST);
            } else {
                Bookmark bookmark = new Bookmark();
                bookmark.setBookmarkId(new BookmarkId(account.getIdAccount(), idPost));
                bookmarkService.save(bookmark);
                return new MResponse<>("Bookmark b??i vi???t th??nh c??ng");
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

    @DeleteMapping("/{id_post}")
    @PreAuthorize("permitAll()")
    public MResponse<?> deletePost(@PathVariable("id_post") Integer idPost,
                                 @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (bookmarkService.isExist(account.getIdAccount(), idPost)) {
                bookmarkService.delete(account.getIdAccount(), idPost);
                return new MResponse<>("Xo?? bookmark th??nh c??ng");
            } else {
                throw new MException("B???n ch??a bookmark b??i vi???t n??y", HttpStatus.BAD_REQUEST);
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

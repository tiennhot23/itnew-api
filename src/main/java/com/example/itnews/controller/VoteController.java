package com.example.itnews.controller;

import com.example.itnews.config.MailSenderService;
import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.entity.*;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.payloads.response.MResponse;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.security.jwt.JwtTokenProvider;
import com.example.itnews.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/vote")
@Slf4j(topic = "VoteController")
public class VoteController {
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

    @PostMapping("/{id_post}/{type}")
    @PreAuthorize("permitAll()")
    public MResponse<?> votePost(@PathVariable("id_post") Integer idPost,
                                     @PathVariable("type") Integer type,
                                     @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            if (type < 0 || type > 1) {
                throw new MException("Loại vote không hợp lệ");
            }
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            Vote vote;
            if (voteService.isExist(account.getIdAccount(), idPost)) {
                vote = voteService.getVote(account.getIdAccount(), idPost);
                vote.setType(type);
            } else {
                vote = new Vote(new VoteId(account.getIdAccount(), idPost), type);
            }
            voteService.save(vote);
            return new MResponse<>("Thêm vote thành công");
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

    @GetMapping("/{id_post}")
    @PreAuthorize("permitAll()")
    public MResponse<?> getPost(@PathVariable("id_post") Integer idPost,
                                 @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            Vote vote = voteService.getVote(account.getIdAccount(), idPost);
            if (vote != null) {
                return new MResponse<>("Lấy loại vote thành công", vote.getType());
            } else {
                return new MResponse<>("Bạn chưa vote bài viết này");
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
    public MResponse<?> removeVotePost(@PathVariable("id_post") Integer idPost,
                                 @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            Vote vote = voteService.getVote(account.getIdAccount(), idPost);
            if (vote != null) {
                voteService.delete(account.getIdAccount(), idPost);
                return new MResponse<>("Xóa vote thành công");
            } else {
                return new MResponse<>("Bạn chưa vote bài viết này");
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

    @PutMapping("/{id_post}/{type}")
    @PreAuthorize("permitAll()")
    public MResponse<?> updateVotePost(@PathVariable("id_post") Integer idPost,
                                       @PathVariable("type") Integer type,
                                       @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            if (type < 0 || type > 1) {
                throw new MException("Loại vote không hợp lệ");
            }
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            Vote vote;
            if (voteService.isExist(account.getIdAccount(), idPost)) {
                vote = voteService.getVote(account.getIdAccount(), idPost);
                vote.setType(type);
                voteService.save(vote);
                return new MResponse<>("Sửa vote thành công");
            } else {
                return new MResponse<>("Bạn chưa vote bài viết này");
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

package com.example.itnews.controller;

import com.example.itnews.config.MailSenderService;
import com.example.itnews.dto.sqlmapping.ICommentDTO;
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

import java.util.List;

@RestController
@RequestMapping("/api/v2/post")
@Slf4j(topic = "CommentController")
public class CommentController {
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

    @PostMapping("/{id_post}/comment")
    @PreAuthorize("permitAll()")
    public MResponse<?> leaveAComment(@PathVariable("id_post") Integer idPost,
                                 @RequestBody Comment body,
                                 @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("T??i kho???n ???? b??? kh??a, kh??ng th??? b??nh lu???n", HttpStatus.FORBIDDEN);
            }
            if (body.getContent() != null && !body.getContent().isEmpty()) {
                String content = body.getContent().trim();
                Comment comment = new Comment();
                comment.setAccount(account);
                comment.setPost(post);
                comment.setContent(content);
                comment.setIdCmtParent(-1);
                comment = commentService.save(comment);
                comment.setIdCmtParent(comment.getIdCmt());
                comment = commentService.save(comment);
                ICommentDTO commentDTO = commentService.selectId(comment.getIdCmt());
                return new MResponse<>("B??nh lu???n th??nh c??ng", commentDTO);
            } else {
                throw new MException("Thi???u d??? li???u", HttpStatus.BAD_REQUEST);
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

    @GetMapping("/{id_post}/comment")
    public MResponse<?> getComments(@PathVariable("id_post") Integer idPost,
                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            List<ICommentDTO> comments = commentService.listCommentInPost(idPost);
            return new MResponse<>("L???y danh s??ch comment th??nh c??ng", comments);
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

    @GetMapping("/{id_post}/comment/main")
    public MResponse<?> getMainComments(@PathVariable("id_post") Integer idPost,
                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            List<ICommentDTO> comments = commentService.listMainCommentInPost(idPost);
            return new MResponse<>("L???y danh s??ch comment th??nh c??ng", comments);
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

    @GetMapping("/{id_post}/comment/reply/{id_cmt_parent}")
    public MResponse<?> leaveAComment(@PathVariable("id_post") Integer idPost,
                                      @PathVariable("id_cmt_parent") Integer idCmtParent,
                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            if (!commentService.isExist(idCmtParent)) {
                throw new MException("Kh??ng t??m th???y b??nh lu???n g???c", HttpStatus.NOT_FOUND);
            }
            List<ICommentDTO> comments = commentService.listReplyInComment(idCmtParent);
            return new MResponse<>("L???y danh s??ch comment th??nh c??ng", comments);
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

    // xem t???t c??? comments k??? c??? comment ???? ???n
    @GetMapping("/{id_post}/comment/all")
    @PreAuthorize("permitAll()")
    public MResponse<?> listAllComments(@PathVariable("id_post") Integer idPost,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            Account author = accountService.getAccountById(post.getAccount().getIdAccount());
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (author.getIdAccount() != account.getIdAccount()) {
                if (account.getRole().getIdRole() < 1 || account.getRole().getIdRole() > 2) {
                    throw new MException("B???n kh??ng c?? quy???n xem", HttpStatus.FORBIDDEN);
                }
            }
            List<ICommentDTO> comments = commentService.listCommentInPost(idPost);
            return new MResponse<>("L???y danh s??ch comment th??nh c??ng", comments);
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

    @PostMapping("/{id_post}/comment/{id_cmt_parent}/reply")
    @PreAuthorize("permitAll()")
    public MResponse<?> replyAComment(@PathVariable("id_post") Integer idPost,
                                      @PathVariable("id_cmt_parent") Integer idCmtParent,
                                      @RequestBody Comment body,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("T??i kho???n ???? b??? kh??a, kh??ng th??? b??nh lu???n", HttpStatus.FORBIDDEN);
            }
            if (!commentService.isExist(idCmtParent)) {
                throw new MException("Kh??ng t??m th???y b??nh lu???n g???c", HttpStatus.NOT_FOUND);
            }
            Comment commentMain = commentService.get(idCmtParent);
            if (!commentMain.getPost().getIdPost().equals(idPost)) {
                throw new MException("B??i ????ng kh??ng c?? b??nh lu???n n??y", HttpStatus.BAD_REQUEST);
            }
            if (body.getContent() != null && !body.getContent().isEmpty()) {
                String content = body.getContent().trim();
                Comment comment = new Comment();
                comment.setAccount(account);
                comment.setPost(post);
                comment.setContent(content);
                comment.setIdCmtParent(idCmtParent);
                comment = commentService.save(comment);
                ICommentDTO commentDTO = commentService.selectId(comment.getIdCmt());
                return new MResponse<>("B??nh lu???n th??nh c??ng", commentDTO);
            } else {
                throw new MException("Thi???u d??? li???u", HttpStatus.BAD_REQUEST);
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

    @PutMapping("/{id_post}/comment/{id_cmt}/update")
    @PreAuthorize("permitAll()")
    public MResponse<?> updateComment(@PathVariable("id_post") Integer idPost,
                                      @PathVariable("id_cmt") Integer idCmt,
                                      @RequestBody Comment body,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("T??i kho???n ???? b??? kh??a, kh??ng th??? b??nh lu???n", HttpStatus.FORBIDDEN);
            }
            if (!commentService.isExist(idCmt)) {
                throw new MException("Kh??ng t??m th???y b??nh lu???n", HttpStatus.NOT_FOUND);
            }
            Comment comment = commentService.get(idCmt);
            if (!comment.getPost().getIdPost().equals(idPost)) {
                throw new MException("B??i ????ng kh??ng c?? b??nh lu???n n??y", HttpStatus.BAD_REQUEST);
            }

            if (!comment.getAccount().getIdAccount().equals(account.getIdAccount())) {
                throw new MException("B???n kh??ng ph???i t??c gi??? comment n??y", HttpStatus.FORBIDDEN);
            }
            if (body.getContent() != null && !body.getContent().isEmpty()) {
                String content = body.getContent().trim();
                comment.setContent(content);
                comment = commentService.save(comment);
                ICommentDTO commentDTO = commentService.selectId(comment.getIdCmt());
                return new MResponse<>("B??nh lu???n th??nh c??ng", commentDTO);
            } else {
                throw new MException("Thi???u d??? li???u", HttpStatus.BAD_REQUEST);
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

    @DeleteMapping("/{id_post}/comment/{id_cmt}/delete")
    @PreAuthorize("permitAll()")
    public MResponse<?> deleteComment(@PathVariable("id_post") Integer idPost,
                                      @PathVariable("id_cmt") Integer idCmt,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("T??i kho???n ???? b??? kh??a, kh??ng th??? b??nh lu???n", HttpStatus.FORBIDDEN);
            }
            if (!commentService.isExist(idCmt)) {
                throw new MException("Kh??ng t??m th???y b??nh lu???n", HttpStatus.NOT_FOUND);
            }
            Comment comment = commentService.get(idCmt);
            if (!comment.getPost().getIdPost().equals(idPost)) {
                throw new MException("B??i ????ng kh??ng c?? b??nh lu???n n??y", HttpStatus.BAD_REQUEST);
            }

            if (!comment.getAccount().getIdAccount().equals(account.getIdAccount()) && account.getRole().getIdRole() != 1) {
                throw new MException("B???n kh??ng c?? quy???n xo?? comment n??y", HttpStatus.FORBIDDEN);
            }
            commentService.delete(idCmt);
            return new MResponse<>("Xo?? b??nh lu???n th??nh c??ng");
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

    @PutMapping("/{id_post}/comment/{id_cmt}/status/{new_status}")
    @PreAuthorize("permitAll()")
    public MResponse<?> deleteComment(@PathVariable("id_post") Integer idPost,
                                      @PathVariable("id_cmt") Integer idCmt,
                                      @PathVariable("new_status") Integer newStatus,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("T??i kho???n ???? b??? kh??a, kh??ng th??? b??nh lu???n", HttpStatus.FORBIDDEN);
            }
            if (!commentService.isExist(idCmt)) {
                throw new MException("Kh??ng t??m th???y b??nh lu???n", HttpStatus.NOT_FOUND);
            }
            Comment comment = commentService.get(idCmt);
            if (!comment.getPost().getIdPost().equals(idPost)) {
                throw new MException("B??i ????ng kh??ng c?? b??nh lu???n n??y", HttpStatus.BAD_REQUEST);
            }

            if (!comment.getAccount().getIdAccount().equals(account.getIdAccount()) && account.getRole().getIdRole() == 3) {
                throw new MException("B???n kh??ng c?? quy???n th???c thi", HttpStatus.FORBIDDEN);
            }
            if (newStatus != 0 &&  newStatus != 1) {
                throw new MException("Tr???ng th??i kh??ng h???p l???", HttpStatus.BAD_REQUEST);
            }
            comment.setStatus(newStatus);
            commentService.save(comment);
            ICommentDTO commentDTO = commentService.selectId(idCmt);
            return new MResponse<>("C???p nh???t tr???ng th??i th??nh c??ng", commentDTO);
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

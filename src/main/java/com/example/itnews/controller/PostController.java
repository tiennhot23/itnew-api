package com.example.itnews.controller;

import com.example.itnews.config.MailSenderService;
import com.example.itnews.dto.AccountDTO;
import com.example.itnews.dto.PostDTO;
import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.*;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.payloads.response.MResponse;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.security.jwt.JwtTokenProvider;
import com.example.itnews.service.*;
import com.google.api.services.drive.model.File;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.PermitAll;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/post")
@Slf4j(topic = "PostController")
public class PostController {

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

    @GetMapping("/drafts")
    @PreAuthorize("permitAll()")
    public MResponse<?> getDraftPost(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            IAccountDTO accountDTO = accountService.getAllInformation(account.getIdAccount());
            List<Integer> postsId = postService.getDraftPosts(account.getIdAccount(), page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectIdForUser(postId, account.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết nháp thành công", list);
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

    @GetMapping("/public")
    @PreAuthorize("permitAll()")
    public MResponse<?> getPublicPost(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            IAccountDTO accountDTO = accountService.getAllInformation(account.getIdAccount());
            List<Integer> postsId = postService.getPublicPosts(account.getIdAccount(), page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectIdForUser(postId, account.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết công khai thành công", list);
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

    @GetMapping("/unlisted")
    @PreAuthorize("permitAll()")
    public MResponse<?> getUnlistedPost(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            IAccountDTO accountDTO = accountService.getAllInformation(account.getIdAccount());
            List<Integer> postsId = postService.getUnlistedPosts(account.getIdAccount(), page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectIdForUser(postId, account.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết chưa cho vào danh sách thành công", list);
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

    @GetMapping("/bookmark")
    @PreAuthorize("permitAll()")
    public MResponse<?> getBookmarkPost(@RequestParam(value = "page", required = false) Integer page,
                                        @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            IAccountDTO accountDTO = accountService.getAllInformation(account.getIdAccount());
            List<IPostDTO> posts = bookmarkService.getListPostBookmark(account.getIdAccount(), page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (IPostDTO p : posts) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectIdForUser(p.getIdPost(), account.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(p.getIdPost());
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết đã bookmark thành công", list);
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

    // bài viết công khai chưa kiểm duyệt
    @GetMapping("/browse")
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    public MResponse<?> getPostsForBrowse(@RequestParam(value = "page", required = false) Integer page,
                                        @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<Integer> postsId = postService.getPostsForBrowse(page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectId(postId);
                IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết công khai nhưng chưa được kiểm duyệt thành công", list);
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
    @GetMapping("/spam")
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    public MResponse<?> getSpamPost(@RequestParam(value = "page", required = false) Integer page,
                                          @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<Integer> postsId = postService.getSpamPost(page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectId(postId);
                IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết spam thành công", list);
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

    @GetMapping("/search")
    public MResponse<?> searchPost(@RequestParam(value = "page", required = false) Integer page,
                                    @RequestParam(value = "k", required = false) String search,
                                    @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            if (search == null || search.trim().length() == 0) {
                throw new MException("Chưa có từ khoá tìm kiếm", HttpStatus.BAD_REQUEST);
            }
            search = '%' + search.toLowerCase() + '%';

            List<Integer> postsId = postService.getSearch(search, page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO;
                if (isValidHeader(header)) {
                    String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                    Account account = accountService.getAccountByUsername(username);
                    postDTO = postService.selectIdForUser(postId, account.getIdAccount());
                } else {
                    postDTO = postService.selectId(postId);
                }
                IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết tìm kiếm thành công", list);
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


    @GetMapping("/newest")
    public MResponse<?> getNewestPostOfUser(@RequestParam(value = "page", required = false) Integer page,
                                   @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<Integer> postsId = postService.getNewestPost(page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO;
                if (isValidHeader(header)) {
                    String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                    Account account = accountService.getAccountByUsername(username);
                    postDTO = postService.selectIdForUser(postId, account.getIdAccount());
                } else {
                    postDTO = postService.selectId(postId);
                }
                IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết công khai mới nhất thành công", list);
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
    @GetMapping("/newest/all")
    public MResponse<?> getNewestPost(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<Integer> postsId = postService.getNewestPost(page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectId(postId);
                IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết công khai mới nhất thành công", list);
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


    @GetMapping("/trending")
    public MResponse<?> getTrendingPost(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<Integer> postsId = postService.getTrendingPost(page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO;
                if (isValidHeader(header)) {
                    String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                    Account account = accountService.getAccountByUsername(username);
                    postDTO = postService.selectIdForUser(postId, account.getIdAccount());
                } else {
                    postDTO = postService.selectId(postId);
                }
                IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết trending thành công", list);
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

    @GetMapping("/following")
    @PreAuthorize("permitAll()")
    public MResponse<?> getPostsFromFollwingTagsAndFollowingAccounts(@RequestParam(value = "page", required = false) Integer page,
                                        @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            List<Integer> postsId = postService.getFollowing(account.getIdAccount(), page == null ? 1 : page);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer postId : postsId) {
                Map<String, Object> map = new HashMap<>();
                IPostDTO postDTO = postService.selectIdForUser(postId, account.getIdAccount());
                IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                List<Tag> tags = postService.selectTagsOfPost(postId);
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", tags);
                list.add(map);
            }
            return new MResponse<>("Lấy danh sách bài viết thành công", list);
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

    @GetMapping("/{id}")
    public MResponse<?> getPostByIdAndIncreaseView(@PathVariable("id") Integer idPost,
                                                   @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            IPostDTO postDTO;
            if (isValidHeader(header)) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                postDTO = postService.selectIdForUser(idPost, account.getIdAccount());
            } else {
                postDTO = postService.selectId(idPost);
            }
            Map<String, Object> map = new HashMap<>();
            IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
            List<Tag> tags = postService.selectTagsOfPost(idPost);
            map.put("post", postDTO);
            map.put("author", accountDTO);
            map.put("tags", tags);

            if (isValidHeader(header)) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account visitor = accountService.getAccountByUsername(username);
                if (visitor.getRole().getIdRole() <= 2 || visitor.getIdAccount().equals(post.getAccount().getIdAccount())) {
                    // Moder trở lên hoặc chính tác giả
                    // Hoặc bài viết là công khai
                    // Không tính view

                    return new MResponse<>("Lấy bài viết thành công", map);
                } else if (post.getStatus() == 1 && post.getAccess() != 0) {
                    post.setView(post.getView() + 1);
                    postService.update(post);
                    return new MResponse<>("Lấy bài viết thành công", map);
                } else {
                    throw new MException("Bạn không có quyền truy cập", HttpStatus.FORBIDDEN);
                }
            } else {
                if (post.getStatus() == 1 && post.getAccess() != 0) {
                    // Những bài viết [đã kiểm duyệt] && [công khai || có link]
                    post.setView(post.getView() + 1);
                    postService.update(post);
                    return new MResponse<>("Lấy bài viết thành công", map);
                } else {
                    throw new MException("Bạn không có quyền truy cập", HttpStatus.FORBIDDEN);
                }
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

    @PostMapping("")
    @PreAuthorize("permitAll()")
    public MResponse<?> addPost(@RequestBody PostDTO body,
                                @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("Tài khoản đã bị khóa, không thể viết bài", HttpStatus.FORBIDDEN);
            }

            if (body.getTitle() != null && !body.getTitle().isEmpty()
                    && body.getContent() != null && !body.getContent().isEmpty()
                    && body.getTags() != null && body.getTags().size() > 0) {
                if (body.getAccess() != null) {
                    int access = body.getAccess();
                    if (access < 0 || access > 2) {
                        throw new MException("Chế độ truy câp không đúng yêu cầu", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    body.setAccess(0);
                }
                if (body.getTags().size() < 1 || body.getTags().size() > 5) {
                    throw new MException("Số lượng thẻ chỉ từ 1 tói 5", HttpStatus.BAD_REQUEST);
                }

                for (Integer idTag : body.getTags()) {
                    if (!tagService.hasId(idTag)) {
                        throw new MException("Thẻ không hợp lệ", HttpStatus.NOT_FOUND);
                    }
                }

                Post post = new Post();
                post.setTitle(body.getTitle());
                post.setContent(body.getContent());
                post.setAccount(account);
                post.setAccess(body.getAccess());
                List<Tag> tags = new ArrayList<>();
                for (Integer idTag : body.getTags()) {
                    Tag tag = tagService.get(idTag);
                    tags.add(tag);
                }
                post.setTags(tags);
                post = postService.save(post);
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("tags", tags);
                return new MResponse<>("Tạo bài viết thành công", map);
            } else {
                throw new MException("Thiếu dữ liệu", HttpStatus.BAD_REQUEST);
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

    @PutMapping("/{id}")
    @PreAuthorize("permitAll()")
    public MResponse<?> updatePost(@PathVariable("id") Integer idPost,
                                   @RequestBody PostDTO body,
                                    @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("Tài khoản đã bị khóa, không thể sửa bài", HttpStatus.FORBIDDEN);
            }

            Post post = postService.getPostById(idPost);
            if (post.getAccount().getIdAccount().equals(account.getIdAccount())) {
                if (body.getTitle() != null && !body.getTitle().isEmpty()
                        && body.getContent() != null && !body.getContent().isEmpty()
                        && body.getTags() != null && body.getTags().size() > 0) {
                    if (body.getAccess() != null) {
                        int access = body.getAccess();
                        if (access < 0 || access > 2) {
                            throw new MException("Chế độ truy câp không đúng yêu cầu", HttpStatus.BAD_REQUEST);
                        }
                    }
                    if (body.getTags().size() < 1 || body.getTags().size() > 5) {
                        throw new MException("Số lượng thẻ chỉ từ 1 tói 5", HttpStatus.BAD_REQUEST);
                    }

                    for (Integer idTag : body.getTags()) {
                        if (!tagService.hasId(idTag)) {
                            throw new MException("Thẻ không hợp lệ", HttpStatus.NOT_FOUND);
                        }
                    }

                    post.setTitle(body.getTitle());
                    post.setContent(body.getContent());
                    post.setAccess(body.getAccess() == null ? post.getAccess() : body.getAccess());
                    List<Tag> tags = new ArrayList<>();
                    for (Integer idTag : body.getTags()) {
                        Tag tag = tagService.get(idTag);
                        tags.add(tag);
                    }
                    post.setTags(tags);
                    post = postService.update(post);
                    return new MResponse<>("Cập nhật bài viết thành công");
                } else {
                    throw new MException("Thiếu dữ liệu", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new MException("Không thể sửa bài viết của người khác", HttpStatus.FORBIDDEN);
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

    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    public MResponse<?> deletePost(@PathVariable("id") Integer idPost,
                                   @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("Tài khoản đã bị khóa, không thể xoá bài", HttpStatus.FORBIDDEN);
            }

            Post post = postService.getPostById(idPost);
            if (post.getAccount().getIdAccount().equals(account.getIdAccount()) || account.getRole().getIdRole() == 0) {
                postService.delete(idPost);
                return new MResponse<>("Xoá bài viết thành công");
            } else {
                throw new MException("Không thể xoá bài viết của người khác", HttpStatus.FORBIDDEN);
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

    @PutMapping("/{id}/status/{status_new}")
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    public MResponse<?> updateStatusPost(@PathVariable("id") Integer idPost,
                                   @PathVariable("status_new") Integer newStatus,
                                   @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            if (newStatus <0 || newStatus > 2) {
                throw new MException("Trạng thái mới không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            Post post = postService.getPostById(idPost);
            post.setStatus(newStatus);
            postService.update(post);

            if (newStatus == 1 && post.getAccess() == 1) {
                IPostDTO postDTO = postService.selectId(idPost);
                IAccountDTO author = accountService.getAllInformation(postDTO.getIdAccount());
                List<FollowAccount> followingAccounts = followAccountService.getListFollowerOfAccount(author.getIdAccount());
                for (FollowAccount followAccount : followingAccounts) {
                    notificationService.addNotification(followAccount.getFollowAccountId().getIdFollowing(),
                            author.getRealName() + " đã đăng một bài viết mới: " + postDTO.getTitle(),
                            "/post/" + idPost);
                }
                notificationService.addNotification(author.getIdAccount(),
                        "Bài viết " + postDTO.getTitle() + " của bạn đã được duyệt.",
                        "/post/" + idPost);
            }

            return new MResponse<>("Cập nhật trạng thái bài viết thành công");
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

    @PutMapping("/{id}/access/{access_new}")
    @PreAuthorize("permitAll()")
    public MResponse<?> updateAccessPost(@PathVariable("id") Integer idPost,
                                         @PathVariable("access_new") Integer newAccess,
                                         @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            if (newAccess <0 || newAccess > 2) {
                throw new MException("Trạng thái truy cập mới không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            Post post = postService.getPostById(idPost);
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (!post.getAccount().getIdAccount().equals(account.getIdAccount())) {
                throw new MException("Không thể thay đổi access bài viết của người khác", HttpStatus.FORBIDDEN);
            }
            post.setAccess(newAccess);
            postService.update(post);
            return new MResponse<>("Cập nhật trạng thái truy cập bài viết thành công");
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
    @GetMapping("/{id}/voteup")
    public MResponse<?> getVoteUp(@PathVariable("id") Integer idPost,
                                         @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            List<Vote> votes = voteService.getUpVotes(idPost);
            Map<String, Object> map = new HashMap<>();
            map.put("total", votes.size());
            map.put("vote", votes);
            return new MResponse<>("Lấy danh sách vote up thành công", map);
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
    @GetMapping("/{id}/votedown")
    public MResponse<?> getVoteDown(@PathVariable("id") Integer idPost,
                                  @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            List<Vote> votes = voteService.getDownVotes(idPost);
            Map<String, Object> map = new HashMap<>();
            map.put("total", votes.size());
            map.put("vote", votes);
            return new MResponse<>("Lấy danh sách vote up thành công", map);
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
    @GetMapping("/{id}/vote")
    public MResponse<?> getVote(@PathVariable("id") Integer idPost,
                                    @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Post post = postService.getPostById(idPost);
            List<Vote> votesUp = voteService.getUpVotes(idPost);
            List<Vote> votesDown = voteService.getDownVotes(idPost);
            return new MResponse<>("Lấy điểm vote thành công", votesUp.size() - votesDown.size());
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

    private boolean isValidHeader(String header) {
        return header != null && !header.equals("") && header.length() > 7;
    }
}

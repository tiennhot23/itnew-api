package com.example.itnews.controller;

import com.example.itnews.config.MailSenderService;
import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.Account;
import com.example.itnews.entity.Role;
import com.example.itnews.entity.Tag;
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

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v2/tag")
@Slf4j(topic = "TagController")
public class TagController {

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


    @GetMapping("/all")
    public MResponse<?> getAllTags(@RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<ITagDTO> tags = new ArrayList<>();
            if (isValidHeader(header)) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                tags = tagService.selectAllByAccount(account.getIdAccount());
            } else {
                tags = tagService.selectAll();
            }
            return new MResponse<>("Lấy danh sách thẻ thành công", tags);
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
    @GetMapping("/{id_account}/all")
    public MResponse<?> getAllTags(@PathVariable("id_account") Integer idAccount,
            @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<ITagDTO> tags = new ArrayList<>();
            if (isValidHeader(header)) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                tags = tagService.selectAllByAccount(account.getIdAccount());
            } else {
                tags = tagService.selectAll();
            }
            return new MResponse<>("Lấy danh sách thẻ thành công", tags);
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
    public MResponse<?> searchTags(@RequestParam(value = "k", required = false) String search,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            if (search != null && !search.isEmpty()) {
                search = search.toLowerCase();
            } else {
                throw new MException("Chưa có từ khoá tìm kiếm", HttpStatus.BAD_REQUEST);
            }
            if (page == null) {
                page = 1;
            }
            List<ITagDTO> tagIds = tagService.getSearch(search, page);
            List<ITagDTO> tags = new ArrayList<>();
            for (ITagDTO idTag : tagIds) {
                if (isValidHeader(header)) {
                    String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                    Account account = accountService.getAccountByUsername(username);
                    ITagDTO tagDTO = tagService.selectIdByAccount(idTag.getIdTag(), account.getIdAccount());
                    tags.add(tagDTO);
                } else {
                    ITagDTO tagDTO = tagService.selectId(idTag.getIdTag());
                    tags.add(tagDTO);
                }
            }
            return new MResponse<>("Lấy danh sách thẻ thành công", tags);
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
    public MResponse<?> getTagById(@PathVariable("id") Integer idTag,
                                   @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            ITagDTO tagDTO;
            if (isValidHeader(header)) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                tagDTO = tagService.selectIdByAccount(idTag, account.getIdAccount());
            } else {
                tagDTO = tagService.selectId(idTag);
            }
            return new MResponse<>("Lấy thẻ thành công", tagDTO);
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
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    public MResponse<?> addTag(@RequestParam(value = "logo", required = false) MultipartFile logo,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            if (logo == null) {
                throw new MException("Không có logo nào được tải lên", HttpStatus.BAD_REQUEST);
            }
            if (name != null && !name.isEmpty()) {
                if (tagService.hasName(name)) {
                    throw new MException("Tên thẻ đã bị trùng", HttpStatus.BAD_REQUEST);
                }
                Tag tag = tagService.add(new Tag(null, name, "tempLogoUrl"));
                URL url = Resources.getResource("photo.jpg"); // target/classes/com/photo.jpg
                java.io.File fileTemp = Paths.get(url.toURI()).toFile();
                logo.transferTo(fileTemp);
                File file = driveService.uploadFile("TAG::" + tag.getIdTag(), fileTemp.getAbsolutePath(), logo.getContentType());
                String fileURL = "https://drive.google.com/uc?id=" + file.getId();
                tag.setLogo(fileURL);
                tagService.update(tag);
                return new MResponse<>("Tạo mới thẻ thành công", tag);
            } else {
                throw new MException("Thiếu tên thẻ", HttpStatus.BAD_REQUEST);
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
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    public MResponse<?> updateTag(@PathVariable("id") Integer idTag,
                                  @RequestParam(value = "logo", required = false) MultipartFile logo,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            if (!tagService.hasId(idTag)) {
                throw new MException("Không tìm thấy thẻ", HttpStatus.NOT_FOUND);
            }
            if (name == null || name.equals("")) {
                throw  new MException("Thiếu tên thẻ", HttpStatus.BAD_REQUEST);
            }
            Tag tag = tagService.get(idTag);
            if (logo != null) {
                String fileOldId = tag.getLogo().split("id=")[1];
                URL url = Resources.getResource("photo.jpg"); // target/classes/com/photo.jpg
                java.io.File fileTemp = Paths.get(url.toURI()).toFile();
                logo.transferTo(fileTemp);
                File file = driveService.uploadFile("TAG::" + tag.getIdTag(), fileTemp.getAbsolutePath(), logo.getContentType());
                String fileURL = "https://drive.google.com/uc?id=" + file.getId();
                tag.setLogo(fileURL);
                driveService.removeById(fileOldId);
            }
            tag.setName(name);
            tagService.update(tag);
            return new MResponse<>("Cập nhật thẻ thành công", tag);
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
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    public MResponse<?> deleteTag(@PathVariable("id") Integer idTag,
                                  @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            if (tagService.hasId(idTag)) {
                Integer countPostsOfTag = tagService.countPostsOfTag(idTag);
                if (countPostsOfTag > 0) {
                    throw new MException("Thẻ đã có bài viết, không thể xoá", HttpStatus.FORBIDDEN);
                } else {
                    Tag tag = tagService.get(idTag);
                    String fileOldId = tag.getLogo().split("id=")[1];
                    driveService.removeById(fileOldId);
                    tagService.delete(idTag);
                    return new MResponse<>("Xoá thẻ thành công");
                }
            } else {
                throw new MException("Không tìm thấy thẻ để xoá", HttpStatus.NOT_FOUND);
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

    @GetMapping("/{id}/posts")
    public MResponse<?> getPostsOfTag(@PathVariable("id") Integer idTag,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            if (tagService.hasId(idTag)) {
                List<Integer> postIds = postService.getListPostIdOfTag(idTag, page);
                List<Map<String, Object>> data = new ArrayList<>();
                for (Integer postId : postIds) {
                    IPostDTO postDTO = postService.selectId(postId);
                    IAccountDTO accountDTO = accountService.getAllInformation(postDTO.getIdAccount());
                    List<Tag> tagDTO = postService.selectTagsOfPost(postDTO.getIdPost());

                    Map<String, Object> map = new HashMap<>();
                    map.put("post", postDTO);
                    map.put("author", accountDTO);
                    map.put("tags", tagDTO);
                    data.add(map);
                }
                return new MResponse<>("Lấy danh sách bài viết thành công", data);
            } else {
                throw new MException("Không tìm thấy thẻ để xoá", HttpStatus.NOT_FOUND);
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

    private boolean isValidHeader(String header) {
        return header != null && !header.equals("") && header.length() > 7;
    }
}

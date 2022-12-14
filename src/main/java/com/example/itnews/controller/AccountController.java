package com.example.itnews.controller;

import com.example.itnews.config.AppConstants;
import com.example.itnews.config.ISubject;
import com.example.itnews.config.MailSenderService;
import com.example.itnews.dto.*;
import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.example.itnews.dto.sqlmapping.ILockAccountDTO;
import com.example.itnews.dto.sqlmapping.IPostDTO;
import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.*;
import com.example.itnews.dto.AccountDTO;
import com.example.itnews.dto.CreateAccountDTO;
import com.example.itnews.payloads.request.LoginRequest;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.payloads.response.MResponse;
import com.example.itnews.security.SecureUserDetails;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/account")
@Slf4j(topic = "AccountController")
public class AccountController {

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

    @PostMapping("/login")
    public MResponse<String> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult)
            throws Exception {
        if (bindingResult.hasErrors() || loginRequest == null) {
            throw new MException("Thi???u th??ng tin ????ng nh???p", HttpStatus.BAD_REQUEST);
        }
        // X??c th???c t??? username v?? password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // N???u kh??ng x???y ra exception t???c l?? th??ng tin h???p l???
        // Set th??ng tin authentication v??o Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tr??? v??? jwt cho ng?????i d??ng.
        String jwt = tokenProvider.generateToken((SecureUserDetails) authentication.getPrincipal());
        return new MResponse<>("????ng nh???p th??nh c??ng", jwt, null);

//        try {
//            if (bindingResult.hasErrors()) {
//                throw new MException("Thi???u th??ng tin ????ng nh???p", HttpStatus.BAD_REQUEST);
//            }
//            Account acc = accountService.getAccountByUsername(loginRequest.getUsername());
//            if (acc != null) {
//                if (acc.getStatus() == 1) {
//                    if (lockAccountService.isValidLockAccount(acc.getIdAccount())) {
//                        throw new MException("T??i kho???n ??ang b??? kho??", HttpStatus.BAD_REQUEST);
//                    } else {
//                        acc.setStatus(0);
//                        accountService.updateAccount(acc);
//                    }
//                }
//                boolean match = BCrypt.checkpw(loginRequest.getPassword(), acc.getPassword());
//                if (match) {
//                    return new MResponse<>("????ng nh???p th??nh c??ng", "accesstoken", null);
//                }
//            }
//            throw new MException("M???t kh???u ho???c t??i kho???n kh??ng ch??nh x??c", HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            throw new MException(e.getLocalizedMessage());
//        }
    }

    @PostMapping("/{id}/ban")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<String> banAccount(@PathVariable Integer id,
                                        @Valid @RequestBody LockAccountDTO lockAccountDTO,
                                        @RequestHeader(name = "Authorization") String header) throws MException {
        try {
            String usernameBoss = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            if (accountService.getAccountById(id).getAccountName().equals("")) {
                throw new MException("Kh??ng t??m th???y t??i kho???n b??? kh??a", HttpStatus.NOT_FOUND);
            }
            Account accLock = accountService.getAccountById(id);
            Account accBoss = accountService.getAccountByUsername(usernameBoss);


            if (accBoss.getStatus() != 0 || accBoss.getRole().getIdRole() >= accLock.getRole().getIdRole()) {
                throw new MException("T??i kho???n kh??ng c?? quy???n th???c hi???n", HttpStatus.FORBIDDEN);
            }
            if (lockAccountDTO.getHoursLock() < 1 || lockAccountDTO.getHoursLock() > 576) {
                throw new MException("Th???i gian kh??a ch??? ???????c nh??? h??n 576 gi???", HttpStatus.BAD_REQUEST);
            }
            if (accLock.getStatus() != 0) {
                throw new MException("T??i kho???n n??y ???? b??? kh??a", HttpStatus.FORBIDDEN);
            }

            LockAccount lockAccount = lockAccountService.lock(accLock.getIdAccount(), accBoss.getIdAccount(), lockAccountDTO);
            Notification notification = notificationService
                    .addNotification(accLock.getIdAccount(),
                            "T??i kho???n c???a b???n ???? b??? kh??a " + lockAccountDTO.getHoursLock() + "gi???",
                            "/account/" + accLock.getIdAccount());
            mailSenderService.sendSimpleMessage(accLock.getEmail(), ISubject.LOCK_ACCOUNT,
                    "T??i kho???n c???a b???n ???? b??? kh??a " + lockAccountDTO.getHoursLock() + " gi???. \nL?? do: " + lockAccountDTO.getReason());
            accLock.setStatus(1);
            accLock = accountService.updateAccount(accLock);
            log.error("usernameBoss: {}", usernameBoss);
            log.error("accLock: {}", accLock.getAccountName());
            log.error("accBoss: {}", accBoss.getAccountName());
            log.error("body: {}", lockAccountDTO.getHoursLock());
            log.error("lockAccount: {}", lockAccount.toString());
            log.error("notification: {}", notification.toString());
            log.error("accLockUpdated: {}", accLock.toString());
            return new MResponse<>("Ch???n t??i kho???n th??nh c??ng");
        } catch (Exception e) {
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PostMapping("/forgot/password")
    public MResponse<?> forgotPassword(@Valid @RequestBody Account account) throws MException {
        try {
            String username = account.getAccountName();
            if (username.equals(""))
                throw new MException("Thi???u d??? li???u", HttpStatus.BAD_REQUEST);
            account = accountService.getAccountByUsername(username);
            String code = createCode();
            String hashCode = passwordEncoder.encode(code);
            mailSenderService.sendSimpleMessage(account.getEmail(), ISubject.CHANGE_PASSWORD, code);
            Verification verification = verificationService.addVerification(account.getIdAccount(), hashCode);

            log.error("code: {}", code);
            log.error("hashCode: {}", hashCode);
            log.error("verification: {}", verification.toString());
            Map<String, Integer> map = new HashMap<>();
            map.put("id_account", account.getIdAccount());
            return new MResponse<>("???? g???i m?? x??c nh???n", map);
        } catch (Exception e) {
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PostMapping("/{idAccount}/confirm")
    public MResponse<Account> resetPassword(@RequestBody Verification v,
                                            @PathVariable Integer idAccount) throws MException {
        try {
            log.error("code: {}", v.getCode());
            log.error("idAccount: {}", idAccount);
            Verification verification = verificationService.checkValidVerification(idAccount);
            if (passwordEncoder.matches(v.getCode(), verification.getCode())) {
                verificationService.deleteVerification(verification.getIdVerification());
                String hashPass = passwordEncoder.encode("123456");
                Account account = accountService.getAccountById(idAccount);
                account.setPassword(hashPass);
                accountService.updateAccount(account);
                String token = jwtTokenProvider.generateToken(SecureUserDetails.build(account));
                return new MResponse<>("????ng nh???p th??nh c??ng, m???t kh???u reset 123456", token, account);
            } else {
                throw new MException("M???t m?? kh??ng ????ng", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PutMapping("/{idAccount}/change_password")
    @PreAuthorize("permitAll()")
    public MResponse<String> changePassword(@PathVariable("idAccount") Integer idAccount,
                                            @Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                            @RequestHeader(name = "Authorization") String header) throws MException {
        try {
            String newPassword = changePasswordDTO.getNewPassword();
            String oldPassword = changePasswordDTO.getOldPassword();
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (passwordEncoder.matches(oldPassword, account.getPassword())) {
                String hashPass = passwordEncoder.encode(newPassword);
                account.setPassword(hashPass);
                accountService.updateAccount(account);
                return new MResponse<>("Thay ?????i m???t kh???u th??nh c??ng");
            } else {
                throw new MException("M???t kh???u c?? kh??ng ch??nh x??c!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PostMapping("/")
    public MResponse<Integer> createAccount(@Valid @RequestBody CreateAccountDTO accountDTO) throws MException {
        try {
            Account account = new Account();
            account.setAccountName(accountDTO.getAccountName());
            account.setRealName(accountDTO.getRealName());
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());

            Integer idRole = 3;
            String hashPassword = passwordEncoder.encode(account.getPassword());
            if (accountService.isAccountExisted(account)) {
                throw new MException("T??n t??i kho???n ho???c email ???? t???n t???i!", HttpStatus.BAD_REQUEST);
            }
            account.setPassword(hashPassword);
            account.setRole(new Role(idRole, "User"));
            account.setAvatar(AppConstants.avatarDefault);
            account = accountService.saveAccount(account);
            return new MResponse<Integer>("T???o m???i t??i kho???n th??nh c??ng");
        } catch (Exception e) {
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PutMapping("/{idAccount}")
    @PreAuthorize("permitAll()")
    public MResponse<Account> updateAccount(@PathVariable Integer idAccount,
                                            @RequestBody AccountDTO accountDTO,
                                            @RequestHeader(name = "Authorization") String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (!account.getIdAccount().equals(idAccount)) {
                throw new MException("Kh??ng th??? s???a ?????i th??ng tin c???a ng?????i kh??c", HttpStatus.UNAUTHORIZED);
            }
            if (accountDTO.getRealName() == null || accountDTO.getRealName().equals("")) {
                throw new MException("real_name l?? b???t bu???c", HttpStatus.BAD_REQUEST);
            }
            if (accountDTO.getBirth() != null && !accountDTO.getBirth().equals("")) {
                String date = validDate(accountDTO.getBirth());
                if (date.equals("")) {
                    throw new MException("Ng??y sinh kh??ng h???p l???", HttpStatus.BAD_REQUEST);
                } else {
                    account.setBirth(new Date(date));
                }
            }
            account.setRealName(accountDTO.getRealName() == null ? account.getRealName() : accountDTO.getRealName());
            account.setGender(accountDTO.getGender() == null ? account.getGender() : accountDTO.getGender());
            account.setCompany(accountDTO.getCompany() == null ? account.getCompany() : accountDTO.getCompany());
            account.setPhone(accountDTO.getPhone() == null ? account.getPhone() : accountDTO.getPhone());
            account.setAvatar(accountDTO.getAvatar() == null ? account.getAvatar() : accountDTO.getAvatar());
            log.error("account {} ", account.toString());
            account = accountService.updateAccount(account);
            return new MResponse<>("C???p nh???t th??ng tin t??i kho???n th??nh c??ng", account);
        } catch (Exception e) {
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/information")
    @PreAuthorize("permitAll()")
    public MResponse<IAccountDTO> getInformation(@RequestHeader(name = "Authorization") String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            IAccountDTO accountDTO = accountService.getAllInformation(account.getIdAccount());
            return new MResponse<>("L???y th??ng tin th??nh c??ng", accountDTO);
        } catch (Exception e) {
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/all")
    public MResponse<List<IAccountDTO>> getAllAccount(@RequestHeader(name = "Authorization") String header) throws MException {
        try {
            List<IAccountDTO> list = new ArrayList<>();
            List<Integer> ids = accountService.getListIdAccount();
            if (header != null && !header.equals("")) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                for (Integer id : ids) {
                    IAccountDTO iAccountDTO = accountService.getAllInformationWithStatus(id, account.getIdAccount());
                    list.add(iAccountDTO);
                }
            } else {
                for (Integer id : ids) {
                    IAccountDTO accountDTO = accountService.getAllInformation(id);
                    list.add(accountDTO);
                }
            }
            return new MResponse<>("L???y danh s??ch t??i kho???n th??nh c??ng", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @GetMapping("/search")
    public MResponse<List<IAccountDTO>> searchAccount(@RequestParam(required = false, value = "k") String k,
                                                      @RequestParam(required = false, value = "page") Integer page,
                                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            if (k == null || k.trim().length() == 0) {
                throw new MException("Ch??a c?? t??? kh??a t??m ki???m", HttpStatus.BAD_REQUEST);
            }

            k = k.toLowerCase();

            List<IAccountDTO> list = new ArrayList<>();
            List<Integer> ids = accountService.getSearch(k, page == null ? 1 : page);
            if (header != null && !header.equals("")) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                for (Integer id : ids) {
                    IAccountDTO iAccountDTO = accountService.getAllInformationWithStatus(id, account.getIdAccount());
                    list.add(iAccountDTO);
                }
            } else {
                for (Integer id : ids) {
                    IAccountDTO accountDTO = accountService.getAllInformation(id);
                    list.add(accountDTO);
                }
            }
            return new MResponse<>("T??m ki???m danh s??ch t??i kho???n th??nh c??ng", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}")
    public MResponse<IAccountDTO> getAccountById(@PathVariable("id") Integer id,
                                                 @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            accountService.getAccountById(id);
            IAccountDTO accountDTO;
            if (header != null && !header.equals("")) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account user = accountService.getAccountByUsername(username);
                accountDTO = accountService.getAllInformationWithStatus(id, user.getIdAccount());
            } else {
                accountDTO = accountService.getAllInformation(id);
            }
            if (accountDTO == null) throw new MException("T??i kho???n kh??ng t???n t???i", HttpStatus.NOT_FOUND);
            return new MResponse<>("???? t??m th???y t??i kho???n", accountDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}/status/{id_user}")
    public MResponse<IAccountDTO> getAccountStatusById(@PathVariable("id") Integer id,
                                                       @PathVariable("id_user") Integer idUser,
                                                 @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            if (accountService.isAccountExisted(id)) {
                IAccountDTO accountDTO = accountService.getAllInformationWithStatus(id, idUser);
                return new MResponse<>("???? t??m th???y t??i kho???n", accountDTO);
            } else {
                throw new MException("???? t??m th???y t??i kho???n", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/account_locked/all")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<List<ILockAccountDTO>> getAllLockedAccount(@RequestHeader(name = "Authorization") String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (account.getStatus() != 0) {
                throw new MException("T??i kho???n ??ang b??? kh??a", HttpStatus.FORBIDDEN);
            } else {
                List<ILockAccountDTO> list = lockAccountService.selectAll();
                return new MResponse<>("Thao t??c th???c hi???n", list);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @GetMapping("/{id}/role")
    public MResponse<Role> getAccountRoleById(@PathVariable("id") Integer id,
                                              @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Account account = accountService.getAccountById(id);
            return new MResponse<>("OK", account.getRole());
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}/posts")
    public MResponse<List<?>> getAccountPostsById(@PathVariable("id") Integer id,
                                                  @RequestParam(required = false, value = "page") Integer page,
                                                  @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            IAccountDTO accountDTO = accountService.getAllInformation(id);
            List<Integer> postsId = postService.getListPostIdOfAccount(accountDTO.getIdAccount(), page == null ? 1 : page);
            List<Map<String, Object>> data = new ArrayList<>();
            for (Integer idPost : postsId) {
                IPostDTO postDTO = postService.selectId(idPost);
                Post post = postService.getPostById(idPost);
                Map<String, Object> map = new HashMap<>();
                map.put("post", postDTO);
                map.put("author", accountDTO);
                map.put("tags", post.getTags());
                data.add(map);
            }

            return new MResponse<>("L???y danh s??ch c???c b??i vi???t c???a t??i kho???n th??nh c??ng", data);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}/bookmarks")
    public MResponse<List<?>> getAccountBookmarksById(@PathVariable("id") Integer id,
                                                      @RequestParam(required = false, value = "page") Integer page,
                                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            IAccountDTO accountDTO = accountService.getAllInformation(id);
            List<Integer> postsId = bookmarkService.getListPostBookmark(accountDTO.getIdAccount(), page == null ? 1 : page)
                    .stream().map(IPostDTO::getIdPost).collect(Collectors.toList());
            List<Map<String, Object>> data = new ArrayList<>();
            for (Integer idPost : postsId) {
                IPostDTO postDTO = postService.selectId(idPost);
                Post post = postService.getPostById(idPost);
                IAccountDTO authorDTO = accountService.getAllInformation(post.getAccount().getIdAccount());
                Map<String, Object> map = new HashMap<>();
                map.put("post", postDTO);
                map.put("author", authorDTO);
                map.put("tags", post.getTags());
                data.add(map);
            }

            return new MResponse<>("L???y danh s??ch b??i vi???t bookmark th??nh c??ng", data);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}/follow_tag")
    public MResponse<List<?>> getAccountFollowTagById(@PathVariable("id") Integer id,
                                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<ITagDTO> listTag = new ArrayList<>();
            if (header != null && !header.equals("")) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                Account followTagAccount = accountService.getAccountById(id);
                listTag = followTagService.getListFollowTag(followTagAccount.getIdAccount(), account.getIdAccount());
            } else {
                Account followTagAccount = accountService.getAccountById(id);
                listTag = followTagService.getListFollowTag(followTagAccount.getIdAccount());
            }
            return new MResponse<>("L???y danh s??ch c??c th??? m?? t??i kho???n theo d??i th??nh c??ng", listTag);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}/follower")
    public MResponse<List<?>> getAccountFollowerById(@PathVariable("id") Integer id,
                                                     @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<IAccountDTO> list = new ArrayList<>();
            List<FollowAccount> followAccountList = followAccountService.getListFollowerOfAccount(id);
            if (header != null && !header.equals("")) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                for (FollowAccount followAccount : followAccountList) {
                    IAccountDTO accountDTO = accountService.getAllInformationWithStatus(followAccount.getFollowAccountId().getIdFollowing(), account.getIdAccount());
                    list.add(accountDTO);
                }
            } else {
                for (FollowAccount followAccount : followAccountList) {
                    IAccountDTO accountDTO = accountService.getAllInformation(followAccount.getFollowAccountId().getIdFollowing());
                    list.add(accountDTO);
                }
            }
            return new MResponse<>("L???y danh s??ch c??c t??i kho???n theo d??i ng?????i n??y th??nh c??ng", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}/following")
    public MResponse<List<?>> getAccountFollowingById(@PathVariable("id") Integer id,
                                                     @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            List<IAccountDTO> list = new ArrayList<>();
            List<FollowAccount> followAccountList = followAccountService.getListFollowingOfAccount(id);
            if (header != null && !header.equals("")) {
                String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
                Account account = accountService.getAccountByUsername(username);
                for (FollowAccount followAccount : followAccountList) {
                    IAccountDTO accountDTO = accountService.getAllInformationWithStatus(followAccount.getFollowAccountId().getIdFollower(), account.getIdAccount());
                    list.add(accountDTO);
                }
            } else {
                for (FollowAccount followAccount : followAccountList) {
                    IAccountDTO accountDTO = accountService.getAllInformation(followAccount.getFollowAccountId().getIdFollower());
                    list.add(accountDTO);
                }
            }
            return new MResponse<>("L???y danh s??ch c??c t??i kho???n ng?????i n??y ??ang theo d??i th??nh c??ng", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @GetMapping("/{id}/mark")
    public MResponse<?> getAccountVoteMarkById(@PathVariable("id") Integer id,
                                                      @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Account account = accountService.getAccountById(id);
            Integer totalVoteUp = voteService.getTotalVoteUp(account.getIdAccount());
            Integer totalVoteDown = voteService.getTotalVoteDown(account.getIdAccount());
            Integer mark = totalVoteUp - totalVoteDown;
            Map<String , Object> result = new HashMap<>();
            result.put("mark", mark);
            result.put("up", totalVoteUp);
            result.put("down", totalVoteDown);
            return new MResponse<>("L???y t???ng ??i???m th??nh c??ng", result);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @GetMapping("/{id}/view")
    public MResponse<?> getAccountViewById(@PathVariable("id") Integer id,
                                               @RequestHeader(name = "Authorization", required = false) String header) throws MException {
        try {
            Account account = accountService.getAccountById(id);
            Integer totalView = postService.getTotalViewOfAccountPosts(account.getIdAccount());
            return new MResponse<>("L???y t???ng s??? l?????t xem th??nh c??ng", totalView);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PutMapping("/{id}/role/{id_role_new}")
    @PreAuthorize("hasAnyRole('Moder', 'Admin')")
    public MResponse<?> changeRoleAccount(@PathVariable("id") Integer id, @PathVariable("id_role_new") Integer idRoleNew,
                                           @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String usernameBoss = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account accountBoss = accountService.getAccountByUsername(usernameBoss);
            Account accountUser = accountService.getAccountById(id);

            if (accountBoss.getRole().getIdRole() >= accountUser.getRole().getIdRole() ||
                idRoleNew <= accountBoss.getRole().getIdRole()) {
                throw new MException("B???n kh??ng c?? quy???n", HttpStatus.FORBIDDEN);
            } else {
                if (roleService.isValidRole(idRoleNew)) {
                    accountUser.setRole(roleService.getRole(idRoleNew));
                    accountUser = accountService.updateAccount(accountUser);
                    return new MResponse<>("C???p nh???t ch???c v??? th??ng c??ng", accountUser);
                } else {
                    throw new MException("Ch???c v??? kh??ng h???p l???", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }


    @Deprecated
    @PutMapping("/change/information")
    @PreAuthorize("permitAll()")
    public MResponse<?> changeSelfInformation(@RequestBody AccountDTO accountDTO,
                                              @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (accountDTO.getRealName() == null || accountDTO.getRealName().equals("")) {
                throw new MException("real_name l?? b???t bu???c", HttpStatus.BAD_REQUEST);
            }
            if (accountDTO.getBirth() != null && !accountDTO.getBirth().equals("")) {
                String date = validDate(accountDTO.getBirth());
                if (date.equals("")) {
                    throw new MException("Ng??y sinh kh??ng h???p l???", HttpStatus.BAD_REQUEST);
                } else {
                    account.setBirth(new Date(date));
                }
            }
            account.setRealName(accountDTO.getRealName() == null ? account.getRealName() : accountDTO.getRealName());
            account.setGender(accountDTO.getGender() == null ? account.getGender() : accountDTO.getGender());
            account.setCompany(accountDTO.getCompany() == null ? account.getCompany() : accountDTO.getCompany());
            account.setPhone(accountDTO.getPhone() == null ? account.getPhone() : accountDTO.getPhone());
            account.setAvatar(accountDTO.getAvatar() == null ? account.getAvatar() : accountDTO.getAvatar());
            log.error("account {} ", account.toString());
            account = accountService.updateAccount(account);
            return new MResponse<>("C???p nh???t th??ng tin t??i kho???n th??nh c??ng", account);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PutMapping("/update/information")
    @PreAuthorize("permitAll()")
    public MResponse<?> changeSelfInformation(@RequestParam(value = "avatar", required = false) MultipartFile image,
                                              @RequestParam(value = "real_name", required = false) String realName,
                                              @RequestParam(value = "birth", required = false) String birth,
                                              @RequestParam(value = "gender", required = false) Integer gender,
                                              @RequestParam(value = "company", required = false) String company,
                                              @RequestParam(value = "phone", required = false) String phone,
                                              @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (birth != null && !birth.equals("")) {
                String date = validDate(birth);
                if (date.equals("")) {
                    throw new MException("Ng??y sinh kh??ng h???p l???", HttpStatus.BAD_REQUEST);
                } else {
                    account.setBirth(new Date(date));
                }
            }
            if (image != null) {
                String[] arr = account.getAvatar().split("id=");
                if (arr.length > 1) {
                    String fileOldId = arr[1];
                    driveService.removeById(fileOldId);
                }
                URL url = Resources.getResource("photo.jpg"); // target/classes/com/photo.jpg
                java.io.File fileTemp = Paths.get(url.toURI()).toFile();
                image.transferTo(fileTemp);
                File file = driveService.uploadFile(String.valueOf(account.getIdAccount()), fileTemp.getAbsolutePath(), image.getContentType());
                account.setAvatar("https://drive.google.com/uc?id=" + file.getId());
            }
            account.setRealName(realName == null ? account.getRealName() : realName);
            account.setGender(gender == null ? account.getGender() : gender);
            account.setCompany(company == null ? account.getCompany() : company);
            account.setPhone(phone == null ? account.getPhone() : phone);
            log.error("account {} ", account.toString());
            account = accountService.updateAccount(account);
            return new MResponse<>("C???p nh???t th??ng tin t??i kho???n th??nh c??ng", account);
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

    @PatchMapping("/{id}/unlock")
    @PreAuthorize("hasAnyRole('Admin')")
    public MResponse<?> unlockAccount(@PathVariable("id") Integer idAccountLock,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account accountBoss = accountService.getAccountByUsername(username);
            Account accountLock = accountService.getAccountById(idAccountLock);

            if (accountBoss.getStatus() != 0 ||
                    accountBoss.getRole().getIdRole() >= accountLock.getRole().getIdRole()) {
                throw new MException("T??i kho???n kh??ng c?? quy???n th???c hi???n", HttpStatus.FORBIDDEN);
            } else if (accountLock.getStatus() == 2) {
                throw new MException("T??i kho???n ???? b??? kho?? v?? th???i h???n", HttpStatus.FORBIDDEN);
            } else {
                accountLock.setStatus(0);
                lockAccountService.unlock(idAccountLock);
                accountService.updateAccount(accountLock);
                notificationService.addNotification(idAccountLock, "T??i kho???n c???a b???n ???? ???????c m??? kh??a", "/account/" + idAccountLock);
                mailSenderService.sendSimpleMessage(accountLock.getEmail(), ISubject.UNLOCK_ACCOUNT,
                        "T??i kho???n c???a b???n ???? ???????c m??? kh??a");
                return new MResponse<>("M??? kh??a t??i kho???n th??nh c??ng");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @PatchMapping("/{id}/die")
    @PreAuthorize("hasAnyRole('Admin')")
    public MResponse<?> lockAccount(@PathVariable("id") Integer idAccountLock,
                                      @RequestBody LockAccountDTO lockAccountDTO,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account accountBoss = accountService.getAccountByUsername(username);
            Account accountLock = accountService.getAccountById(idAccountLock);

            String reason = lockAccountDTO.getReason();
            if (reason != null && !reason.equals("")) {
                lockAccountDTO.setHoursLock(0);
                lockAccountService.lock(idAccountLock, accountBoss.getIdAccount(), lockAccountDTO);
                notificationService.addNotification(idAccountLock, "T??i kho???n c???a b???n ???? b??? kh??a v?? th???i h???n", "/account/" + idAccountLock);
                accountLock.setStatus(2);
                accountService.updateAccount(accountLock);
                mailSenderService.sendSimpleMessage(accountLock.getEmail(), ISubject.LOCK_ACCOUNT,
                        "T??i kho???n c???a b???n ???? b??? kh??a v?? th???i h???n. \nL?? do: " + lockAccountDTO.getReason());
                return new MResponse<>("Kh??a v??nh vi???n t??i kho???n th??nh c??ng");
            } else {
                throw new MException("Thi???u d??? li???u", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }


    @PatchMapping("/{id}/revive")
    @PreAuthorize("hasRole('Admin')")
    public MResponse<?> unlockDieAccount(@PathVariable("id") Integer idAccountLock,
                                      @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            Account accountLock = accountService.getAccountById(idAccountLock);
            accountLock.setStatus(0);
            lockAccountService.unlock(idAccountLock);
            notificationService.addNotification(idAccountLock, "T??i kho???n c???a b???n ???? ???????c m??? kho??", "/account/" + idAccountLock);
            mailSenderService.sendSimpleMessage(accountLock.getEmail(), ISubject.UNLOCK_ACCOUNT,
                    "T??i kho???n c???a b???n ???? ???????c m??? kho??");
            return new MResponse<>("M??? kho?? t??i kho???n th??nh c??ng");
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof MRuntimeException) {
                throw new MException(((MRuntimeException) e).getMessage(), ((MRuntimeException) e).getHttpStatus());
            } else if (e instanceof MException) {
                throw e;
            } else {
                throw new MException(e.getLocalizedMessage());
            }
        }
    }

    @Deprecated
    @PutMapping("/update/avatar")
    @PreAuthorize("permitAll()")
    public MResponse<?> updateAvatar(@RequestParam(value = "image", required = false) MultipartFile image,
                                     @RequestHeader(name = "Authorization", required = true) String header) throws MException {
        try {
            String username = jwtTokenProvider.getUsernameFromJWT(header.substring(7));
            Account account = accountService.getAccountByUsername(username);
            if (image != null) {
                String fileOldId = account.getAvatar().split("id=")[1];
                URL url = Resources.getResource("photo.jpg"); // target/classes/com/photo.jpg
                java.io.File fileTemp = Paths.get(url.toURI()).toFile();
                image.transferTo(fileTemp);
                File file = driveService.uploadFile(String.valueOf(account.getIdAccount()), fileTemp.getAbsolutePath(), image.getContentType());
                String fileURL = "https://drive.google.com/uc?id=" + file.getId();
                account.setAvatar(fileURL);
                accountService.updateAccount(account);
                driveService.removeById(fileOldId);
                return new MResponse<>("C???p nh???t avatar th??nh c??ng", fileURL);
            } else {
                throw new MException("Kh??ng t??m th???y file", HttpStatus.BAD_REQUEST);
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

    private String createCode() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            Random rand = new Random();
            int x = rand.nextInt(10);
            result.append(x);
        }
        return result.toString();
    }

    private String validDate(String date) {
        try {
            String[] arr = date.split("/");
            int day = Integer.parseInt(arr[0]);
            int month = Integer.parseInt(arr[1]);
            int year = Integer.parseInt(arr[2]);

            if (checkMonth(month) && day > 0 && day <= lastDayOfMonth(day, month, year)) {
                return month + "/" + day + "/" + year;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    private Integer lastDayOfMonth(Integer day, Integer month, Integer year) {
        if (month == 2) {
            if (isLeapYear(year)) return 29;
            return 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        return 31;
    }

    private Boolean checkMonth(Integer month) {
        return month > 0 && month <= 12;
    }

    private Boolean isLeapYear(Integer year) {
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) return true;
        return false;
    }
}

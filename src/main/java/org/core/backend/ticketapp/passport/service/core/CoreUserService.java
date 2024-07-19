package org.core.backend.ticketapp.passport.service.core;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thecarisma.DocumentPojo;
import io.github.thecarisma.FatalObjCopierException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.core.backend.ticketapp.common.enums.Gender;
import org.core.backend.ticketapp.common.enums.UserType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.mailchimp.SendMessage;
import org.core.backend.ticketapp.common.mailchimp.To;
import org.core.backend.ticketapp.common.redis.TwoFADTO;
import org.core.backend.ticketapp.common.request.MessagingServiceRequest;
import org.core.backend.ticketapp.common.request.TwoFaValidationDTO;
import org.core.backend.ticketapp.common.util.Helpers;
import org.core.backend.ticketapp.passport.dao.UserDao;
import org.core.backend.ticketapp.passport.dtos.RoleDto;
import org.core.backend.ticketapp.passport.dtos.core.*;
import org.core.backend.ticketapp.passport.entity.ChangePassword;
import org.core.backend.ticketapp.passport.entity.EmailVerification;
import org.core.backend.ticketapp.passport.entity.*;
import org.core.backend.ticketapp.passport.repository.EmailVerificationRepository;
import org.core.backend.ticketapp.passport.repository.RoleRepository;
import org.core.backend.ticketapp.passport.repository.UserRepository;
import org.core.backend.ticketapp.passport.repository.UserRoleRepository;
import org.core.backend.ticketapp.passport.service.MailChimpService;
import org.core.backend.ticketapp.passport.service.RedisService;
import org.core.backend.ticketapp.passport.service.SmsService;
import org.core.backend.ticketapp.passport.service.TenantService;
import org.core.backend.ticketapp.passport.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
public class CoreUserService extends BaseRepoService<User> implements UserDetailsService {

    private static final String AUTH_2FA = "_AUTH_2FA";
    @Autowired
    private final SharedEnvironment sharedEnvironment;
    protected Page<User> page = Page.empty();
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MailChimpService mailChimpService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ActionService actionService;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private Environment env;
    @Value("${ticketapp.password-reset-url}")
    private String resetPasswordUrl;
    @Value("${user.failed.login.threshold}")
    private Long failedLoginThreshold;
    @Value("${user.password.expiration.in.days}")
    private Long passwordExpirationInDays;
    @Value("${baseFrontEndUrl}")
    private String baseFrontEndUrl;
    @Value("${ticketapp.token-secret}")
    private String secret;
    @Value("${send-2fa-sms}")
    private boolean send2faSms;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SmsService smsService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CoreUserService(UserRepository userRepository) {
        super(userRepository);
        sharedEnvironment = new SharedEnvironment(env);
    }

    public static String maskPhoneNumber(String phoneNumber) {
        var mask = "*****";
        String prefix;
        String suffix;
        if (phoneNumber.length() >= 13) {
            prefix = phoneNumber.substring(0, 4);
            suffix = phoneNumber.substring(9);
        } else {
            prefix = phoneNumber.substring(0, 2);
            suffix = phoneNumber.substring(7);
        }
        return prefix + mask + suffix;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByEmail(s).get();
    }

    @Transactional
    public void updateLogin(User user) {
        user.setLastLogin(new Date());
        user.setLoginAttempt(0);
        userRepository.saveAndFlush(user);
    }

    public Optional<User> getUserById(UUID id) {
        return userDao.getUserById(id);
    }

    public Optional<User> getUserWithPermissionsAndRolesByEmail(String email) {
        var user = userDao.getUserByEmail(email);
        var userPermissions = userDao.getUserPermissions(user.get().getId());
        BeanUtils.copyProperties(userPermissions, user, ObjectUtils.getNullPropertyNames(userPermissions));
        return user;
    }

    public Optional<User> getUserPermissions(UUID id) {
        return userDao.getUserPermissions(id);
    }

    public Optional<User> getMemberByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user;
    }

    public boolean authenticateUser(User _user, String password) {
        return new BCryptPasswordEncoder().matches(password, _user.getPassword());
    }

    public boolean passwordAdhereToPolicy(User user, String password) {
        if (password.length() < 8) { //Password must not be less than 8 characters
            return false;
        }
        //Password must have at least one Upper case character
        //Password must have at least one lower case character
        //Password must have at least one number
        //Password must not have spacing
        if (!Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$").matcher(password).matches()) {
            return false;
        }

        //Password must have at least one special character like
        if (!password.contains("@") && !password.contains("!") && !password.contains("#") && !password.contains("$") &&
                !password.contains("%") && !password.contains("^") && !password.contains("&") && !password.contains("*") &&
                !password.contains("(") && !password.contains(")") && !password.contains("-") && !password.contains("_") &&
                !password.contains("=") && !password.contains("+") && !password.contains("=") && !password.contains(".") &&
                !password.contains("=") && !password.contains(",")) {
            return false;
        }

        //password must not have aby of the user name
        return !password.contains(user.getEmail()) && !password.contains(user.getFirstName()) && !password.contains(user.getLastName());
    }

    public User changePassword(User user, ChangePassword changePassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setFirstTimeLogin(false);
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        user.setModifiedOn(new Date());
        user.setModifiedBy(user.getId());
        user.setPasswordCreatedOn(Instant.now());
        return userRepository.save(user);
    }

    public User renewPassword(User user, RenewPassword renewPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setFirstTimeLogin(false);
        user.setPassword(passwordEncoder.encode(renewPassword.getNewPassword()));
        user.setModifiedOn(new Date());
        user.setModifiedBy(user.getId());
        user.setPasswordCreatedOn(Instant.now());
        return userRepository.save(user);
    }

    @Transactional
    public List<User> createUserFromExcel(LoggedInUserDto loggedInUser, MultipartFile excelSheet) throws IOException {
        Integer rowCounter = 0;
        List<User> uploadedUsers = DocumentPojo.fromExcel(excelSheet.getInputStream(), User.class);
        var usersToBeUploaded = new ArrayList<User>();
        var usersToBeSentMail = new ArrayList<User>();
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        for (User user : uploadedUsers) {
            rowCounter++;
            user.setId(UUID.randomUUID());
            user.setCreatedOn(new Date());
            user.setCreatedBy(loggedInUser.getUserId());
            user.setFirstTimeLogin(true);
            try {
                user.setDob(dateFormat.parse(user.getDateOfBirth()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                user.setDepartmentId(UUID.fromString(user.getDepartmentIdString()));
            } catch (ParseException e) {
                throw new ApplicationException(400, "bad_date_of_birth", String.format("Date of birth format is bad at row %s, please use this format 'yyyy-MM-ddTHH:mm:ss'", rowCounter));
            } catch (Exception ex) {
                throw new ApplicationException(400, "upload_exception", String.format("Failed to process file upload... error at row %s (%s)", rowCounter, ex.getMessage()));
            }
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
            var password = PasswordUtil.generatePassword();
            user.setPassword(passwordEncoder.encode(password));
            user.setPasswordCreatedOn(Instant.now());
            user.setCreatedOn(new Date());
            user.setGender(user.getGender().toUpperCase());
            User newUser = new User();
            BeanUtils.copyProperties(user, newUser);
            usersToBeUploaded.add(newUser);

            user.setPassword(password);
            usersToBeSentMail.add(user);
        }
        List<User> users = userRepository.saveAllAndFlush(usersToBeUploaded);
        new Thread(() -> usersToBeSentMail.forEach(this::sendRegistrationEmail)).start();
        return users;
    }

    @Transactional
    public User createUser(final UserDto userDto, final LoggedInUserDto loggedInUser) throws JsonProcessingException {
        final var user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setId(UUID.randomUUID());
        user.setCreatedOn(new Date());
        user.setCreatedBy(loggedInUser.getUserId());
        user.setFirstTimeLogin(true);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final var password = PasswordUtil.generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordCreatedOn(Instant.now());
        user.setCreatedOn(new Date());
        final var gender = Gender.valueOf(userDto.getGender().toUpperCase());
        user.setGender(org.apache.commons.lang3.ObjectUtils.isEmpty(gender) ? "OTHERS" : gender.name());
        user.setType(userDto.getType());

        userRepository.saveAndFlush(user);
        user.setPassword(password); //set the password so it can be sent to the user via email

        if (!userDto.getRoleIds().isEmpty()) {
            var userRolesDtos = userDto.getRoleIds().stream().map(x -> {
                var userRole = new UserRoleDto();
                userRole.setRoleId(x);
                userRole.setUserId(user.getId());
                return userRole;
            }).collect(Collectors.toList());
            assignRolesToUser(userRolesDtos, loggedInUser);
        }

        if (!userDto.getActionIds().isEmpty()) {c
            var userActions = userDto.getActionIds().stream().map(x -> {
                var userAction = new UserActionDto();
                userAction.setActionId(x);
                userAction.setUserId(user.getId());
                return userAction;
            }).collect(Collectors.toList());
            actionService.saveAllUserActions(userActions, loggedInUser);
        }

        if (!userDto.getGroupIds().isEmpty()) {
            groupUserService.assignGroupsToUser(userDto.getGroupIds(), user.getId(), loggedInUser);
        }

        new Thread(() -> {
            try {
                sendRegistrationEmail(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return attachUserToTenantAccount(user, loggedInUser);
    }

    @Transactional
    public User attachUserToTenantAccount(@NotNull final User user, @NotNull LoggedInUserDto loggedInUserDto) throws JsonProcessingException {
        if (user.getType().equals(UserType.MERCHANT_OWNER) && Objects.isNull(user.getTenantId())) {
            final var tenantDto = modelMapper.map(user, TenantDto.class);
            tenantDto.setAccountLockoutDurationInMinutes(5);
            tenantDto.setAccountLockoutThresholdCount(5);
            tenantDto.setState(user.getStateOfOrigin());
            tenantDto.setPasswordExpirationInDays(365);
            tenantDto.setInactivePeriodInMinutes(10);
            tenantDto.setCurrency("NGN");
            tenantDto.setEmailAlert(true);
            tenantDto.setId(UUID.randomUUID());
            final var tenant = tenantService.create(tenantDto, user.getId());
            user.setTenantId(tenant.getId());
            user.setModifiedOn(new Date());
            user.setModifiedBy(user.getId());
        } else if (user.getType().equals(UserType.INDIVIDUAL)) {
            user.setTenantId(loggedInUserDto.getTenantId());
        }
        return save(user);
    }

    public Object sendRegistrationEmail(User user) {
        Context context = new Context();
        context.setVariable("iconUrl", sharedEnvironment.iconUrl);
        context.setVariable("email", user.getEmail());
        context.setVariable("password", user.getPassword());
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("supportEmail", sharedEnvironment.supportEmailAddress);
        context.setVariable("date", new Date().toString());
        context.setVariable("baseFrontEndUrl", baseFrontEndUrl);

        String html = templateEngine.process("user-registration-template", context);

        var to = new To();
        to.setEmail(user.getEmail());
        to.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));
        var message = SendMessage.builder()
                .subject("Welcome to TicketApp")
                .html(html)
                .to(List.of(to))
                .build();
        return mailChimpService.send(message);
    }

    private void sendMailWithMailChimp(User user) {
        Context context = new Context();
        context.setVariable("iconUrl", sharedEnvironment.iconUrl);
        context.setVariable("email", user.getEmail());
        context.setVariable("password", user.getPassword());
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("supportEmail", sharedEnvironment.supportEmailAddress);
        context.setVariable("date", new Date().toString());
        context.setVariable("baseFrontEndUrl", baseFrontEndUrl);

        String html = templateEngine.process("user-registration-template", context);

        var to = new To();
        to.setEmail(user.getEmail());
        to.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));

        var message = SendMessage.builder()
                .subject("Welcome to TicketApp")
                .html(html)
                .to(List.of(to))
                .build();
        mailChimpService.send(message);
    }

    public String previewUserCSV(Stream<User> userStream) {
        AtomicReference<String> text = new AtomicReference<>("FirstName, LastName, MiddleName, Email, DOB, Gender, Position Id, PricingSubscription Id, Unit id\n");
        userStream.forEach(user -> {
            text.updateAndGet(v -> v + String.format("%s, %s, %s, %s, %s\n",
                    user.getFirstName().replaceAll(",", ""),
                    user.getLastName().replaceAll(",", ""),
                    user.getMiddleName() != null ? user.getMiddleName().replaceAll(",", "") : "",
                    user.getEmail().replaceAll(",", ""),
                    user.getDob() != null ? user.getDob().toString().replaceAll(",", "") : ""));
        });
        return text.get();
    }

    public void sendPasswordResetEmail(User user) throws NoSuchAlgorithmException {
        String passwordResetUrl = String.format("%s?token=%s",
                resetPasswordUrl,
                generateUniqueVerificationToken(user, 24));
        Context context = new Context();
        context.setVariable("iconUrl", sharedEnvironment.iconUrl);
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("supportEmail", sharedEnvironment.supportEmailAddress);
        context.setVariable("passwordResetUrl", passwordResetUrl);
        context.setVariable("date", new Date().toString());

        String html = templateEngine.process("reset-password-template", context);
        var to = new To();
        to.setEmail(user.getEmail());
        to.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));

        var message = SendMessage.builder()
                .subject("Reset Your TicketApp Account Password")
                .html(html)
                .to(List.of(to))
                .build();
        mailChimpService.send(message);
    }

    private String generateUniqueVerificationToken(User user, int expiryHour) throws NoSuchAlgorithmException {
        EmailVerification emailVerification = new EmailVerification();
        Date dateIssued = new Date();
        Date expiryDate = new Date(System.currentTimeMillis() + ((long) expiryHour * 60 * 60) * 1000);
        String token = Helpers.MD5(Helpers.GetSaltString(
                String.format("%s%d%s", user.getEmail(),
                        dateIssued.getTime(), secret), 10
        ));
        token = Helpers.GetSaltString(user.getFirstName() + token + user.getEmail(), 40);

        emailVerification.setId(UUID.randomUUID());
        emailVerification.setUserId(user.getId());
        emailVerification.setEmailAddress(user.getEmail());
        emailVerification.setDateIssued(dateIssued.getTime() / 1000);
        emailVerification.setExpiryDate(expiryDate.getTime() / 1000);
        emailVerification.setTokenUsed(false);
        emailVerification.setToken(token);
        emailVerificationRepository.save(emailVerification);
        return token;
    }

    public Page<User> listUsers(String firstName, String lastName,
                                String middleName,
                                String email,
                                String gender,
                                String phone,
                                Pageable pageable) {

        return userDao.getUsers(firstName, lastName, middleName, email, gender, phone, pageable);
    }

    public List<User> findUsers(String firstName, String lastName,
                                String middleName) {

        return userRepository.findUser(firstName, lastName, middleName);
    }

    public User updateUser(User existing, UserDto newData) {
        BeanUtils.copyProperties(newData, existing, ObjectUtils.getNullPropertyNames(newData));
        Gender gender = Gender.valueOf(newData.getGender().toUpperCase());
        existing.setGender(org.apache.commons.lang3.ObjectUtils.isEmpty(gender) ? "OTHERS" : gender.name());
        return userRepository.saveAndFlush(existing);
    }

    public User updateUserPicture(User existing, MultipartFile file) throws FatalObjCopierException {
        existing.setProfilePictureLocation("");
        //ObjCopier.copyFieldsExcept(new String[]{"id"}, existing, newData);
        return userRepository.saveAndFlush(existing);
    }

    public List<User> findManagers() throws ParseException {

        return userRepository.findManagers();
    }

    public Page<User> listLockedUsers(String firstName, String lastName, String email, String dateOn, String dateBefore, String dateAfter, String startDate, String endDate, Pageable pageable) throws ParseException {
        Pattern p, q;
        if (dateOn == null && dateBefore == null && dateAfter == null && startDate == null && endDate == null) {
            page = userRepository.listLockedUser(firstName, lastName, email, pageable);
        } else if (dateOn != null) {
            p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}$");
            q = Pattern.compile("\\d{2}-\\d{2}-\\d{4}$");
            if (p.matcher(dateOn).matches() || q.matcher(dateOn).matches()) {
                page = userRepository.filterLockedUserByDateOn(dateOn, pageable);
            }
        } else if (dateBefore != null) {
            p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}$");
            q = Pattern.compile("\\d{2}-\\d{2}-\\d{4}$");
            if (p.matcher(dateBefore).matches() || q.matcher(dateBefore).matches()) {
                page = userRepository.filterLockedUserByDateBefore(dateBefore, pageable);
            }
        } else if (dateAfter != null) {
            p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}$");
            q = Pattern.compile("\\d{2}-\\d{2}-\\d{4}$");
            if (p.matcher(dateAfter).matches() || q.matcher(dateAfter).matches()) {
                page = userRepository.filterLockedUserByDateAfter(dateAfter, pageable);
            }
        } else if (startDate != null && endDate != null) {
            p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}$");
            q = Pattern.compile("\\d{2}-\\d{2}-\\d{4}$");
            if ((p.matcher(startDate).matches() || q.matcher(startDate).matches()) && (p.matcher(endDate).matches() || q.matcher(endDate).matches())) {
                page = userRepository.filterLockedUserByDateBetween(startDate, endDate, pageable);
            }
        }
        return page;
    }

    public void updateFailedLogin(User user) {
        user.setLoginAttempt(user.getLoginAttempt() + 1);
        user.setLastFailedLogin(new Date());
        userRepository.saveAndFlush(user);
    }

    public void processUserLoginValidation(User user) {
        if (user.isDeactivated()) {
            throw new ApplicationException(401, "unauthorized", "Your account is currently inactive. Please reach out to support.");
        }
        if (Objects.nonNull(user.getLockDate()) && ChronoUnit.MINUTES.between(user.getLockDate().toInstant(), Instant.now()) >= user.getAccountLockoutDurationInMinutes() && user.getAccountLockoutDurationInMinutes() != 0) {
            user.setLocked(false);
            user.setLockedBy(null);
            user.setLockDate(null);
            user.setLoginAttempt(0);
            userRepository.save(user);
        } else if (user.isLocked()) {
            throw new ApplicationException(401, "unauthorized", "Your account is currently locked. Please reach out to support.");
        }
        if (user.getLoginAttempt() >= failedLoginThreshold) {
            user.setLocked(true);
            user.setLockDate(new Date());
            user.setLockedBy(user.getCreatedBy());
            userRepository.save(user);
            throw new ApplicationException(401, "unauthorized", "Your account is currently locked. Please reach out to support.");
        }
        if (ChronoUnit.DAYS.between(user.getPasswordCreatedOn(), Instant.now()) >= passwordExpirationInDays) {
            throw new ApplicationException(401, "password_expired", "Your password has expired, kindly change your password.");
        }
    }

    public void validate2FAToken(TwoFaValidationDTO twoFaValidationDTO) throws JsonProcessingException {
        String key = twoFaValidationDTO.getUserId() + AUTH_2FA;
        String fetchedData = redisService.fetchDataAsString(key);
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(fetchedData)) {
            TwoFADTO otpData = objectMapper.readValue(fetchedData, TwoFADTO.class);
            if (Objects.equals(twoFaValidationDTO.getOtp(), otpData.getOtp())) {
                CompletableFuture.runAsync(() -> redisService.deleteData(key));
                return;
            } else
                throw new ApplicationException(400, "expired_or_invalid_otp", "Token is invalid or has expired.");
        }
        throw new ApplicationException(400, "expired_or_invalid_otp", "Token is invalid or has expired.");
    }

    public void send2FAToken(String userId, String phone, User user) throws JsonProcessingException {
        TwoFADTO twoFADTO = TwoFADTO.builder().build();
        twoFADTO.setDateCreated(new Date());
        twoFADTO.setOtp(RandomStringUtils.randomNumeric(6));
        twoFADTO.setPurpose("Authentication");
        redisService.storeDataAsString(userId + AUTH_2FA, objectMapper.writeValueAsString(twoFADTO), 1L);

        if (send2faSms) {
            smsService.sendSingleSms(
                    MessagingServiceRequest.builder()
                            .to(phone)
                            .text(String.format("[TicketApp] 2FA token: %s. Do not share this with anyone. 2FA token valid for 60secs", twoFADTO.getOtp()))
                            .build()
            );
        } else {
            Context context = new Context();
            context.setVariable("iconUrl", sharedEnvironment.iconUrl);
            context.setVariable("email", user.getEmail());
            context.setVariable("firstName", user.getFirstName());
            context.setVariable("supportEmail", sharedEnvironment.supportEmailAddress);
            context.setVariable("date", new Date().toString());
            context.setVariable("baseFrontEndUrl", baseFrontEndUrl);
            context.setVariable("authToken", twoFADTO.getOtp());
            String html = templateEngine.process("user-2fa-template", context);
            var to = new To();
            to.setEmail(user.getEmail());
            to.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));
            var message = SendMessage.builder()
                    .subject("TicketApp Login OTP").html(html).to(List.of(to)).build();
            var emailResponse = mailChimpService.send(message);
            Arrays.stream(emailResponse).parallel().forEach(mailChimpResponse1 -> {
                log.info(String.format("----||||PUBLISHED MAIL SENDING STATUS||||----> %s", mailChimpResponse1.getStatus()));
                log.info(String.format("----||||TO MAIL||||----> %s", mailChimpResponse1.getEmail()));
            });
        }
    }

    public Role updateRole(Role _role, RoleDto roleDto) {
        BeanUtils.copyProperties(roleDto, _role, ObjectUtils.getNullPropertyNames(roleDto));
        _role.setName(roleDto.getName());
        _role.setCode(StringUtil.normalizeWithUnderscore(roleDto.getName()));
        _role.setNormalizedName(StringUtil.normalizeString(roleDto.getName()));
        roleRepository.saveAndFlush(_role);
        return _role;
    }

    public Role createRole(RoleDto roleDto, LoggedInUserDto user) {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(roleDto.getName());
        role.setCreatedBy(user.getUserId());
        role.setCreatedOn(new Date());
        role.setCode(StringUtil.normalizeWithUnderscore(roleDto.getName()));
        role.setNormalizedName(StringUtil.normalizeString(roleDto.getName()));
        role.setDescription(roleDto.getDescription());

        return roleRepository.saveAndFlush(role);
    }

    public Optional<Role> getByName(String name) {
        return roleRepository.getByName(name);
    }

    public Optional<Role> getRoleByRoleId(UUID id) {
        return roleRepository.getByUUID(id);
    }

    @Deprecated
    public Page<Role> getAll(String name, Pageable pageable) {
        Page<Role> page = roleRepository.getAll(name.toUpperCase(), pageable);
        List<Role> modifiedList = new ArrayList<>(page.getContent());
        for (Role role : modifiedList) {
            Set<UserRole> userRole = userRoleRepository.findAllByRoleId(role.getId());
        }
        return new PageImpl<>(modifiedList, pageable,
                (page.getTotalElements() - (page.getTotalElements() - modifiedList.size())));
    }

    public UserRole assignRoleToUser(UserRoleDto userRoleDto, LoggedInUserDto loggedInUserDto) {
        var userRole = new UserRole();
        userRole.setRoleId(userRoleDto.getRoleId());
        userRole.setId(UUID.randomUUID());
        userRole.setUserId(userRoleDto.getUserId());
        userRole.setCreatedBy(loggedInUserDto.getUserId());
        userRole.setCreatedOn(new Date());
        return userRoleRepository.save(userRole);
    }

    public void assignRolesToUser(List<UserRoleDto> userRoleDtos, LoggedInUserDto loggedInUserDto) {
        var userRoles = userRoleDtos.stream().map(userRoleDto -> {
            var userRole = new UserRole();
            userRole.setRoleId(userRoleDto.getRoleId());
            userRole.setId(UUID.randomUUID());
            userRole.setUserId(userRoleDto.getUserId());
            userRole.setCreatedBy(loggedInUserDto.getUserId());
            userRole.setCreatedOn(new Date());
            return userRole;
        }).collect(Collectors.toList());
        userRoleRepository.saveAllAndFlush(userRoles);
    }
}

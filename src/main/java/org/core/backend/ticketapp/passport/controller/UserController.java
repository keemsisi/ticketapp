package org.core.backend.ticketapp.passport.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thecarisma.FatalObjCopierException;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.enums.UserType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dao.UserDao;
import org.core.backend.ticketapp.passport.dtos.core.*;
import org.core.backend.ticketapp.passport.entity.ChangePassword;
import org.core.backend.ticketapp.passport.entity.EmailVerification;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.SmsService;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.service.core.EmailVerificationService;
import org.core.backend.ticketapp.passport.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;


@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private CoreUserService userService;
    private EmailVerificationService emailVerificationService;
    private JwtTokenUtil jwtTokenUtil;
    private UserDao userDao;
    private ObjectMapper objectMapper;
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    private SmsService smsService;
    private UserAuthenticationService userAuthenticationService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ModelMapper modelMapper;


    @RequestMapping(value = "/onboard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userOnboarding(@Validated @RequestBody UserLiteDto userDto) throws JsonProcessingException {
        final var request = modelMapper.map(userDto, UserDto.class);
        if (userDto.getAccountType().equals(AccountType.SUPER_ADMIN) || userDto.getAccountType().equals(AccountType.SYSTEM_ADMIN_USER)) {
            UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), "super_admin");
        }
        if (userDto.getUserType().equals(UserType.SELLER) && userDto.getAccountType().equals(AccountType.INDIVIDUAL)) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Seller with Individual account type not allowed!", null),
                    HttpStatus.BAD_REQUEST);
        }
        final var newRegisteredUser = userService.createUser(request, new LoggedInUserDto());
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), null,
                objectMapper.writeValueAsString(newRegisteredUser), "Initiated a request to register a user under a tenant");
        return new ResponseEntity<>(
                new GenericApiResponse<>("00", "The user has been successfully registered", ""),
                HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> viewUser(@PathVariable UUID id) {
        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "No user with the id '" + id + "' found", ""),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new GenericApiResponse<>("00", "", user.get()),
                HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listUsers(@RequestParam(required = false) String firstName,
                                       @RequestParam(required = false) String lastName,
                                       @RequestParam(required = false) String middleName,
                                       @RequestParam(required = false) String email,
                                       @RequestParam(required = false) String gender,
                                       @RequestParam(required = false) String phone,
                                       @RequestParam(required = false) Integer pageSize,
                                       @RequestParam(required = false) Integer pageNumber,
                                       @RequestParam(required = false) Sort.Direction order,
                                       @RequestParam(required = false) String[] sortBy
    ) {
        String _firstName = firstName != null ? firstName.toLowerCase() : null;
        String _lastName = lastName != null ? lastName.toLowerCase() : null;
        String _middleName = middleName != null ? middleName.toLowerCase() : null;

        var user = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(user.getRoles(), ConstantUtil.SUPER_ADMIN);

        Page<User> users = userService.listUsers(_firstName, _lastName, _middleName,
                email, gender, phone, ResponsePageRequest.createPageRequest(pageNumber, pageSize, order, sortBy, true, "created_on"));

        return new ResponseEntity<>(new GenericApiResponse<>("00", "", users),
                HttpStatus.OK);
    }


    @RequestMapping(value = "/locked", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listLockedUsers(@RequestParam(required = false) String firstName,
                                             @RequestParam(required = false) String lastName,
                                             @RequestParam(required = false) String email,
                                             @RequestParam(required = false) String dateOn,
                                             @RequestParam(required = false) String dateBefore,
                                             @RequestParam(required = false) String dateAfter,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate,
                                             @RequestParam(required = false) Integer pageNumber,
                                             @RequestParam(required = false) Integer pageSize,
                                             @RequestParam(required = false) Sort.Direction order,
                                             @RequestParam(required = false) String[] sortBy) throws ParseException {
        String _firstName = firstName != null ? firstName.toLowerCase() : null;
        String _lastName = lastName != null ? lastName.toLowerCase() : null;
        String _email = email;
        String _dateOn = dateOn;
        String _startDate = startDate;
        String _endDate = endDate;
        String _dateBefore = dateBefore;
        String _dateAfter = dateAfter;

        var loggedInUser = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.SUPER_ADMIN);

        Page<User> users = userService.listLockedUsers(_firstName, _lastName, _email, _dateOn, _dateBefore, _dateAfter, _startDate, _endDate, ResponsePageRequest.createPageRequest(pageNumber, pageSize, order, sortBy, true, "created_on"));

        return new ResponseEntity<>(new GenericApiResponse<>("00", "", users),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUsers(@RequestParam(required = false) String firstName,
                                       @RequestParam(required = false) String lastName,
                                       @RequestParam(required = false) String middleName) throws ParseException {
        List<User> userEntities = userService.findUsers(firstName, lastName, middleName);

        return new ResponseEntity<>(new GenericApiResponse<>("00", "", userEntities),
                HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDto userDto) throws JsonProcessingException {
        User newRegisteredUser = null;
        var loggedInUser = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.ONBOARD_USER);
        if (Objects.nonNull(userDto.getUserType()) && loggedInUser.getUserType().isBuyerOrSeller()) {
            if ((loggedInUser.getUserType().isSeller() && userDto.getUserType().isBuyer()
                    || loggedInUser.getUserType().isBuyer() && userDto.getUserType().isSeller())
                    || (!AccountType.isTenantUserAccountType(userDto.getAccountType()))) {
                return new ResponseEntity<>(
                        new GenericApiResponse<>("01", "Oops! Not Allowed!", ""),
                        HttpStatus.FORBIDDEN);
            }
        }
        if (AccountType.isAdminUser(userDto.getAccountType()) && !loggedInUser.isAdmin()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Oops! Not Allowed! Only super admin account!", ""),
                    HttpStatus.FORBIDDEN);
        }
        newRegisteredUser = userService.createUser(userDto, loggedInUser);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), null, objectMapper.writeValueAsString(newRegisteredUser), "Initiated a request to register a user under a tenant");
        return new ResponseEntity<>(
                new GenericApiResponse<>("00", "The user has been successfully registered", ""),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/email/registration/resend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resendRegistrationEmail() {
        var loggedInUser = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.SUPER_ADMIN);
        Optional<User> userResult = userService.getUserById(jwtTokenUtil.getUser().getUserId());
        if (!userResult.isPresent()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Unable to find member account. Ensure you are logged into the system", ""),
                    HttpStatus.BAD_REQUEST);
        }
        var response = userService.sendRegistrationEmail(userResult.get());
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), null, null, "Initiated a request to resend registration email");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Invitation email resent to user.", response), HttpStatus.OK);
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userFullProfile(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization) {
        var loggedInUser = jwtTokenUtil.getUser();
        var user = userService.getUserById(loggedInUser.getUserId());
        return new ResponseEntity<>(new GenericApiResponse<>("00", "", user), HttpStatus.OK);
    }

    @RequestMapping(value = "{userId}/avatar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserAvatar(@PathVariable(value = "userId") UUID userId) {
        var userResult = userDao.getUserAvatar(userId);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "", userResult.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/lock", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> lockUser(@RequestBody Map<String, Object> body) throws JsonProcessingException {
        User updatedUserAccount = null;
        String oldDataJSON = "";
        var loggedInUser = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.SUPER_ADMIN);
        Optional<User> userResult = userService.getUserById((UUID.fromString((String) body.get("id"))));


        if (userResult.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Unable to find member with the id '" +
                            body.get("id") + "'", ""),
                    HttpStatus.NOT_FOUND);
        }

        var user = userResult.get();
        oldDataJSON = objectMapper.writeValueAsString(user);
        user.setLocked((boolean) body.get("lock"));
        if (user.isLocked()) {
            user.setLockDate(new Date());
            user.setLockedBy(loggedInUser.getUserId());
        } else {
            user.setLockedBy(null);
            user.setLockDate(null);
            user.setLoginAttempt(0);
        }
        updatedUserAccount = userService.save(user);

        String lockMsg = (Boolean) body.get("lock") ? "This account has been successfully locked" : "This account has been successfully unlocked";
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUserAccount), "Initiated a request to get a user Avatar");
        return new ResponseEntity<>(new GenericApiResponse<>("00", lockMsg,
                null), HttpStatus.OK);
    }


    @RequestMapping(value = "/deactivate", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deActivateUser(@RequestBody Map<String, Object> body) throws JsonProcessingException {
        User unUpdatedUserAccount = null, updatedUserAccount = null;
        String oldDataJSON = "";
        Optional<User> userResult = userService.getUserById(UUID.fromString((String) body.get("id")));
        if (userResult.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Unable to find member with the id '" +
                            body.get("id") + "'", ""),
                    HttpStatus.FORBIDDEN);
        }
        unUpdatedUserAccount = userResult.get();
        oldDataJSON = objectMapper.writeValueAsString(unUpdatedUserAccount);
        unUpdatedUserAccount.setDeactivated((Boolean) body.get("deactivate"));
        updatedUserAccount = userService.save(unUpdatedUserAccount);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUserAccount), "Initiated a request to get a user Avatar");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "This account has been successfully activate or deactivate",
                null), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMyProfile(@Validated @RequestBody UserDto userDto) throws FatalObjCopierException, JsonProcessingException {
        String oldDataJSON = null;
        User updatedUserAccount = null;
        var _user = jwtTokenUtil.getUser();
        Optional<User> userResult = userService.getUserById(_user.getUserId());

        if (userResult.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Unable to find member account. Ensure you are logged into the system", ""),
                    HttpStatus.FORBIDDEN);
        }
        oldDataJSON = objectMapper.writeValueAsString(userResult.get());
        updatedUserAccount = userService.updateUser(userResult.get(), userDto);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUserAccount), "Initiated a request to update your profile");

        return new ResponseEntity<>(new GenericApiResponse<>("00", "", updatedUserAccount), HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Validated @RequestBody UserDto
                                                   userDto, @PathVariable(name = "userId") UUID userId) throws JsonProcessingException {
        String oldDataJSON = null;
        User updatedUserAccount = null;
        Optional<User> userResult = userService.getUserById(userId);
        if (userResult.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Unable to find member account. Ensure you are logged into the system", ""),
                    HttpStatus.FORBIDDEN);
        }
        final var loggedInUser = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.SUPER_ADMIN);
        oldDataJSON = objectMapper.writeValueAsString(userResult.get());
        updatedUserAccount = userService.updateUser(userResult.get(), userDto);
        activityLogProcessorUtils.processActivityLog(loggedInUser.getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUserAccount), "Initiated a request to update profile");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "", updatedUserAccount), HttpStatus.OK);
    }


    @RequestMapping(value = "/profile/photo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfilePicture(@RequestHeader("Authorization") String authorization,
                                                  @Valid @RequestPart("photo") MultipartFile file) throws FatalObjCopierException, JsonProcessingException {
        User updatedUserAccount = null, unUpdatedUserAccount = null;
        String oldDataJSON = null;
        try {
            var user = jwtTokenUtil.getUser();
            Optional<User> userResult = userService.getUserById(user.getUserId());
            if (!userResult.isPresent()) {
                return new ResponseEntity<>(
                        new GenericApiResponse<>("01", "Unable to find member account. Ensure you are logged into the system", ""),
                        HttpStatus.FORBIDDEN);
            }
            unUpdatedUserAccount = userResult.get();
            oldDataJSON = objectMapper.writeValueAsString(unUpdatedUserAccount);
            updatedUserAccount = userService.updateUserPicture(unUpdatedUserAccount, file);
            return new ResponseEntity<>(new GenericApiResponse<>("00", "", updatedUserAccount), HttpStatus.OK);
        } finally {
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUserAccount), "Initiated a request to update a user profile");
        }
    }

    @RequestMapping(value = "/password/reset/email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> requestPasswordChangeEmail(@RequestParam(value = "email") String email) throws
            NoSuchAlgorithmException, JsonProcessingException {
        User unUpdatedUserAccount = null;
        Optional<User> userResult = userService.getMemberByEmail(email);
        if (!userResult.isPresent()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Unable to find member account. Ensure you the email address is correct", null),
                    HttpStatus.FORBIDDEN);
        }
        unUpdatedUserAccount = userResult.get();
        userService.sendPasswordResetEmail(userResult.get());
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId() == null ? UUID.randomUUID() : jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), objectMapper.writeValueAsString(unUpdatedUserAccount), null, "Initiated a request to reset a user password using email to fetch the account to be updated");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "The password reset detail has been sent to your email", null),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPassword resetPassword) throws
            JsonProcessingException {
        User unUpdatedUserAccount = null, updatedUserAccount = null;
        String oldDataJSON = null;
        Optional<EmailVerification> emailVerification = emailVerificationService.getByToken(resetPassword.getToken());

        if (emailVerification.get().isTokenUsed()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "The verification token has already been used", null),
                    HttpStatus.ALREADY_REPORTED);
        }

        Optional<User> user = userService.getUserById(emailVerification.get().getUserId());

        if (!user.isPresent()) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Unable to find member account", null),
                    HttpStatus.FORBIDDEN);
        }

        if (!userService.passwordAdhereToPolicy(user.get(), resetPassword.getPassword())) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "The password does not adhere to the system policy", null),
                    HttpStatus.BAD_REQUEST);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        unUpdatedUserAccount = user.get();
        oldDataJSON = objectMapper.writeValueAsString(unUpdatedUserAccount);

        unUpdatedUserAccount.setPassword(passwordEncoder.encode(resetPassword.getPassword()));

        updatedUserAccount = userService.save(unUpdatedUserAccount);

        emailVerificationService.changeVerificationStatus(emailVerification.get(), true);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUserAccount), "Initiated a request to reset user account password");
        return new ResponseEntity<>(
                new GenericApiResponse<>("00", "Your password has been reset successfully", null),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/password/change", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authorization,
                                            @Valid @RequestBody ChangePassword changePassword) throws JsonProcessingException {
        User updatedUser = null, unUpdatedUserAccount = null;
        String oldDataJSON = null;
        var userId = UUID.fromString((String) jwtTokenUtil.getClaimByKey("user_id"));
        var user = userService.getUserById(userId);

        if (!userService.passwordAdhereToPolicy(user.get(), changePassword.getNewPassword())) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "The password does not adhere to the system policy", ""),
                    HttpStatus.BAD_REQUEST);
        }

        if (!new BCryptPasswordEncoder().matches(changePassword.getOldPassword(), user.get().getPassword())) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Kindly confirm that your old password is correct.", ""),
                    HttpStatus.UNAUTHORIZED);
        }

        if (new BCryptPasswordEncoder().matches(changePassword.getNewPassword(), user.get().getPassword())) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "You cannot use an old password. Set a new one", ""),
                    HttpStatus.BAD_REQUEST);
        }
        unUpdatedUserAccount = user.get();
        oldDataJSON = objectMapper.writeValueAsString(unUpdatedUserAccount);
        updatedUser = userService.changePassword(user.get(), changePassword);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUser), "Initiated a request to change user account password");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Password changed successfully", null),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/password/renew", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> renewPassword(@Valid @RequestBody RenewPassword renewPassword) throws
            JsonProcessingException {
        User updatedUserAccount = null;
        String oldDataJSON = null;
        var _user = userService.getMemberByEmail(renewPassword.getEmail());

        if (_user.isEmpty()) {
            throw new ApplicationException(401, "401", "Invalid username or password");
        }

        var user = _user.get();
        oldDataJSON = objectMapper.writeValueAsString(user);

        if (!userService.passwordAdhereToPolicy(user, renewPassword.getNewPassword())) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "The password does not adhere to the system policy", ""),
                    HttpStatus.BAD_REQUEST);
        }

        if (!new BCryptPasswordEncoder().matches(renewPassword.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "Invalid username or password", ""),
                    HttpStatus.UNAUTHORIZED);
        }

        if (new BCryptPasswordEncoder().matches(renewPassword.getNewPassword(), user.getPassword())) {
            return new ResponseEntity<>(
                    new GenericApiResponse<>("01", "You cannot use an old password. Set a new one", ""),
                    HttpStatus.BAD_REQUEST);
        }

        updatedUserAccount = userService.renewPassword(user, renewPassword);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedUserAccount), "Initiated a request to change user account password");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Password changed successfully", null),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/managers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findManagers(@RequestHeader("Authorization") String authorization) throws
            ParseException {
        List<User> userEntities = userService.findManagers();
        return new ResponseEntity<>(new GenericApiResponse<>("00", "", userEntities),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/actions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getActionsByUserId(@PathVariable(value = "userId") UUID userId) {
        var userActions = userDao.getUserActionsById(jwtTokenUtil.getUser().getUserId());
        return new ResponseEntity<>(
                new GenericApiResponse<>("00", "User actions retrieved successfully.", userActions),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRolesByUserId() {
        var userRoles = userDao.getUserRolesById(jwtTokenUtil.getUser().getUserId());
        return new ResponseEntity<>(
                new GenericApiResponse<>("00", "User roles retrieved successfully.", userRoles),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGroupsByUserId() {
        var userGroups = userDao.getUserGroupsByUserId(jwtTokenUtil.getUser().getUserId());
        return new ResponseEntity<>(
                new GenericApiResponse<>("00", "User actions retrieved successfully.", userGroups),
                HttpStatus.OK);
    }


    @RequestMapping(value = "/uploads/bulk", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadUsers(@Valid @RequestPart("file") MultipartFile file) throws IOException, ParseException {
        List<User> users;
        var user = jwtTokenUtil.getUser();
        users = userService.createUserFromExcel(user, file);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), User.class.getTypeName(), null, objectMapper.writeValueAsString(users), "Initiated a request to fetch a user groups");
        return new ResponseEntity<>(
                new GenericApiResponse<>("00", "User records uploaded successfully.", users),
                HttpStatus.OK);
    }
}
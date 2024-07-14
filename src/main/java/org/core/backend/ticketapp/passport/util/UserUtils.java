package org.core.backend.ticketapp.passport.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class UserUtils {
    private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
    private static final String MOBILE_NO_PATTERN = "^((\\+|00)?[1-9]|0)\\d{1,14}$";
    private static final String REGION_CODE_SEPARATOR = "|";
    private static final String DEFAULT_REGION_CODE = "NG";
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public static boolean isEmail(String email) {
        return StringUtils.isNotBlank(email) && email.matches(EMAIL_PATTERN);
    }

    public static boolean isPhoneNumber(String mobileNo) {
        String number = mobileNo.contains("|") ?
                StringUtils.substringAfterLast(mobileNo, REGION_CODE_SEPARATOR) :
                mobileNo;
        return StringUtils.isNotBlank(number) && number.matches(MOBILE_NO_PATTERN);
    }

    public static String normalizePhoneNumber(String mobileNo) {
        if (!isPhoneNumber(mobileNo)) {
            throw new ApplicationException(400, "invalid_phone_number", "Invalid phone number format, check and try again.");
        }
        try {
            String[] values = mobileNo.split(String.format("\\%s", REGION_CODE_SEPARATOR)); // split() argument is a regex
            String country;
            String number;
            switch (values.length) {
                case 1:
                    country = DEFAULT_REGION_CODE;
                    number = values[0];
                    break;
                case 2:
                    country = values[0];
                    number = values[1];
                    break;
                default:
                    throw new InvalidParameterException("Invalid mobile number");
            }
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, country);
            PhoneNumberUtil.PhoneNumberType type = phoneNumberUtil.getNumberType(phoneNumber);
            if (type == PhoneNumberUtil.PhoneNumberType.MOBILE ||
                    type == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE) {
                String phoneNumberE164 = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                return phoneNumberE164.startsWith("+") ? phoneNumberE164.substring(1) : phoneNumberE164;
            } else {
                throw new InvalidParameterException("Invalid mobile number");
            }
        } catch (NumberParseException e) {
            throw new InvalidParameterException("Invalid mobile number");
        }
    }

    public static void assertUserHasRole(List<String> roles, String role) {
        if (!roles.contains(role)) {
            throw new ApplicationException(403, "403", "You don't have the right permission to complete this action.");
        }
    }

    public static String generateOTP() {
        int randomPin = (int) (Math.random() * 900000) + 100000;
        String otp = String.valueOf(randomPin);
        return otp;
    }

    public static String validateUsername(String username) {
        if (isEmail(username)) {
            return username;
        } else if (isPhoneNumber(username)) {
            return normalizePhoneNumber(username);
        } else {
            throw new InvalidParameterException("Invalid user name");
        }
    }

    public static boolean isValidMobileNo(String mobileNo) {
        boolean valid;
        try {
            valid = StringUtils.isNotBlank(mobileNo) &&
                    StringUtils.isNotBlank(normalizePhoneNumber(mobileNo));
        } catch (InvalidParameterException e) {
            valid = false;
        }
        return valid;
    }

    public static String maskRecipient(String recipient) {
        if (StringUtils.isNotBlank(recipient)) {
            StringBuilder masked = new StringBuilder(recipient);

            int start = recipient.length() / 3;
            int end = recipient.length() - start;
            int maskLen = end - start;

            String mask = "";
            for (int i = 0; i < maskLen; i++) {
                mask += "*";
            }
            masked.replace(start, end, mask);

            recipient = masked.toString();
        }
        return recipient;
    }

    public static String i8nMobileNo(String mobileNo) {
        if (!mobileNo.startsWith("+")) {
            return String.format("+%s", mobileNo);
        }
        return mobileNo;
    }

    public static String getPhoneNumberRegionCode(String mobileNo) {
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(mobileNo, DEFAULT_REGION_CODE);
            String regionCode = phoneNumberUtil.getRegionCodeForNumber(phoneNumber);
            if (StringUtils.isNotBlank(regionCode)) {
                return regionCode;
            } else {
                throw new InvalidParameterException("Invalid mobile number");
            }
        } catch (NumberParseException e) {
            throw new InvalidParameterException("Invalid mobile number");
        }
    }

    public static void isResourceOwner(final UUID userId) {
        final var user = JwtTokenUtil.getAuthUser();
        if (Objects.nonNull(userId) && !userId.equals(JwtTokenUtil.getAuthUser().getUserId())) {
            UserUtils.assertUserHasRole(user.getRoles(), "system_admin");
        }
    }
}

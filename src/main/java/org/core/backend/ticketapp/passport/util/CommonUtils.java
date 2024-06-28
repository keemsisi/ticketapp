package org.core.backend.ticketapp.passport.util;

import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;

import java.util.List;

public class CommonUtils {
    public static String getDelimitedActionNamesWithSingleQuotes(List<String> actionNames) {
        StringBuilder actions = new StringBuilder();
        for (int i = 0; i < actionNames.size(); i++) {
            if (i == actionNames.size() - 1) actions.append(String.format("'%s'", actionNames.get(i)));
            else actions.append(String.format("'%s',", actionNames.get(i)));
        }
        return actions.toString();
    }

    public static String getFullName(LoggedInUserDto loggedInUserDto) {
        return String.join(
                " ",
                loggedInUserDto.getFirstName(),
                ObjectUtils.isEmpty(loggedInUserDto.getMiddleName()) ? "" : loggedInUserDto.getMiddleName(),
                loggedInUserDto.getLastName());
    }
}

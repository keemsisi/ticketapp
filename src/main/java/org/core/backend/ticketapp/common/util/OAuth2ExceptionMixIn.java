package org.core.backend.ticketapp.common.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = JsonOAuth2ExceptionSerializer.class)
public abstract class OAuth2ExceptionMixIn {
}

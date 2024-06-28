package org.core.backend.ticketapp.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

public class JsonGrantedAuthorityDeserializer extends JsonDeserializer<List<GrantedAuthority>> {

    @Override
    public List<GrantedAuthority> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        List roles = jp.readValueAs(new TypeReference<List<String>>() {});
        return AuthorityUtils.commaSeparatedStringToAuthorityList(
                StringUtils.collectionToCommaDelimitedString(roles));
    }
}

package org.core.backend.ticketapp.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonGrantedAuthoritySerializer extends JsonSerializer<List<GrantedAuthority>> {

    @Override
    public void serialize(List<GrantedAuthority> authorities, JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        String string = StringUtils.collectionToCommaDelimitedString(authorities);
        List<String> roles = StringUtils.hasText(string) ?
                Arrays.asList(string.split(",")) : Collections.emptyList();
        gen.writeObject(roles);
    }
}

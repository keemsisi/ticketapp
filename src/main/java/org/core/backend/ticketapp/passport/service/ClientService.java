package org.core.backend.ticketapp.passport.service;

import io.github.thecarisma.FatalObjCopierException;
import io.github.thecarisma.ObjCopier;
import org.core.backend.ticketapp.passport.dao.ClientDao;
import org.core.backend.ticketapp.passport.dtos.core.BasicClientDetails;
import org.core.backend.ticketapp.passport.dtos.core.ClientDto;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.entity.Client;
import org.core.backend.ticketapp.passport.repository.ClientRepository;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@SuppressWarnings("deprecation")
@Service
public class ClientService implements ClientDetailsService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDao clientDao;

    public Client create(ClientDto clientDto, LoggedInUserDto userDto) throws FatalObjCopierException {
        var client = new Client();
        ObjCopier.copyFields(client, clientDto);
        client.setId(UUID.randomUUID());
        var clientSecret = UUID.randomUUID().toString();
        client.setClientSecret(passwordEncoder.encode(clientSecret));
        client.setCreatedBy(userDto.getUserId());
        client.setCreatedOn(new Date());
        client.setNormalizedName(StringUtil.normalizeWithUnderscore(clientDto.getClientName()));
        clientRepository.save(client);

        client.setClientSecret(clientSecret);
        return client;
    }

    public Client getClientById(UUID clientId) {

        return clientDao.getClientById(clientId);
    }

    public BasicClientDetails getBasicClientDetails(String clientId) {
        return clientDao.getBasicClientDetailsById(UUID.fromString(clientId));
    }


    @Transactional
    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        return clientDao.getClientById(UUID.fromString(s));
    }
}

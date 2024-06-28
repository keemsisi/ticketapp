package org.core.backend.ticketapp.passport.service.core;

import org.core.backend.ticketapp.passport.dtos.core.FileTemplateDto;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.entity.FileTemplate;
import org.core.backend.ticketapp.passport.repository.FileTemplateRepository;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class FileTemplateService extends BaseRepoService<FileTemplate> {


    @Autowired
    private FileTemplateRepository fileTemplateRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public FileTemplateService(FileTemplateRepository repository) {
        super(repository);
    }

    public Optional<FileTemplate> create(FileTemplateDto fileTemplateDto, LoggedInUserDto loggedInUserDto) {
        var template = FileTemplate.builder()
                .id(UUID.randomUUID())
                .name(fileTemplateDto.getName())
                .url(fileTemplateDto.getUrl())
                .description(fileTemplateDto.getDescription())
                .normalizedName(StringUtil.normalizeWithUnderscore(fileTemplateDto.getName()))
                .createdBy(loggedInUserDto.getUserId())
                .createdOn(new Date())
                .build();
        return Optional.of(fileTemplateRepository.saveAndFlush(template));
    }

    public Optional<FileTemplate> getTemplateByName(String name) {
        return fileTemplateRepository.findByName(name);
    }
}

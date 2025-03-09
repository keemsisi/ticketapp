package org.core.backend.ticketapp.transaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.transaction.dto.wallet.request.CreateWalletDTO;
import org.core.backend.ticketapp.transaction.dto.wallet.request.WalletUpdateRequestDTO;
import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;
import org.core.backend.ticketapp.transaction.service.wallet.WalletService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/v1/api/wallets")
public class WalletController {
    private final WalletService walletService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;
    private final ActivityLogProcessorUtils activityLogProcessorUtils;

    @PreAuthorize("hasAuthority('SCOPE_wallet_create')")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Wallet>> createWallet(@Valid @RequestBody CreateWalletDTO request) throws JsonProcessingException {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), ConstantUtil.SUPER_ADMIN);
        final var wallet = walletService.createWallet(request);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Wallet.class.getTypeName(), null, objectMapper.writeValueAsString(wallet),
                "Initiated a request to create wallet");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully created wallet", wallet), HttpStatus.OK);
    }

    @RequestMapping(value = "/{walletId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Wallet>> getById(@PathVariable(value = "walletId") UUID walletId) throws JsonProcessingException {
        final var wallet = walletService.getWalletById(walletId);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Wallet.class.getTypeName(), null, objectMapper.writeValueAsString(wallet),
                "Initiated a request to get wallet");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Resource was found", wallet), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        final var result = PagedMapperUtil.map(walletService.getAll(pageable));
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", "Successfully fetched resource", result));
    }

    @PreAuthorize("hasAuthority('SCOPE_wallet_delete')")
    @RequestMapping(value = "/{walletId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Wallet>> delete(@PathVariable(value = "walletId") UUID walletId) throws JsonProcessingException {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), ConstantUtil.SUPER_ADMIN);
        final var wallet = walletService.deleteWalletById(walletId);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Wallet.class.getTypeName(), null, objectMapper.writeValueAsString(wallet),
                "Initiated a request to delete");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Wallet delete was successful", wallet), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_wallet_update')")
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Wallet>> update(@Valid @RequestBody WalletUpdateRequestDTO request) throws JsonProcessingException {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), ConstantUtil.SUPER_ADMIN);
        final var wallet = walletService.updateWallet(request);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Wallet.class.getTypeName(), null, objectMapper.writeValueAsString(wallet),
                "Initiated a request to update wallet");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Wallet update was successful", wallet), HttpStatus.OK);

    }
}

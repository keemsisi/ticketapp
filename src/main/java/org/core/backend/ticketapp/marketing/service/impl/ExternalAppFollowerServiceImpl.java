package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing.dto.social.FollowUserSocialLinkRequest;
import org.core.backend.ticketapp.marketing.entity.ExternalAppFollower;
import org.core.backend.ticketapp.marketing.repository.ExternalAppFollowerRepository;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.service.wallet.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ExternalAppFollowerServiceImpl implements ExternalAppFollowerService {
    private final WalletService walletService;
    private final ExternalAppFollowerRepository repository;

    @Override
    public ExternalAppFollower follow(final FollowUserSocialLinkRequest request) {
        final var record = getById(request.getId());
        final var wallet = walletService.getOrCreatedWallet(request.getUserId());

        final var followerRecord = new ExternalAppFollower();
        followerRecord.setUserId(record.getUserId());
        followerRecord.setFollowerUserId(request.getId());
        followerRecord.setFollowerUserId(request.getUserId());
        repository.save(followerRecord);

        final var transaction = Transaction.builder().amount(BigDecimal.ONE).build();
        walletService.creditWallet(transaction, wallet);
        return followerRecord;
    }
}

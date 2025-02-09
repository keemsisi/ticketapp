package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing.dto.social.FollowUserSocialLinkRequest;
import org.core.backend.ticketapp.marketing.entity.ExternalAppFollower;
import org.core.backend.ticketapp.marketing.repository.ExternalAppFollowerRepository;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.entity.wallet.WalletType;
import org.core.backend.ticketapp.transaction.service.wallet.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ExternalAppFollowerServiceImpl implements ExternalAppFollowerService {
    private static final String DEFAULT_POINT = "2.00";
    private final WalletService walletService;
    private final ExternalAppFollowerRepository repository;

    @Override
    @Transactional
    public ExternalAppFollower follow(final FollowUserSocialLinkRequest request) {
        final var record = getById(request.getId());
        final var wallet = walletService.getOrCreateWallet(request.getUserId(), WalletType.COIN_WALLET);

        final var followerRecord = new ExternalAppFollower();
        followerRecord.setUserId(record.getUserId());
        followerRecord.setFollowerUserId(request.getId());
        followerRecord.setFollowerUserId(request.getUserId());
        repository.save(followerRecord);

        //later the transaction will be completely created and saved for tracking purpose
        final var transaction = Transaction.builder().amount(new BigDecimal(DEFAULT_POINT)).build();
        walletService.creditWallet(transaction, wallet);
        return followerRecord;
    }
}

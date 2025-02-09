package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.marketing.common.FollowerStatus;
import org.core.backend.ticketapp.marketing.dto.social.FollowUserSocialLinkRequest;
import org.core.backend.ticketapp.marketing.entity.ExternalAppFollower;
import org.core.backend.ticketapp.marketing.repository.ExternalAppFollowerRepository;
import org.core.backend.ticketapp.passport.service.RedisService;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.entity.wallet.WalletType;
import org.core.backend.ticketapp.transaction.service.wallet.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class ExternalAppFollowerServiceImpl implements ExternalAppFollowerService {
    private static final String DEFAULT_POINT = "2.00";
    private static final String DEFAULT_FOLLOW_COUNT = "16";
    private static final String FOLLOWER_LIMIT_TEMPLATE = "follower_limit_user_id_%s";
    private final WalletService walletService;
    private final SocialMediaLinkAdvertisementServiceImpl advertisementService;
    private final ExternalAppFollowerRepository repository;
    private final RedisService redisService;

    @Override
    @Transactional
    public ExternalAppFollower follow(final FollowUserSocialLinkRequest request) {
        validateRequest(request);
        final var socialMediaLinkAdvertisement = advertisementService.getById(request.getId());
        final var wallet = walletService.getOrCreateWallet(request.getUserId(), WalletType.COIN_WALLET, "NGN");
        final var followerRecord = new ExternalAppFollower();
        followerRecord.setFollowerUserId(request.getUserId());
        followerRecord.setUserId(socialMediaLinkAdvertisement.getUserId());
        followerRecord.setSocialMediaLinkAdId(socialMediaLinkAdvertisement.getId());
        followerRecord.setStatus(FollowerStatus.FOLLOWED);
        repository.save(followerRecord);
        //later the transaction will be completely created and saved for tracking purpose
        final var transaction = Transaction.builder().amount(new BigDecimal(DEFAULT_POINT)).build();
        walletService.creditWallet(transaction, wallet);
        decrement(request.getUserId());
        return followerRecord;
    }

    private void validateRequest(final FollowUserSocialLinkRequest request) {
        final var key = String.format(FOLLOWER_LIMIT_TEMPLATE, request.getUserId());
        final var result = redisService.get(key);
        if (StringUtils.isNotBlank(result) && Integer.parseInt(result) == 0) {
            throw new ApplicationException(412, "max_reached", "Oops! You have reached maximum numbers of followers!");
        } else if (StringUtils.isBlank(result)) {
            redisService.put(key, DEFAULT_FOLLOW_COUNT, TimeUnit.HOURS.toMinutes(1));
        }
    }

    private void decrement(final UUID userId) {
        final var key = String.format(FOLLOWER_LIMIT_TEMPLATE, userId);
        final var result = redisService.get(key);
        if (StringUtils.isNotBlank(result) && Integer.parseInt(result) > 0) {
            redisService.decrease(key, 1);
            return;
        }
        throw new ApplicationException(400, "error", "Failed to decrease follower counts...");
    }
}

package com.personalprojects.youtubefeedapi.subscription;

import com.personalprojects.youtubefeedapi.error.BadRequestException;
import com.personalprojects.youtubefeedapi.error.NotFoundException;
import com.personalprojects.youtubefeedapi.error.enums.BadRequestErrorCode;
import com.personalprojects.youtubefeedapi.error.enums.NotFoundErrorCode;
import com.personalprojects.youtubefeedapi.pubsubhubbub.services.IPubSubHubbubService;
import com.personalprojects.youtubefeedapi.user.IUserRepository;
import com.personalprojects.youtubefeedapi.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final ISubscriptionRepository subscriptionRepository;
    private final ISubscriptionMapper subscriptionMapper;
    private final IUserRepository userRepository;
    private final IPubSubHubbubService pubSubHubbubService;

    @Override
    public List<SubscriptionDto> getAllSubscriptions(String userId) {
        return subscriptionRepository.findByUserId(userId).stream()
                .map(subscriptionMapper::toSubscriptionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserSubscriptionDto> getAllUserSubscriptions() {
        return subscriptionMapper.toUserSubscriptionDtoList(
                subscriptionRepository.findAll()
        );
    }

    @Override
    public SubscriptionDto getByUserIdAndSubscriptionId(String userId, Long subscriptionId) {
        Optional<Subscription> foundSubscription = subscriptionRepository
                .findByUserIdAndSubscriptionId(userId, subscriptionId);

        if (foundSubscription.isPresent()) {
            return subscriptionMapper.toSubscriptionDto(foundSubscription.get());
        } else {
            throw new NotFoundException(NotFoundErrorCode.SUBSCRIPTION_ID_NOT_FOUND);
        }
    }

    @Override
    public SubscriptionDto createSubscription(String userId, SubscriptionRequest subscriptionRequest) {
        validateSubscriptionRequest(userId, subscriptionRequest);
        User assignedUser = retrieveUser(userId);

        Subscription subscription = subscriptionMapper.toSubscription(subscriptionRequest);
        subscription.setUser(assignedUser);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        pubSubHubbubService.subscribe(savedSubscription);

        return subscriptionMapper.toSubscriptionDto(savedSubscription);
    }

    @Override
    public List<SubscriptionDto> createSubscriptions(String userId, List<SubscriptionRequest> subscriptionRequest) {
        subscriptionRequest.forEach(subRequest -> validateSubscriptionRequest(userId, subRequest));
        User assignedUser = retrieveUser(userId);

        List<Subscription> subscriptions = subscriptionRequest.stream()
                .map(subscriptionMapper::toSubscription)
                .peek(sub -> sub.setUser(assignedUser))
                .toList();

        List<Subscription>  savedSubscriptions = subscriptionRepository.saveAll(subscriptions);

        savedSubscriptions.forEach(pubSubHubbubService::subscribe);

        return subscriptionMapper.toSubscriptionDto(savedSubscriptions);
    }

    @Override
    public void deleteSubscription(String userId, Long subscriptionId) {
        Optional<Subscription> foundSubscription = subscriptionRepository
                .findByUserIdAndSubscriptionId(userId, subscriptionId);

        if (foundSubscription.isPresent()) {
            pubSubHubbubService.unsubscribe(foundSubscription.get());
            subscriptionRepository.deleteById(subscriptionId);

        } else {
            throw new NotFoundException(NotFoundErrorCode.SUBSCRIPTION_ID_NOT_FOUND);
        }
    }

    @Override
    public SubscriptionDto patchSubscription(String userId, Long subscriptionId, SubscriptionRequest subscriptionRequest) {
        Optional<Subscription> foundSubscription = subscriptionRepository
                .findByUserIdAndSubscriptionId(userId, subscriptionId);

        if (foundSubscription.isPresent()) {
            Subscription subscription = foundSubscription.get();

            String oldTopic = subscription.getTopicUrl();
            String newTopic = subscriptionRequest.getTopicUrl();

            subscriptionMapper.updateSubscription(subscription, subscriptionRequest);
            var savedSubscription = subscriptionRepository.save(subscription);

            // Handle pubSubHubbub subscriptions if our topicUrl changed
            if (newTopic != null && !newTopic.equals(oldTopic)) {
                pubSubHubbubService.unsubscribe(userId, subscriptionId.toString(), oldTopic);
                pubSubHubbubService.subscribe(userId, subscriptionId.toString(), newTopic);
            }

            return subscriptionMapper.toSubscriptionDto(savedSubscription);
        } else {
            throw new NotFoundException(NotFoundErrorCode.SUBSCRIPTION_ID_NOT_FOUND);
        }
    }

    private void validateSubscriptionRequest(String userId, SubscriptionRequest subscriptionRequest) {
        String topicUrl = subscriptionRequest.getTopicUrl();

        if (subscriptionRequest.getName() == null) {
            throw new BadRequestException(BadRequestErrorCode.MISSING_NAME);
        } else if (topicUrl == null) {
            throw new BadRequestException(BadRequestErrorCode.MISSING_TOPIC_URL);
        } else if (subscriptionRepository.findByUserIdAndTopicUrl(userId, topicUrl).isPresent()) {
            throw new BadRequestException(BadRequestErrorCode.DUPLICATE_TOPIC_URL);
        }
    }

    private User retrieveUser(String userId) {
        Optional<User> foundUser = userRepository.findByUserId(userId);
        User assignedUser;

        if (foundUser.isEmpty()) {
            //  We're assuming the requester is a valid user querying themselves,
            //  so we're automatically generating a new one based on the given id.
            assignedUser = User.builder().userId(userId).build();
            this.userRepository.save(assignedUser);
        } else {
            assignedUser = foundUser.get();
        }

        return assignedUser;
    }
}

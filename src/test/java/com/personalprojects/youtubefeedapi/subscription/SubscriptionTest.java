package com.personalprojects.youtubefeedapi.subscription;

import com.personalprojects.youtubefeedapi.pubsubhubbub.services.IPubSubHubbubService;
import com.personalprojects.youtubefeedapi.user.IUserRepository;
import com.personalprojects.youtubefeedapi.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class SubscriptionTest {
    private static final String USER_ID = "user";

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISubscriptionMapper subscriptionMapper;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPubSubHubbubService pubSubHubbubService;


    private SubscriptionService service;

    @BeforeEach
    public void setup() {
        openMocks(this);
        this.service = new SubscriptionService(
            subscriptionRepository,
            subscriptionMapper,
            userRepository,
            pubSubHubbubService
        );
    }

    @Test
    void givenUserNotFound_whenCreate_thenCreateUser() {

        // user search comes up empty - should create new user with userId and save.
        var createdUser = User.builder().userId(USER_ID).build();
        doReturn(Optional.empty()).when(userRepository).findByUserId(USER_ID);
        doReturn(createdUser).when(userRepository).save(createdUser);

        // define request conversion
        var request = SubscriptionRequest.builder().name("name").topicUrl("url").build();
        var newSubscription = Subscription.builder()
                .subscriptionId(0L)
                .name("name")
                .topicUrl("url")
                .build();
        doReturn(newSubscription).when(subscriptionMapper).toSubscription(request);
        doReturn(newSubscription).when(subscriptionRepository).save(newSubscription);

        service.createSubscription(USER_ID, request);

        verify(userRepository, times(1)).save(eq(createdUser));
    }
}

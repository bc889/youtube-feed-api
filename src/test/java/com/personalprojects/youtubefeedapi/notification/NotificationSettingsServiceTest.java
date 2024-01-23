package com.personalprojects.youtubefeedapi.notification;

import com.personalprojects.youtubefeedapi.user.IUserRepository;
import com.personalprojects.youtubefeedapi.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class NotificationSettingsServiceTest {

    private static final String USER_ID = "user";

    @Mock
    private IUserRepository userRepository;

    @Mock
    private INotificationSettingsRepository notificationSettingsRepository;

    @Mock
    private INotificationSettingsMapper notificationSettingsMapper;


    private NotificationSettingsService service;

    @BeforeEach
    public void setup() {
        openMocks(this);
        this.service = new NotificationSettingsService(
                userRepository,
                notificationSettingsRepository,
                notificationSettingsMapper
        );
    }

    @Test
    void givenEnableNotificationsSetTrue_whenPatch_thenDoNotOverwriteExistingValueWhenNotSetOnRequest() {

        // initial user
        var testUser = buildNewUser();
        doReturn(Optional.of(testUser)).when(userRepository).findByUserId(USER_ID);

        // our settings start with enableNotifications set to true
        var initialSettings = NotificationSettings.builder()
                .notificationSettingsId(0)
                .user(testUser)
                .enableNotifications(true)
                .build();

        doReturn(Optional.of(initialSettings)).when(notificationSettingsRepository).findByUserId(USER_ID);
        doReturn(initialSettings).when(notificationSettingsRepository).save(initialSettings);

        // our request does not set enableNotifications
        var request = new NotificationSettingsRequest();
        doNothing().when(notificationSettingsMapper).updateNotificationSettings(initialSettings, request);

        // what our resulting dto should look like - enableNotifications should remain "true".
        var expectedSettingsDto = NotificationSettingsDto.builder()
                .notificationSettingsId(0L)
                .enableNotifications(true)
                .build();
        doReturn(expectedSettingsDto).when(notificationSettingsMapper).toNotificationSettingsDto(initialSettings);

        var actualSettingsDto = service.patchNotificationSettings(USER_ID, request);

        assertNotNull(expectedSettingsDto);
        assertNotNull(actualSettingsDto);
        assertEquals(expectedSettingsDto, actualSettingsDto);
    }

    @Test
    void givenUserDoesNotExist_whenPatch_thenCreateUserAndSettings() {

        // repositories will return empty when checked
        doReturn(Optional.empty()).when(userRepository).findByUserId(USER_ID);
        doReturn(Optional.empty()).when(notificationSettingsRepository).findByUserId(USER_ID);

        // should then create new user with userId and save.
        var createdUser = buildNewUser();
        doReturn(createdUser).when(userRepository).save(createdUser);

        // our request is mostly empty
        var request = new NotificationSettingsRequest();
        var savedNotification = buildNewSettings(createdUser);

        // converting our request and saving
        doReturn(savedNotification).when(notificationSettingsMapper).toNotificationSettings(request);
        doReturn(savedNotification).when(notificationSettingsRepository).save(savedNotification);

        service.patchNotificationSettings(USER_ID, request);

        verify(userRepository, times(1)).save(eq(createdUser));
        verify(notificationSettingsRepository, times(1)).save(eq(savedNotification));
    }

    @Test
    void givenUserExistsAndSettingsDoNot_whenPatch_thenCreateSettings() {
        var createdUser = buildNewUser();

        // user repo will get results, settings won't
        doReturn(Optional.of(createdUser)).when(userRepository).findByUserId(USER_ID);
        doReturn(Optional.empty()).when(notificationSettingsRepository).findByUserId(USER_ID);

        // our request is mostly empty
        var request = new NotificationSettingsRequest();
        var savedNotification = buildNewSettings(createdUser);

        // converting our request and saving
        doReturn(savedNotification).when(notificationSettingsMapper).toNotificationSettings(request);
        doReturn(savedNotification).when(notificationSettingsRepository).save(savedNotification);

        service.patchNotificationSettings(USER_ID, request);

        verify(userRepository, times(0)).save(eq(createdUser));
        verify(notificationSettingsRepository, times(1)).save(eq(savedNotification));
    }

    @Test
    void givenUserDoesNotExist_whenGet_thenCreateUserAndSettingsThenReturnEmptySettingsObj() {

        // repositories will return empty when checked
        doReturn(Optional.empty()).when(userRepository).findByUserId(USER_ID);
        doReturn(Optional.empty()).when(notificationSettingsRepository).findByUserId(USER_ID);

        // both a new user and settings obj will be generated
        var createdUser = buildNewUser();
        var newSettings = buildNewSettings(createdUser);
        var expectedDto = buildNewDto();

        doReturn(createdUser).when(userRepository).save(createdUser);
        doReturn(newSettings).when(notificationSettingsRepository).save(newSettings);
        doReturn(expectedDto).when(notificationSettingsMapper).toNotificationSettingsDto(newSettings);

        var actualDto = service.getByUserId(USER_ID);

        // new user and settings obj created.
        verify(userRepository, times(1)).save(eq(createdUser));
        verify(notificationSettingsRepository, times(1)).save(eq(newSettings));

        assertNotNull(expectedDto);
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    void givenUserExistsAndSettingsDoesNot_whenGet_thenCreateSettingsThenReturnEmptySettingsObj() {
        var createdUser = User.builder().userId(USER_ID).build();

        // only settings will return empty when checked
        doReturn(Optional.of(createdUser)).when(userRepository).findByUserId(USER_ID);
        doReturn(Optional.empty()).when(notificationSettingsRepository).findByUserId(USER_ID);

        // only a new settings obj will be generated
        var newSettings = buildNewSettings(createdUser);
        var expectedDto = buildNewDto();

        doReturn(createdUser).when(userRepository).save(createdUser);
        doReturn(newSettings).when(notificationSettingsRepository).save(newSettings);
        doReturn(expectedDto).when(notificationSettingsMapper).toNotificationSettingsDto(newSettings);

        var actualDto = service.getByUserId(USER_ID);

        // save not called for user, just for settings.
        verify(userRepository, times(0)).save(eq(createdUser));
        verify(notificationSettingsRepository, times(1)).save(eq(newSettings));

        assertNotNull(expectedDto);
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
    }

    User buildNewUser() {
        return User.builder()
                .userId(USER_ID)
                .build();
    }

    NotificationSettings buildNewSettings (User createdUser) {
        return NotificationSettings.builder()
                .notificationSettingsId(0L)
                .enableNotifications(false)
                .user(createdUser)
                .build();
    }

    NotificationSettingsDto buildNewDto() {
        return NotificationSettingsDto.builder()
                .notificationSettingsId(0L)
                .enableNotifications(false)
                .build();
    }
}

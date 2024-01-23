package com.personalprojects.youtubefeedapi.notification;

import com.personalprojects.youtubefeedapi.user.IUserRepository;
import com.personalprojects.youtubefeedapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationSettingsService implements INotificationSettingsService {

    private final IUserRepository userRepository;
    private final INotificationSettingsRepository notificationSettingsRepository;
    private final INotificationSettingsMapper notificationSettingsMapper;

    @Override
    public NotificationSettingsDto patchNotificationSettings(String userId, NotificationSettingsRequest request) {
        Optional<NotificationSettings> foundNotificationSettings = notificationSettingsRepository.findByUserId(userId);
        User assignedUser = retrieveAssignedUser(userId);
        NotificationSettingsDto nsDto;

        if (foundNotificationSettings.isEmpty()) {
            NotificationSettings notificationSettings = notificationSettingsMapper.toNotificationSettings(request);
            notificationSettings.setUser(assignedUser);
            nsDto = notificationSettingsMapper.toNotificationSettingsDto(
                    notificationSettingsRepository.save(notificationSettings)
            );
        }
        else {
            NotificationSettings newNotificationSettings = foundNotificationSettings.get();
            notificationSettingsMapper.updateNotificationSettings(newNotificationSettings, request);
            nsDto = notificationSettingsMapper.toNotificationSettingsDto(
                    notificationSettingsRepository.save(newNotificationSettings)
            );
        }

        return nsDto;
    }

    @Override
    public NotificationSettingsDto getByUserId(String userId) {
        Optional<NotificationSettings> foundNotificationSettings = notificationSettingsRepository.findByUserId(userId);

        if(foundNotificationSettings.isEmpty()) {
            User assignedUser = retrieveAssignedUser(userId);
            NotificationSettings notificationSettings = new NotificationSettings();
            notificationSettings.setUser(assignedUser);

            return notificationSettingsMapper.toNotificationSettingsDto(
                    notificationSettingsRepository.save(notificationSettings)
            );
        }

        return notificationSettingsMapper.toNotificationSettingsDto(foundNotificationSettings.get());
    }

    private User retrieveAssignedUser(String userId) {
        Optional<User> foundUser = userRepository.findByUserId(userId);
        return foundUser.orElseGet(() -> this.userRepository.save(User.builder().userId(userId).build()));
    }
}

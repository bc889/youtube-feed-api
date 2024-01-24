package com.personalprojects.youtubefeedapi.notificationsettings;


import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface INotificationSettingsMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNotificationSettings(@MappingTarget NotificationSettings entity, NotificationSettingsRequest request);

    NotificationSettings toNotificationSettings(NotificationSettingsRequest request);

    NotificationSettingsDto toNotificationSettingsDto(NotificationSettings settings);
}

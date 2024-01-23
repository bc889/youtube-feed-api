package com.personalprojects.youtubefeedapi.subscription;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ISubscriptionMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSubscription(@MappingTarget Subscription entity, SubscriptionRequest request);

    Subscription toSubscription(SubscriptionRequest subscriptionRequest);

    SubscriptionDto toSubscriptionDto(Subscription subscription);

    List<SubscriptionDto> toSubscriptionDto(List<Subscription> subscriptions);

    // TODO: Using kebab case for all entity ids, look into setting a broader config rule.
    @Mapping(source = "user.userId", target = "userId")
    UserSubscriptionDto toUserSubscriptionDto(Subscription subscription);

    List<UserSubscriptionDto> toUserSubscriptionDtoList(List<Subscription> subscription);
}

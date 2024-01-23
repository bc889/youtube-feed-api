package com.personalprojects.youtubefeedapi.subscription;

import com.personalprojects.youtubefeedapi.filter.Filter;
import com.personalprojects.youtubefeedapi.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subscriptionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String topicUrl;

    @Column(columnDefinition = "boolean default false")
    private boolean allUploadActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(targetEntity = Filter.class, mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Filter> filters;
}

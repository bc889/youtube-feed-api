package com.personalprojects.youtubefeedapi.filter;

import com.personalprojects.youtubefeedapi.subscription.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long filterId;


    private boolean recentOnly;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriptionId")
    private Subscription subscription;
}

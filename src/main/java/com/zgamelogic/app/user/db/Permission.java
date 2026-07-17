package com.zgamelogic.app.user.db;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissions")
public class Permission {
    @EmbeddedId
    @JsonUnwrapped
    @JsonView(Views.AuthView.class)
    private Permission.PermissionsKey permissionsKey;

    @MapsId("userId")
    @ManyToOne
    private UserData user;

    public enum PermissionType {
        ADMIN,
        MODERATOR
    }

    @Embeddable
    @EqualsAndHashCode
    public static class PermissionsKey {
        private long userId;
        @JsonView(Views.AuthView.class)
        private UUID resourceId;
        @Enumerated(EnumType.STRING)
        @JsonView(Views.AuthView.class)
        private PermissionType permission;
    }
}

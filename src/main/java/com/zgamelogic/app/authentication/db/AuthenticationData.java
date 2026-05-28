package com.zgamelogic.app.authentication.db;

import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.Views;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AuthenticationData.AuthenticationDataId.class)
@Table(schema = "minecraft_server_manager", name = "auth_data")
public class AuthenticationData {
    @Id
    @JsonView(Views.AuthView.class)
    private String msmToken;
    @Id
    @JsonView(Views.AuthView.class)
    private long discordId;
    @JsonView(Views.AuthView.class)
    private String discordUsername;
    private String discordToken;
    private String discordRefreshToken;
    private Instant discordTokenExpiration;
    @JsonView(Views.AuthView.class)
    private String discordAvatar;

    public static class AuthenticationDataId implements Serializable {
        private long discordId;
        private String msmToken;
    }
}

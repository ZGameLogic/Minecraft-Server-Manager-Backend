package com.zgamelogic.app.authentication.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AuthenticationData.AuthenticationDataId.class)
@Table(schema = "minecraft_server_manager", name = "auth_data")
public class AuthenticationData {
    @Id
    private String msmToken;
    @Id
    private long discordId;
    private String discordUsername;
    private String discordToken;
    private String discordRefreshToken;
    private Date discordTokenExpiration;

    public static class AuthenticationDataId implements Serializable {
        private long discordId;
        private String msmToken;
    }
}

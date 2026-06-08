package com.zgamelogic.app.authentication.db;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.Views;
import com.zgamelogic.app.user.db.UserData;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(schema = "minecraft_server_manager", name = "auth_data")
public class AuthenticationData {
    @Id
    @JsonView(Views.AuthViewCode.class)
    private String msmToken;
    private String discordToken;
    private String discordRefreshToken;
    private Instant discordTokenExpiration;

    @ManyToOne
    @JoinColumn(name = "discord_id")
    @JsonView(Views.AuthView.class)
    @JsonUnwrapped
    private UserData user;

    public AuthenticationData(String msmToken, String discordToken, String discordRefreshToken, Instant discordTokenExpiration, UserData user) {
        this.msmToken = msmToken;
        this.discordToken = discordToken;
        this.discordRefreshToken = discordRefreshToken;
        this.discordTokenExpiration = discordTokenExpiration;
        this.user = user;
    }
}

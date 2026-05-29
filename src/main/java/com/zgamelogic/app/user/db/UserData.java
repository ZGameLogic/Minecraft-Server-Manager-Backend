package com.zgamelogic.app.user.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.Views;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "users", schema = "minecraft_server_manager")
public class UserData {
    @Id
    @JsonView({Views.AuthViewCode.class, Views.AuthViewToken.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("discordId")
    private long id;
    @JsonView({Views.AuthViewCode.class, Views.AuthViewToken.class})
    @JsonProperty("discordUsername")
    private String username;
    @JsonView({Views.AuthViewCode.class, Views.AuthViewToken.class})
    @JsonProperty("discordAvatar")
    private String avatar;

    public UserData(long id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }
}

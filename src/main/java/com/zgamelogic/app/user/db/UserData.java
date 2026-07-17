package com.zgamelogic.app.user.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class UserData {
    @Id
    @JsonView(Views.AuthView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("discordId")
    private long id;
    @JsonView(Views.AuthView.class)
    @JsonProperty("discordUsername")
    private String username;
    @JsonView(Views.AuthView.class)
    @JsonProperty("discordAvatar")
    private String avatar;
    private boolean superAdmin;
    private boolean superModerator;
    @OneToMany(mappedBy = "user")
    @JsonView(Views.AuthView.class)
    private List<Permission> permissions;

    public UserData(long id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        superAdmin = false;
        superModerator = false;
    }
}

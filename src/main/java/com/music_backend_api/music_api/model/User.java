package com.music_backend_api.music_api.model;

import com.music_backend_api.music_api.roles.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Setter
    @Getter
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Setter
    @Getter
    private String avatar;


    @Getter
    @Column(nullable = false)
    private Boolean enabled = true;

    @Setter
    @Getter
    @Column(length = 100)
    private String fullname;

    @Setter
    @Getter
    @Column(length = 255)
    private String address;

    @Setter
    @Getter
    @Column(length = 10)
    private String phone;

    @Setter
    @Getter
    private Boolean gender;

    @Getter
    @Column(name = "created_at", insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Setter
    @Getter
    @Column(name = "last_login")
    private Timestamp lastLogin;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteSong> favoriteSongs = new ArrayList<>();


    // Constructors
    public User() {
    }

    public User(String username, String email, String password, Role role, String avatar,
                String fullname, String address, String phone, Boolean gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.enabled = true;
    }
}

package com.example.tendawaks.dto;


import com.example.tendawaks.model.Role;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@RequiredArgsConstructor
public class AppUserRequest {
    @NonNull
    private String password;

    @NonNull
    private String email;

//    @NonNull
//    private String location;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role;


    @Data
    @Builder
    @RequiredArgsConstructor
    public static class AppUserRequestLogin {
        @NonNull
        private String password;

        @NonNull
        private String userName;


    }

    @Data
    @RequiredArgsConstructor
    public static class resetPasswordRequest {
        @NonNull
        private String userName;

        @NonNull
        private String password;

    }

    @Data
    @RequiredArgsConstructor
    public static class forgotenPasswordRequest {
        @NonNull
        private String userName;
    }

}

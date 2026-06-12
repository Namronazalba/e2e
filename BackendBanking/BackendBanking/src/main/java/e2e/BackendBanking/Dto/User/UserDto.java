package e2e.BackendBanking.Dto.User;

import e2e.BackendBanking.Model.User.UserRole;

import java.time.LocalDateTime;

public class UserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private UserRole role;

    public UserDto(
            String id,
            String firstName,
            String lastName,
            String email,
            String username,
            String phone,
            String address,
            LocalDateTime createdAt,
            LocalDateTime lastLogin,
            UserRole role
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.role = role;
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public UserRole getRole() { return role; }
}
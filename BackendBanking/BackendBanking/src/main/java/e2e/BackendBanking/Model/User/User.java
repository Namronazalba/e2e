package e2e.BackendBanking.Model.User;
import e2e.BackendBanking.Model.User.UserRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String password;
    private String pinHash;
    private UserRole role = UserRole.USER;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private List<String> accountIds;


    // ================= CONSTRUCTOR =================
    public User() { }

    public User(String firstName,String lastName, String email, String username, String phone, String address, String password, String pin) {
        this.firstName = firstName; this.lastName = lastName; this.email = email; this.username = username; this.phone = phone; this.address = address; this.password = password; this.pinHash = pin;
    }

    // ================= GETTERS =================
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() {return email; }
    public String getUsername() { return username; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }
    public String getPin() { return pinHash; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public List<String> getAccounts() { return accountIds; }


    // ================= SETTERS =================
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPassword(String password) { this.password = password; }
    public void setPin(String pin) { this.pinHash = pin; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setRole(UserRole role) { this.role = role; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setAccounts(List<String> accounts) { this.accountIds = accounts; }
}
package xliic.openapi.example;

//import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Validated
public class User {

    @NotNull
    //@Schema(required = true, format="email") - Removed format for demo purpose
    @Schema(required = true)
    private String email;

    // Constraints can be configured through various annotations.
    @Size(min = 5, max = 100)
    @Schema(required = true)
    @Pattern(regexp = Constants.SIMPLE_STRING)
    @NotNull
    @Valid
    private String name;
    
    // Here we only specify the format, which will be then resolved through 42C Data Dictionary.
    @Schema(required = true, format = "password")
    private String password;

    // Illustrate how to specify an enum value.
    @Schema(description = "vip-level", allowableValues =  {"gold","silver","bronze"})
    @Valid
    private String vipLevel;

    public User(String email, String name, String password, String vipLevel) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.vipLevel = vipLevel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }
}

package xliic.openapi.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;


@RestController
@RequestMapping("/api/v1")
@EnableAutoConfiguration
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Demo API",
                version = "0.1",
                description = "Sample API for SpringBoot",
                license = @License(name = "Apache 2.0", url = "http://open.license"),
                contact = @Contact(url = "http://acme-server.com", name = "Support", email = "Support@acme-server.com")
        ),
        //USEFUL WHEN SECURITY SETUP IS GLOBAL
        //security = {
        //)        @SecurityRequirement(name = "apikey"),
        //        @SecurityRequirement(name = "oauth2", scopes = {"read", "write"})
        //},
        //
        servers = {
                @Server(
                        description = "local server",
                        url = "http://localhost:8090"),
                @Server(
                        description = "docker host",
                        url = "http://host.docker.internal:8090")

        }
)
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "apikey", in = SecuritySchemeIn.HEADER, paramName = "x-access-token")
@SecurityScheme(type = SecuritySchemeType.OAUTH2, name = "oauth2", flows = @OAuthFlows(
        clientCredentials =
                @OAuthFlow(tokenUrl = "http://acme.com/token",
                            scopes = {  @OAuthScope(description = "Write user", name = "write"),
                                        @OAuthScope(description = "Read User", name = "read")}))
)

public class UserMgtApp {

    private final UserService userService = new UserService();

    public static void main(String[] args) {
        SpringApplication.run(UserMgtApp.class, args);
    }

    // Add additionalProperties=false to all schemas.
    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> openApi.getComponents().getSchemas().values().forEach( s -> s.setAdditionalProperties(false));
    }

//    @Operation(operationId = "getLoginToken", summary = "login user with user/password", extensions = {
//            @Extension(properties = {@ExtensionProperty(name = "x-42c-no-authentication", value = "true")})},
//            responses = {
//                    @ApiResponse(description = "successful login", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = Token.class))),
//                    @ApiResponse(responseCode = "403", description = "login failed",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = ErrorResponse.class)))})
//    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void login (@RequestParam(value = "user") String user, @RequestParam(value = "password") String password) {
//         userService.login (user, password);
//    }

    @Operation(operationId = "GetUserProfile", summary = "Get User Details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Data Found",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "403", description = "UnAuthorized",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))})

    @GetMapping(value = "/user/{id}")
    @SecurityRequirement(name = "oauth2", scopes = {"read"})
    public User getUser (@PathVariable(name = "id") @Min (36) @Max (36) @Pattern(regexp = Constants.UUID) @Valid String id) {
        return userService.getUser(id);
    }

    @Operation(operationId = "UpdateUserProfile", summary = "Update User Details",
            responses = {
                    @ApiResponse(description = "Update Successful", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "login failed",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "apikey", scopes = {})
    @Validated (User.class)
    public User updateUser (@RequestBody final User user) {
        return userService.updateUser(user);
    }
}

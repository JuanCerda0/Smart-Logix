package smartlogix.BackendForFrontend.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import smartlogix.BackendForFrontend.shared.UnauthorizedException;

@Service
public class AuthService {

    private final String configuredUsername;
    private final String configuredPassword;
    private final JwtTokenService jwtTokenService;

    public AuthService(
            @Value("${smartlogix.auth.username}") String configuredUsername,
            @Value("${smartlogix.auth.password}") String configuredPassword,
            JwtTokenService jwtTokenService
    ) {
        this.configuredUsername = configuredUsername;
        this.configuredPassword = configuredPassword;
        this.jwtTokenService = jwtTokenService;
    }

    public LoginResponse login(LoginRequest request) {
        if (!configuredUsername.equals(request.username()) || !configuredPassword.equals(request.password())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        TokenData tokenData = jwtTokenService.generateToken(request.username());
        return new LoginResponse(tokenData.token(), "Bearer", tokenData.expiresIn());
    }
}

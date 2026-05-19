package smartlogix.BackendForFrontend.auth;

public record TokenData(
        String token,
        long expiresIn
) {
}

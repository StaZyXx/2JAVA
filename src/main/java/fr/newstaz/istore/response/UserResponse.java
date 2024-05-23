package fr.newstaz.istore.response;

public record UserResponse(boolean success, String message) {

    public record CreateUserResponse(boolean success, String message) {
    }

    public record EditUserResponse(boolean success, String message) {
    }

    public record DeleteUserResponse(boolean success, String message) {
    }
}

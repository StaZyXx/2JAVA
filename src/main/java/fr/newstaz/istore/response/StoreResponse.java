package fr.newstaz.istore.response;

public record StoreResponse(boolean success, String message) {

    public record CreateStoreResponse(boolean success, String message) {

    }

    public record AddEmployeeResponse(boolean success, String message) {

    }

    public record RemoveEmployeeResponse(boolean success, String message) {

    }

    public record CreateInventoryItemResponse(boolean success, String message) {

    }

    public record UpdateInventoryItemResponse(boolean success, String message) {

    }

    public record DeleteInventoryItemResponse(boolean success, String message) {

    }

    public record AddPermissionResponse(boolean success, String message) {

    }

    public record RemovePermissionResponse(boolean success, String message) {

    }


}

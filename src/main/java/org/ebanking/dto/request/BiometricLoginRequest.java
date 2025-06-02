package org.ebanking.dto.request;

public class BiometricLoginRequest {
    private String email;
    private String credentialId;
    private String authenticatorData;
    private String clientDataJSON;
    private String signature;
    private String role;
    private LoginRequest userReceived;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public String getAuthenticatorData() {
        return authenticatorData;
    }

    public String getClientDataJSON() {
        return clientDataJSON;
    }

    public String getSignature() {
        return signature;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public void setAuthenticatorData(String authenticatorData) {
        this.authenticatorData = authenticatorData;
    }

    public void setClientDataJSON(String clientDataJSON) {
        this.clientDataJSON = clientDataJSON;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

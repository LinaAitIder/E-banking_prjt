package org.ebanking.dto.response;

public class AuthenticatorAssertionResponse {
    private final byte[] authenticatorData;
    private final byte[] clientDataJSON;
    private final byte[] signature;
    private final byte[] userHandle;

    public AuthenticatorAssertionResponse(byte[] authenticatorData,
                                          byte[] clientDataJSON,
                                          byte[] signature,
                                          byte[] userHandle) {
        this.authenticatorData = authenticatorData;
        this.clientDataJSON = clientDataJSON;
        this.signature = signature;
        this.userHandle = userHandle;
    }

    public byte[] getAuthenticatorData() {
        return authenticatorData;
    }

    public byte[] getClientDataJSON() {
        return clientDataJSON;
    }

    public byte[] getSignature() {
        return signature;
    }

    public byte[] getUserHandle() {
        return userHandle;
    }
}
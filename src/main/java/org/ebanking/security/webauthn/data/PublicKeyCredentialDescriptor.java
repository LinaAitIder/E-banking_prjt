package org.ebanking.security.webauthn.data;

import com.webauthn4j.data.PublicKeyCredentialType;
import java.util.Set;

public class PublicKeyCredentialDescriptor {
    private final PublicKeyCredentialType type;
    private final byte[] id;
    private final Set<String> transports; // ou Set<AuthenticatorTransport>

    public PublicKeyCredentialDescriptor(PublicKeyCredentialType type,
                                         byte[] id,
                                         Set<String> transports) {
        this.type = type;
        this.id = id;
        this.transports = transports;
    }

    public PublicKeyCredentialType getType() {
        return type;
    }

    public byte[] getId() {
        return id;
    }

    public Set<String> getTransports() {
        return transports;
    }
}
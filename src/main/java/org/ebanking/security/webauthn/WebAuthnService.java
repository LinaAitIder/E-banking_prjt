package org.ebanking.security.webauthn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webauthn4j.*;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.statement.*;
import com.webauthn4j.data.client.CollectedClientData;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.*;
import com.webauthn4j.server.*;
import com.webauthn4j.util.Base64Util;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.WebAuthnCredentialRepository;
import org.ebanking.model.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional
public class WebAuthnService {

    private final String rpId = "localhost";
    private final ObjectConverter objectConverter = new ObjectConverter();
    private final WebAuthnCredentialRepository credentialRepository;
    private final ClientRepository clientRepository;

    public WebAuthnService(WebAuthnCredentialRepository credentialRepository, ClientRepository clientRepository) {
        this.credentialRepository = credentialRepository;
        this.clientRepository = clientRepository;
    }

    public PublicKeyCredentialCreationOptions generateRegistrationOptions(Client client) {
        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);

        return new PublicKeyCredentialCreationOptions(
                new PublicKeyCredentialRpEntity(rpId, "E-Banking"),
                new PublicKeyCredentialUserEntity(
                        client.getNationalId().getBytes(),
                        client.getEmail(),
                        client.getFullName()
                ),
                new DefaultChallenge(challenge),
                Collections.singletonList(
                        new PublicKeyCredentialParameters(
                                PublicKeyCredentialType.PUBLIC_KEY,
                                COSEAlgorithmIdentifier.ES256
                        )
                )
        );
    }

    public void verifyRegistration(String attestationObject, Client client) {
        try {
            // 1. Convertir en objets WebAuthn4j
            AttestationObject attestationObj;
            try {
                byte[] decodedAttestation = Base64.getUrlDecoder().decode(attestationObject);
                attestationObj = objectConverter.getCborConverter()
                        .readValue(decodedAttestation, AttestationObject.class);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to parse attestationObject", e);
            }
            System.out.println("Received attestationObject: " + attestationObject);
            System.out.println("Received Client: " + client);

//            CollectedClientData clientData = objectConverter.getJsonConverter()
//                    .readValue(new String(client.getChallenge().getBytes(), StandardCharsets.UTF_8), CollectedClientData.class);

            // 2. Validation basique
            if (attestationObj == null) {
                throw new RuntimeException("Invalid attestation data");
            } else {
                attestationObj.getAuthenticatorData();
            }

            AttestedCredentialData attestedData = attestationObj.getAuthenticatorData()
                    .getAttestedCredentialData();

            if (attestedData == null) {
                throw new RuntimeException("No attested credential data");
            }

            // 3. Extraction des données
            String credentialId = Base64Util.encodeToString(attestedData.getCredentialId());
            byte[] publicKey = objectConverter.getCborConverter()
                    .writeValueAsBytes(attestedData.getCOSEKey());

            // 4. Enregistrement
            System.out.println("the client "+ client);
            System.out.println("credentialId "+ credentialId);
            System.out.println("the client "+ publicKey);

            saveCredentials(client, credentialId, publicKey);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("WebAuthn verification failed", e);
        }
    }

    public PublicKeyCredentialRequestOptions generateAuthenticationOptions(Client client) {
        String credentialId = client.getWebAuthnCredentials().iterator().next().getCredentialId();
        PublicKeyCredentialDescriptor descriptor = new PublicKeyCredentialDescriptor(
                PublicKeyCredentialType.PUBLIC_KEY,
                credentialId.getBytes(),
                null);

        return new PublicKeyCredentialRequestOptions(
                new DefaultChallenge(new SecureRandom().generateSeed(32)),
                120000L,
                rpId,
                Collections.singletonList(descriptor),
                UserVerificationRequirement.PREFERRED,
                null); // Pas d'extensions
    }

    public boolean verifyAuthentication(String assertion, Client client) {
        try {
            // 1. Parsing
            AuthenticatorAssertionResponse assertionResponse = parseAssertion(assertion);

            // 2. Calcul du hash attendu
            byte[] expectedRpIdHash = MessageDigest.getInstance("SHA-256")
                    .digest(rpId.getBytes(StandardCharsets.UTF_8));

            // 3. Validation du RP ID Hash (premiers 32 bytes)
            byte[] authDataRpIdHash = Arrays.copyOfRange(
                    assertionResponse.getAuthenticatorData(),
                    0,
                    32
            );

            if (!Arrays.equals(expectedRpIdHash, authDataRpIdHash)) {
                return false;
            }

            // 4. Validation supplémentaire (à compléter)
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Authentication verification failed", e);
        }
    }

    private AuthenticatorAssertionResponse parseAssertion(String assertion) {
        try {
            // Implémentation avec parsing JSON (jackson)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(assertion);

            return new AuthenticatorAssertionResponse(
                    Base64Util.decode(node.get("authenticatorData").asText()),
                    Base64Util.decode(node.get("clientDataJSON").asText()),
                    Base64Util.decode(node.get("signature").asText()),
                    node.has("userHandle") ? Base64Util.decode(node.get("userHandle").asText()) : null
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse assertion", e);
        }
    }

    private void saveCredentials(Client client, String credentialId, byte[] publicKey) {
        WebAuthnCredential credential = new WebAuthnCredential();
        credential.setCredentialId(credentialId);
        credential.setPublicKey(publicKey);
        credential.setUser(client);
        credentialRepository.save(credential);
    }

    public String prepareWebAuthnRegistration(Client client) {

        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
        String challengeBase64 = Base64.getUrlEncoder().encodeToString(challenge);
        client.setChallenge(challengeBase64);
        return challengeBase64;
    }
}
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
import org.ebanking.dao.UserRepository;
import org.ebanking.dao.WebAuthnCredentialRepository;
import org.ebanking.dto.response.AuthResponse;
import org.ebanking.model.*;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import com.upokecenter.cbor.CBORObject;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class WebAuthnService {

    private final UserRepository userRepository;
    private final String rpId = "localhost";
    private final ObjectConverter objectConverter = new ObjectConverter();
    private final WebAuthnCredentialRepository credentialRepository;
    private final ClientRepository clientRepository;

    public WebAuthnService(WebAuthnCredentialRepository credentialRepository,
                           ClientRepository clientRepository,
                           UserRepository userRepository) {
        this.credentialRepository = credentialRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public PublicKeyCredentialCreationOptions generateRegistrationOptions(User user) {
        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);

        return new PublicKeyCredentialCreationOptions(
                new PublicKeyCredentialRpEntity(rpId, "E-Banking"),
                new PublicKeyCredentialUserEntity(
                        user.getEmail().getBytes(),
                        user.getEmail(),
                        user.getFullName()),
                new DefaultChallenge(challenge),
                Collections.singletonList(
                        new PublicKeyCredentialParameters(
                                PublicKeyCredentialType.PUBLIC_KEY,
                                COSEAlgorithmIdentifier.ES256)));
    }

    public void verifyRegistration(String attestationObject, User user) {
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
            System.out.println("Received Client: " + user);

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
            String credentialId = Base64.getUrlEncoder().withoutPadding().encodeToString(attestedData.getCredentialId());
            byte[] publicKey = objectConverter.getCborConverter()
                    .writeValueAsBytes(attestedData.getCOSEKey());

            // 4. Enregistrement
            System.out.println("the client "+ user);
            System.out.println("credentialId "+ credentialId);
            System.out.println("the client "+ publicKey);
            System.out.println("🟢 Registered credentialId: " + credentialId);
            System.out.println("🟢 Raw bytes: " + Arrays.toString(attestedData.getCredentialId()));


            saveCredentials(user, credentialId, publicKey);

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

    private void saveCredentials(User user, String credentialId, byte[] publicKey) {
        System.out.println("UserId : " + user.getId());
        if (user.getId() == null) {
            user = userRepository.save(user);
        }
        if (user.getPhone() == null) {
            throw new IllegalArgumentException("User phone cannot be null");
        }
        WebAuthnCredential credential = new WebAuthnCredential();
        credential.setCredentialId(credentialId);
        credential.setPublicKey(publicKey);
        credential.setUser(user);
        credentialRepository.save(credential);
    }

    public String prepareWebAuthnRegistration(User user) {

        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
        String challengeBase64 = Base64.getUrlEncoder().encodeToString(challenge);
        user.setChallenge(challengeBase64);
        return challengeBase64;
    }

    public AuthResponse prepareLoginChallenge(User user) {
        String challenge = generateRandomChallenge();

        String role = user.getRole();

        List<String> allowedCredentials = credentialRepository
                .findByUserId(user.getId())
                .stream()
                .map(WebAuthnCredential::getCredentialId)
                .collect(Collectors.toList());

        return new AuthResponse(challenge, allowedCredentials, null , role);
    }

    public User verifyBiometricAuthentication(
            String email,
            String credentialId,
            String authenticatorData,
            String clientDataJSON,
            String signature) {

        // 1. Vérifier l'utilisateur
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        // 2. Vérifier le credential
        WebAuthnCredential credential = credentialRepository
                .findByCredentialId(credentialId)
                .orElseThrow(() -> new BadCredentialsException("Invalid credential"));

        System.out.println("🔵 Received login credentialId: " + credentialId);
        System.out.println("credentials"+ credential);

        // 3. Vérifier la signature
        boolean isValid = verifySignature(
                authenticatorData,
                clientDataJSON,
                signature,
                credential.getPublicKey()
        );

        if (!isValid) {
            throw new BadCredentialsException("Invalid biometric signature");
        }

        return user;
    }

    private String generateRandomChallenge() {
        byte[] array = new byte[32];
        new SecureRandom().nextBytes(array);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(array);
    }

    private boolean verifySignature(
            String authenticatorData,
            String clientDataJSON,
            String signature,
            byte[] publicKey) {

        try {
            // 1. Convertir les donnees Base64
            byte[] authData = Base64.getUrlDecoder().decode(authenticatorData);
            byte[] clientData = Base64.getUrlDecoder().decode(clientDataJSON);
            byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);

            // 2. Calculer le hash des donnees client
            byte[] clientDataHash = MessageDigest.getInstance("SHA-256")
                    .digest(clientData);

            // 3. Préparer les données à vérifier
            ByteArrayOutputStream dataToVerify = new ByteArrayOutputStream();
            dataToVerify.write(authData);
            dataToVerify.write(clientDataHash);

            // 4. Vérification avec la cle publique (ici ECDSA)
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            PublicKey pubKey = convertCOSEPublicKey(publicKey); // Méthode à implémenter
            ecdsaVerify.initVerify(pubKey);
            ecdsaVerify.update(dataToVerify.toByteArray());

            return ecdsaVerify.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private PublicKey getPublicKeyFromBytes(byte[] publicKeyBytes) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("EC");
        EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return kf.generatePublic(keySpec);
    }

    public PublicKey convertCOSEPublicKey(byte[] cosePublicKey) throws Exception {
        CBORObject cbor = CBORObject.DecodeFromBytes(cosePublicKey);

        byte[] xBytes = cbor.get(-2).GetByteString();
        byte[] yBytes = cbor.get(-3).GetByteString();

        ECPoint ecPoint = new ECPoint(
                new BigInteger(1, xBytes),
                new BigInteger(1, yBytes)
        );

        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
        parameters.init(new ECGenParameterSpec("secp256r1"));
        ECParameterSpec ecSpec = parameters.getParameterSpec(ECParameterSpec.class);

        ECPublicKeySpec pubSpec = new ECPublicKeySpec(ecPoint, ecSpec);
        KeyFactory kf = KeyFactory.getInstance("EC");

        return kf.generatePublic(pubSpec);
    }
}
import { Injectable } from '@angular/core';
import {User} from "../model/user.model";
import {AuthenService} from "./authen.service";
import {firstValueFrom} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebauthnService {

  constructor(private authenService:AuthenService) { }

  // Asynchrounous since we are waiing for the user to enter its biometric credentials
  async registerWebAuthenCredentials(
      challenge: any,
      user: { email: string; fullName: string; [key: string]: any },
      role: string
  ): Promise<boolean> {
    const publicKey: PublicKeyCredentialCreationOptions = {
      challenge,
      rp: {
        name: "E-banking Application"
      },
      user: {
        id: new TextEncoder().encode(user.email),
        name: user.email,
        displayName: user.fullName
      },
      pubKeyCredParams: [{
        type: "public-key",
        alg: -7
      }],
      authenticatorSelection: {
        authenticatorAttachment: "platform",
        userVerification: "required"
      },
      timeout: 60000,
      attestation: "direct"
    };

    try {
      const credential = await navigator.credentials.create({ publicKey }) as PublicKeyCredential;
      const serializedCredentials = this.serializeCredential(credential);
      console.log("this is the user sent ", user)
      console.log("this is the credential sent :",serializedCredentials);
      const verificationResult = await this.verifyAuthenticator(serializedCredentials.response.attestationObject, user);
      console.log(verificationResult);
      console.log("Biometric Registration succeeded!");
        return true;
    } catch (error) {
      console.error("Biometric Registration failed:", error);
      window.alert("Biometric Registration FAILED!");
      return false;
    }
  }

  async verifyAuthenticator(attestationObject: any, user: { email: string; fullName: string; [key: string]: any }): Promise<any> {
    return firstValueFrom(this.authenService.verifyAuthenticatorRequest(attestationObject, user));
  }

  async login(challenge: ArrayBuffer, user: User, allowedCredentialId: ArrayBuffer) {
    const publicKey: PublicKeyCredentialRequestOptions = {
      challenge: challenge,
      allowCredentials: [{
        id: new Uint8Array(allowedCredentialId),
        type: "public-key"
      }],
      userVerification: "required",
      timeout: 60000
    };

    try {
      const credential = await navigator.credentials.get({ publicKey }) as PublicKeyCredential;
      //sending Key to backend
      this.authenService.loginUser(user, credential).subscribe({
        next: (res) => {
          console.log("Logged in ");
          return;
        }
      })
    } catch (err) {
      console.error("Authentication failed:", err);
      throw err;
    }
  }

  private serializeCredential(credential: PublicKeyCredential): any {
    return {
      id: credential.id,
      rawId: this.arrayBufferToBase64(credential.rawId),
      type: credential.type,
      response: {
        attestationObject: this.arrayBufferToBase64((credential.response as AuthenticatorAttestationResponse).attestationObject),
        clientDataJSON: this.arrayBufferToBase64((credential.response as AuthenticatorAttestationResponse).clientDataJSON)
      }
    };
  }

  private arrayBufferToBase64(buffer: ArrayBuffer): string {
      const binary = String.fromCharCode(...new Uint8Array(buffer));
      const base64 = btoa(binary);
      return base64.replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
  }




}

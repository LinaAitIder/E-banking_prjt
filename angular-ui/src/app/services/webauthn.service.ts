import { Injectable } from '@angular/core';
import {User} from "../model/user.model";
import {AuthenService} from "./authen.service";

@Injectable({
  providedIn: 'root'
})
export class WebauthnService {

  constructor(private authenService:AuthenService) { }

  // Asynchrounous since we are waiing for the user to enter its biometric credentials
  async registerWebAuthenCredentials( challenge: any, user: { email: string; fullName: string; [key: string]: any }, role: string) {
    const publicKey: PublicKeyCredentialCreationOptions = {
      challenge: challenge,
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
      console.log("Registration successful!", credential);
      this.authenService.saveCredentials(this.serializeCredential(credential), challenge, role).subscribe({
        next: res=>{
          console.log("Saved Client")
        }
      })
    } catch (err) {
      console.error("Registration failed:", err);
      throw err;
    }

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
    return btoa(String.fromCharCode(...new Uint8Array(buffer)));
  }


}

import { Injectable } from '@angular/core';
import {User} from "../model/user.model";
import {AuthenService} from "./authen.service";
import {firstValueFrom} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class WebauthnService {

  constructor(private authenService:AuthenService, private router:Router) { }

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

  async login(challenge: any, userRole :string , user: User, allowedCredentialId: any) {

    console.log("allowedCredentialId:", allowedCredentialId);
    console.log("Type of allowedCredentialId:", typeof allowedCredentialId);
    console.log("Is array:", Array.isArray(allowedCredentialId));

    if (Array.isArray(allowedCredentialId)) {
      allowedCredentialId.forEach((id, index) => {
        console.log(`allowedCredentialId[${index}] =`, id);
        console.log(`Type: ${typeof id}`);
      });
    }


    const publicKey: PublicKeyCredentialRequestOptions = {
      challenge:  challenge,
      allowCredentials: allowedCredentialId.map((id: ArrayBuffer) => ({
        id,
        type: "public-key",
        transports: ['internal']
      })),
      userVerification: "required",
      timeout: 60000
    };

    console.log("chanleenge :", challenge);
    console.log('the receive allowedCredentails :',  allowedCredentialId);
    console.log("allowedCredentialId instanceof ArrayBuffer:", allowedCredentialId instanceof ArrayBuffer);
    console.log("allowedCredentialId byteLength:", allowedCredentialId.byteLength);

    try {
      const credential = await navigator.credentials.get({ publicKey }) as PublicKeyCredential;
      //sending Key to backend
      const loginPayload = {
        email: user.email,
        credentialId: this.arrayBufferToBase64(credential.rawId),
        authenticatorData: this.arrayBufferToBase64((credential.response as AuthenticatorAssertionResponse).authenticatorData),
        clientDataJSON: this.arrayBufferToBase64((credential.response as AuthenticatorAssertionResponse).clientDataJSON),
        signature: this.arrayBufferToBase64((credential.response as AuthenticatorAssertionResponse).signature),
        userReceived: user,
        role : userRole,
      };

      this.authenService.loginUser(loginPayload).subscribe({
        next: (res) => {
          console.log("received :"+ res);
          const token = res.token;
          const webAuthnEnabled = res.webAuthnEnabled;
          const role = res.role;
          const user = res.user;
          console.log("Logged in ");
          console.log("token", token);
          console.log(user.role);
          if(!token){
            console.log("not token received");
          }
          localStorage.setItem('userData', JSON.stringify(user));
          localStorage.setItem('role', role);
          localStorage.setItem('token', token);
          console.log(role);
          if(user.role === "CLIENT"){
            this.router.navigate(['client']);
          } else if(user.role === "AGENT"){
            this.router.navigate(['agent']);
          } else if(user.role === "ADMIN"){
            this.router.navigate(['admin']);

          }

        }
      })
    } catch (err) {
      if (err instanceof DOMException && err.name === "NotAllowedError") {
        alert("Authentication timed out or was cancelled. Please try again.");
      }
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


  private  base64ToArrayBuffer(base64: string): ArrayBuffer {
    base64 = base64.replace(/-/g, '+').replace(/_/g, '/');
    const padLength = (4 - (base64.length % 4)) % 4;
    base64 += '='.repeat(padLength);
    const binaryString = atob(base64);
    const bytes = new Uint8Array(binaryString.length);
    for (let i = 0; i < binaryString.length; i++) {
      bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes.buffer;
  }


}

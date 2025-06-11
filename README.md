# 💳 E-Banking Web Application

This project is a prototype for a secure and modern e-banking web application, integrating biometric authentication, SMS verification, and AI-powered assistance.

## 🚀 Features

- **Biometric Authentication** using WebAuthn (windows Hello, FingerPrint scan, flexible depending on the machine)
- **SMS Verification** through Infobip API
- **AI Assistant** powered by DialogFlow for user support (ongoing - waiting to resolve merge conflicts for this part)
- **Dockerized** backend and frontend for easier deployment
- **Hash-based Routing** for frontend navigation (due to deployment constraints)

---

## 🛠️ Technologies Used

- **Frontend:** Angular , WebAuthn
- **Backend:** Spring Framework, Spring Security
- **APIs:** Infobip for SMS, DialogFlow for chatbot, WebAuthn for biometric Authentication
- **Authentication:** 2FA Authentication via Biometrics + SMS Verification (When Biometric fails)
- **Deployment:** Docker, Render.com

---

## ⚠️ Known Limitations
- CORS issues occurred during Render deployment, affecting frontend-backend communication. These issues are absent in local Docker deployments. Troubleshooting is ongoing.
- Nginx routing served the default welcome page due to hash-based routing conflicts. Workaround implemented using /#/home to load frontend correctly
  
---

## Videos and screenshoors

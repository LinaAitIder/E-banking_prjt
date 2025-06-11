import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthenService } from '../services/authen.service';
import {environment} from "../../environments/environment.prod";

export const JwtInterceptor: HttpInterceptorFn = (request, next) => {
    const authService = inject(AuthenService);
    const apiUrl = environment.apiUrl;


    const excludedEndpoints = [
        `${apiUrl}/auth/login`,
        `${apiUrl}/auth/register`,
        `${apiUrl}/auth/register/agent`,
        `${apiUrl}/auth/register/admin`,
        `${apiUrl}/auth/verification/password`,
        `${apiUrl}/auth/request-sms-verification`
    ];

    console.log('Intercepting:', request.url); // Debug log

    const isExcluded = excludedEndpoints.some(endpoint => request.url.startsWith(endpoint));
    if (isExcluded) {
        console.log('Skipping JWT for:', request.url);
        return next(request);
    }

    const token = authService.getToken();
    if (token) {
        request = request.clone({
            setHeaders: { Authorization: `Bearer ${token}` }
        });
    }

    return next(request);
};

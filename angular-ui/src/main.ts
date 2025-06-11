
import { bootstrapApplication } from '@angular/platform-browser';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {AppComponent} from './app/app.component';
import {provideRouter, withHashLocation} from "@angular/router";
import {routes} from "./app/app.routes";
import {JwtInterceptor} from "./app/utils/jwt.interceptor";
import {HashLocationStrategy, LocationStrategy} from "@angular/common";

bootstrapApplication(AppComponent, {
    providers: [
        provideRouter(routes, withHashLocation()),
        provideHttpClient(withInterceptors([JwtInterceptor])),
    ],
});
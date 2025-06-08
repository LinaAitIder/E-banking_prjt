
import { bootstrapApplication } from '@angular/platform-browser';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {AppComponent} from './app/app.component';
import {provideRouter} from "@angular/router";
import {routes} from "./app/app.routes";
import {JwtInterceptor} from "./app/utils/jwt.interceptor";


bootstrapApplication(AppComponent, {
  providers: [
      provideHttpClient(
      withInterceptors([
        JwtInterceptor
      ])
  ),provideHttpClient(), provideRouter(routes)],
});
import { ReactiveFormsModule } from '@angular/forms';
import {AppComponent} from "./app.component";
import {NgModule} from "@angular/core";
import {ClientRegistrationComponent} from "./registration/client-registration/client-registration.component";
import {BrowserModule} from "@angular/platform-browser";
import {RouterModule} from "@angular/router";
import {routes} from "./app.routes";
import {JwtInterceptor} from "./utils/jwt.interceptor";

import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptors} from "@angular/common/http";
import {CommonModule} from "@angular/common";


@NgModule({
    declarations: [
    ],
    imports: [
        ReactiveFormsModule,
        BrowserModule,
        ReactiveFormsModule,
        ClientRegistrationComponent,
        AppComponent,

        CommonModule,
        ClientRegistrationComponent,
        RouterModule.forRoot(routes)
    ],
    providers: [

        provideHttpClient(withInterceptors([JwtInterceptor]))
    ],
})
export class AppModule { }

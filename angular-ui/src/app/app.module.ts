import { ReactiveFormsModule } from '@angular/forms';
import {AppComponent} from "./app.component";
import {NgModule} from "@angular/core";
import {ClientRegistrationComponent} from "./authentification/client-registration.component";
import {BrowserModule} from "@angular/platform-browser";


@NgModule({
    declarations: [
    ],
    imports: [
        ReactiveFormsModule,
        BrowserModule,
        ReactiveFormsModule,
        ClientRegistrationComponent,
        AppComponent,
        ClientRegistrationComponent
    ],
    providers: [],
})
export class AppModule { }

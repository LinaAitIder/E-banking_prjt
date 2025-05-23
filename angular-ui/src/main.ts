import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { appConfig } from './app/app.config';
import { TestConnectionComponent } from './app/test-connection/test-connection.component';
import {AppComponent} from './app/app.component';
import {provideRouter} from "@angular/router";
import {routes} from "./app/app.routes";

// bootstrapApplication(TestConnectionComponent, appConfig)
//     .catch(err => console.error(err));

bootstrapApplication(AppComponent, {
    providers: [provideHttpClient(), provideRouter(routes)],
}).catch(err => console.error(err));

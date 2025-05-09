import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { appConfig } from './app/app.config';
import { TestConnectionComponent } from './app/test-connection/test-connection.component';
import {AppComponent} from './app/app.component';

bootstrapApplication(TestConnectionComponent, appConfig)
  .catch(err => console.error(err));

bootstrapApplication(AppComponent, {
  providers: [provideHttpClient()],
}).catch(err => console.error(err));

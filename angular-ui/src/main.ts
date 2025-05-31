import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, withComponentInputBinding, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import {EspaceAdminComponent} from "./app/Espace-admin/Espace-admin.component";
import {EspaceClientComponent} from "./app/Espace-client/Espace-client.component";
import {EspaceAgentComponent} from "./app/Espace-agent/Espace-agent.component"
import {HomeComponent} from "./app/home/home.component";
import { provideHttpClient } from '@angular/common/http';
import { appConfig } from './app/app.config';
import { TestConnectionComponent } from './app/test-connection/test-connection.component';
import {AppComponent} from './app/app.component';
import {provideRouter} from "@angular/router";
import {routes} from "./app/app.routes";

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'admin',
    component: EspaceAdminComponent,
    loadChildren: () => import('./app/Espace-admin/Espace-admin.routes').then(m => m.ADMIN_ROUTES)
  },
  {
    path: 'client',
    component: EspaceClientComponent,
    loadChildren: () => import('./app/Espace-client/Espace-client.routes').then(m => m.CLIENT_ROUTES)
  },

  {
    path: 'agent',
    component: EspaceAgentComponent,
    loadChildren: () => import('./app/Espace-agent/Espace-agent.routes').then(m => m.AGENT_ROUTES)
  },
  { path: '', redirectTo: 'admin', pathMatch: 'full' },
  { path: '**', redirectTo: 'admin' } // Redirection pour les routes inconnues
];

// bootstrapApplication(TestConnectionComponent, appConfig)
//     .catch(err => console.error(err));


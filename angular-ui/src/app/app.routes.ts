import {RouterModule, Routes} from '@angular/router';
import {LoginFormComponent} from "./authentification/login-form.component";
import {ClientRegistrationComponent} from "./registration/client-registration/client-registration.component";
import {AppComponent} from "./app.component";
import {SmsVerificationComponent} from "./authentification/sms-verification.component";
import {HomeComponent} from "./home/home.component";
import {EspaceAdminComponent} from "./Espace-admin/Espace-admin.component";
import {EspaceClientComponent} from "./Espace-client/Espace-client.component";
import {EspaceAgentComponent} from "./Espace-agent/Espace-agent.component";
import { AdminLoginComponent } from './registration/admin-login/admin-login.component';
import {AgentRegistrationComponent} from "./registration/agent-registration/agent-registration.component";
import {RegistrationTypeComponent} from "./registration/registration-type.component";
import {NgModule} from "@angular/core";

export const routes: Routes = [
    {
        path : 'login',
        component : LoginFormComponent
    }, {
        path: 'registration',
        component: RegistrationTypeComponent,
    }, {
        path: 'clientRegistration',
        component: ClientRegistrationComponent
    }, {
        path: 'agentRegistration',
        component: AgentRegistrationComponent
    },{
        path: 'smsVerificationPage',
        component: SmsVerificationComponent
    }, {
        path: 'home',
        component: HomeComponent,
    },
    {
        path: 'admin',
        component: EspaceAdminComponent,
        loadChildren: () => import('./Espace-admin/Espace-admin.routes').then(m => m.ADMIN_ROUTES)
    },
    {
          path: 'admin/login',
          component: AdminLoginComponent
    },
    {
        path: 'client',
        component: EspaceClientComponent,
        loadChildren: () => import('./Espace-client/Espace-client.routes').then(m => m.CLIENT_ROUTES)
    },

    {
        path: 'agent',
        component: EspaceAgentComponent,
        loadChildren: () => import('./Espace-agent/Espace-agent.routes').then(m => m.AGENT_ROUTES)
    },
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: '**', redirectTo: 'home' }

];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule],
})
export class AppRoutingModule {}

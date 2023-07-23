import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {NgOptimizedImage} from "@angular/common";
import {WelcomeScreenComponent} from './components/welcome-screen/welcome-screen.component';
import {AppRoutingModule} from './app-routing.module';
import {HomeScreenComponent} from './components/home-screen/home-screen.component';
import {HttpClientModule} from '@angular/common/http';
import {SignupComponent} from "./components/signup/signup.component";
import {FormsModule} from '@angular/forms';
import {LoginComponent} from './components/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    WelcomeScreenComponent,
    HomeScreenComponent,
    SignupComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    NgOptimizedImage,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}

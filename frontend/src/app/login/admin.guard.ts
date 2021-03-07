import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';


@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {

  private readonly TOKEN_KEY = 'token';
  private readonly ROLE_KEY = 'role';
  private readonly ROLE_ADMIN = 'ADMIN';
  private readonly ALERT_MESSAGE = 'Admin access only!';

    constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (localStorage.getItem(this.TOKEN_KEY) && localStorage.getItem(this.ROLE_KEY) === this.ROLE_ADMIN) {
            return true;
        }
        alert(this.ALERT_MESSAGE);
        this.router.navigate(['login']);
        return false;
    }
}

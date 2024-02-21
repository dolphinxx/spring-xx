import Cookies from "js-cookie";
import {type Router} from "vue-router";

export const hasAuthCookie = () => Cookies.get("authenticated") === "1";

let loginRedirectUrl: string | null = null;

export const saveLoginRedirectUrl = (url: string) => (loginRedirectUrl = url);

export const clearLoginRedirectUrl = () => (loginRedirectUrl = null);

export const getLoginRedirectUrl = (): string | null => {
  return loginRedirectUrl;
};

export const redirectToLogin = (router:Router) => {
  return router.replace('/login');
}

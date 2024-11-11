import axios from "axios";
export default class AccountService {
  /**
   *
   * @returns
   */
  public login(): Promise<any> {
    return new Promise((resolve) => {
      axios
        .get<any>("/auth/authenticate")
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          Promise.reject(error);
        });
    });
  }

  public logout(): void {
    sessionStorage.removeItem("token");
    localStorage.removeItem("token");
  }
}

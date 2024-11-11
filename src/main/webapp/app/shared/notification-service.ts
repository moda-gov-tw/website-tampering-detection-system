import store from "@/store/index";

export default class NotificationService {
  constructor() {}

  public danger(message: string){
    this.showModal(message,"danger")
  }

  public info(message: string){
    this.showModal(message,"info")
  }

  private showModal(message: string, level: string) {
    store.commit("show", true);
    store.commit("changeMessage", message);
    store.commit("changeLevel", level);
  }
}

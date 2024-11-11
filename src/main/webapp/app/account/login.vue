<template>
  <div>
    <h1 class="login">login</h1>
    <form v-on:submit.prevent="submitForm" class="text-center">
      <label for="username">Username &nbsp;:&nbsp;</label>
      <input type="text" id="username" name="username" v-model="username" />
      <br />
      <br />
      <br />
      <label for="password">Password&nbsp;:&nbsp;</label>
      <input type="password" id="password" name="password" v-model="password" />
      <br />
      <br />
      <br />
      <!-- <button type="submit">Login</button> -->
      <b-button
        class="ml-2"
        style="background-color: #1aa4b7"
        @click="submitForm"
        >Login</b-button
      >
    </form>

    <!-- <form class="login">
      <h1>login</h1>
      <h2>帳號</h2>
      <input type="text" placeholder="請輸入帳號" />
      <h2>密碼</h2>
      <input type="text" placeholder="請輸入密碼" />
      <button type="submit">登入</button>
    </form> -->
  </div>
</template>

<script lang="ts">
import { ref, h } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import { NotifyModalStore } from "@/store/notify-modal-store";
import homeVue from "@/component/home.vue";
export default {
  name: "home",
  componet: { homeVue },
  setup() {
    let username = ref("admin");
    let password = ref("admin");
    let jwt: any = ref("");
    const router = useRouter();
    async function submitForm() {
      if (username.value === "" || password.value === "") {
        alert("Please enter both username and password.");
      } else {
        jwt.value = await axios.post("auth/authenticate", {
          username: username.value,
          password: password.value,
        });
        sessionStorage.setItem("token", jwt.value.data);
        router.push({ name: "home" });
      }
    }

    return {
      username,
      password,
      submitForm,
      jwt,
    };
  },
};
</script>

<style scoped>
/* body {
  width: 100%;
  height: 100%;
  padding: 0;
  margin: 0;
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
} */
.login {
  /* position: fixed; */
  /* left: 50%;
  top: 50%; */
  /* transform: translate(-50%, -50%); */
  background: (0, 0, 0, 0.5);
  text-align: center;
  color: #666;
  border-radius: 10px;
  /* padding: 30px; */
}
</style>
<template>
  <div>
    <div>
      <div>
        <b-button v-b-toggle.sidebar-right variant="Light">
          <b-icon icon="exclamation-circle-fill" variant="primary"></b-icon
          >功能選單</b-button
        >
        <b-collapse id="sidebar-right" title="Sidebar" right>
          <div class="px-3 py-2">
            <div class="sidebar">
              <ul>
                <li>
                  <router-link
                    class="list-group-item"
                    active-class="active"
                    to="/searchLog"
                    >批次服務查詢</router-link
                  >
                </li>
                <!-- <li>
                  <router-link
                    class="list-group-item"
                    active-class="active"
                    to="/addServer"
                    >新增批次服務</router-link
                  >
                </li>
                <li>
                  <router-link
                    class="list-group-item"
                    active-class="active"
                    to="/searchServerHome"
                    >主機查詢</router-link
                  >
                </li> -->
                <li>
                  <router-link
                    class="list-group-item"
                    active-class="active"
                    to="/iwg-host-list"
                    >註冊監控主機</router-link
                  >
                </li>
                <li>
                  <router-link
                    class="list-group-item"
                    active-class="active"
                    to="/iwg-host-target"
                    >主機監控設定</router-link
                  >
                </li>
                <!-- <li>
                  <router-link
                    class="list-group-item"
                    active-class="active"
                    to="/testRouter"
                    >testRouter</router-link
                  >
                </li> -->
                <!-- <li>
                  <router-link
                    class="list-group-item"
                    active-class="active"
                    to="/testRouter2"
                    >testRouter2</router-link
                  >
                </li> -->
              </ul>
            </div>
          </div>
        </b-collapse>
      </div>
    </div>
    <div>
      <router-view></router-view>
    </div>
    <!-- <h1>Login Page</h1>
    <form v-on:submit.prevent="submitForm">
      <label for="username">Username:</label>
      <input type="text" id="username" name="username" v-model="username" />
      <label for="password">Password:</label>
      <input type="password" id="password" name="password" v-model="password" />
      <button type="submit">
        <router-link class="list-group-item" active-class="active" to="/log"
          >Login</router-link
        >
      </button>
      {{ jwt }}
    </form> -->
  </div>
</template>

<script lang="ts">
import { ref, toRef, Ref, toRefs } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";
import AccountService from "@/account/accout.service";
import { NotifyModalStore } from "@/store/notify-modal-store";
import { useStore } from "vuex";

export default {
  name: "home",
  setup() {
    // const logStatusProps: Ref<any> = toRef(props, "logStatus");

    // console.log("6666logStatusPropsvalue", logStatusProps.value);
    // console.log("6666logStatusProps", logStatusProps);
    // console.log("NotifyModalStore", NotifyModalStore);
    // console.log("useStore", useStore);
    const router = useRouter();
    const accountService = new AccountService();
    const result = ref("");
    const testToken: any = () => {
      axios
        .get("testResource")
        .then((response: any) => {
          console.log(response);
          result.value = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    };

    const logout = () => {
      accountService.logout();
      router.push({ name: "login" });
    };
    return {
      testToken,
      logout,
    };
  },
};
</script>

<style scoped>
.sidebar {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 10px;
}

/* .sidebar h3 {
  font-size: 20px;
  margin-top: 0;
  margin-bottom: 10px;
} */

.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar li {
  margin-bottom: 10px;
  height: 20px;
  width: 100px;
  /* background-color: #1aa4b7;
  border-radius: 4px; */
  /* color: whith; */
}

.sidebar a {
  color: #337ab7;
  text-decoration: none;
}

.sidebar a:hover {
  text-decoration: underline;
}
</style>
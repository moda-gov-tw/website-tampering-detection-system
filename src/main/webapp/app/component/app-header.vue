<template>
  <header>
    <div class="desktop-app-header-wrapper">
      <b-container>
          <b-row>
          <b-col cols="4">
          </b-col>
          <b-col cols="4"><div class="top-nav"><strong>系統監控管理</strong></div></b-col>
          <b-col cols="4"><a class="btn-link" @click="logout">登出</a></b-col>
        </b-row>     
      </b-container>
    </div>
  </header>
</template>

<script lang="ts">
import { ref } from "vue";
import AccountService from "@/account/accout.service";
import axios from "axios";
import { useRouter } from "vue-router";
import { NotifyModalStore } from "@/store/notify-modal-store";
import { useStore } from "vuex";

export default {
  name: "AppHeader",
  setup() {
    const router = useRouter();
    const accountService = new AccountService();

    const logout = () => {
      accountService.logout();
      router.push({ name: "login" });
    };

    return { logout, NotifyModalStore };
  },
};
</script>
<style lang="scss" scoped>
.desktop-app-header-wrapper > .top-nav {
  font-size: 18px; /* 14px */
  color: #fff;
  background-color: #1aa4b7;
  padding-top: 1rem;
  padding-bottom: 1rem;
  text-align: right;
  font-weight: 500;
}
.desktop-app-header-wrapper {
  font-size: 18px; /* 14px */
  color: #fff;
  background-color: #1aa4b7;
  padding-top: 1rem;
  padding-bottom: 1rem;
  text-align: center;
  font-weight: 500;
}
.desktop-app-header-wrapper2 > .top-nav {
  font-size: 20px; /* 14px */
  color: #fff;
  background-color: #1aa4b7;
  padding-top: 1rem;
  padding-bottom: 1rem;
  text-align: center;
  font-weight: 500;
}

.btn-link {
  font-size: 14px; /* 14px */
  padding-top: 1rem;
  padding-bottom: 1rem;
  text-align: center;
  font-weight: 500;
  color: #fff;
}
.btn-link:hover {
  margin-left: 0.25rem;
  margin-right: 0.25rem;
  color: #fff;
  border-bottom: 1px solid;
}
</style>

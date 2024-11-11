<template>
  <div>
    <appHeaderVue />
    <div>
      <router-view></router-view>
    </div>
    <NotifyModal></NotifyModal>
  </div>
</template>

<script lang="ts">
import { useRouter } from "vue-router";
import NotifyModal from "@/component/notify-modal.vue";
import appHeaderVue from "./component/app-header.vue";
export default {
  name: "app",
  components: {
    NotifyModal,
    appHeaderVue,
  },
  setup() {
    const router = useRouter();
    router.push({ name: "login" });
  },
};
</script>

<style scoped>
.belt {
  position: relative;
}
.belt-btn-wrapper {
  position: absolute;
  top: 0;
  left: 15px;
  height: 100%;
}
</style>
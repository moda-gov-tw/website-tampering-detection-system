<template>
  <div>
    <h1>test router2</h1>
    <BButton @click="notifyTest()">Open Modal</BButton>
  </div>
</template>
<script lang="ts">
import NotificationService from "@/shared/notification-service";

export default {
  name: "testRouter2",
  setup() {
    const notificationService = new NotificationService();
    function notifyTest(){
      notificationService.info("nooooo")
    }
    return {
      notifyTest
    };
  },
};
</script>
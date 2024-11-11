<template>
  <div>
    <h1>test router</h1>
    <h1>home</h1>
    <button @click="testToken()">testToken</button>
    <div>
      <b-table striped hover :items="items"></b-table>
    </div>   
    <i class="bi bi-clipboard"></i>
    <div>
      <h3>
        check store modal
        <BButton @click="showModals">change store</BButton>
      </h3>
    </div>
  </div>
</template>
<script lang="ts">
import { ref, getCurrentInstance, watch } from "vue";
import axios from "axios";
import { BModal } from "bootstrap-vue-next";
import { useStore } from "vuex";
import NotificationService from "@/shared/notification-service";
export default {
  name: "testRouter",
  components: {
    BModal,
  },
  setup() {
    const vue = getCurrentInstance();
    const notificationService = new NotificationService();
    const store = useStore();
    const items = ref([
      { age: 40, first_name: "Dickerson", last_name: "Macdonald" },
      { age: 21, first_name: "Larsen", last_name: "Shaw" },
      { age: 89, first_name: "Geneva", last_name: "Wilson" },
      { age: 38, first_name: "Jami", last_name: "Carney" },
    ]);
    const result = ref("");
    const showModal = ref(false);
    const testToken: any = () => {
      axios
        .get("testResource")
        .then((response: any) => {
          console.log(response);
          result.value = response.data;
          notificationService.info(response.data);
        })
        .catch((error) => {
          notificationService.danger(error);
          console.log(error);
        });
    };

    function showModals() {
      notificationService.danger("!!!");
    }
    

    return {
      testToken,
      items,
      showModal,
      showModals,
    };
  },
};
</script>
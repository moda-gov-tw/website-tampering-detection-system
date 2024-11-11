<template>
  <div>
    <div class="text-center">
      <b-table :items="iwgHostList" :fields="fields">
        <template #cell(action)="row">
          <!-- {{ data }} -->
          <b-button @click="toIwgHostTargetSetting(row.item)"
            >監控設定</b-button
          >
        </template>
      </b-table>
    </div>
  </div>
</template>

<script lang="ts">
import axios from "axios";
import { ref, computed, reactive, onMounted } from "vue";
import router from "@/router";
import NotificationService from "@/shared/notification-service";

export default {
  name: "IwgHostsTarget",
  setup() {
    const notificationService = new NotificationService();
    const resultData: any[] = [];
    const iwgHostList = reactive([]);

    const fields = [
      {
        key: "hostname",
        label: "主機",
        sortable: false,
        thStyle: "width:10%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
      {
        key: "port",
        label: "port號",
        sortable: false,
        thStyle: "width:20%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
      {
        key: "username",
        label: "使用者",
        sortable: false,
        thStyle: "width:20%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
      {
        key: "active",
        label: "是否啟動",
        sortable: false,
        thStyle: "width:20%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
      {
        key: "action",
        label: "動作",
        sortable: false,
        thStyle: "width:20%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
    ];

    async function findAllIwgHost() {
      let hostList = await axios.post("/find/iwgHosts/all");
      Object.assign(iwgHostList, hostList.data);
      resultData.splice(0, resultData.length, hostList.data);
    }

    function toIwgHostTargetSetting(iwghost: any) {
      console.log("iwghost", iwghost.id);
      if (iwghost !== null && iwghost.id) {
        router.push({ path: `iwg-host-target/setting/${iwghost.id}` });
      }
    }

    onMounted(() => {
      findAllIwgHost();
    });

    return {
      iwgHostList,
      fields,
      toIwgHostTargetSetting,
    };
  },
};
</script>

<style scoped>
.batchService,
.batchCotent {
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}
.ml-2 {
  margin-right: 20px;
}
</style>
<template>
  <div>
    <div class="text-center">
      <div>
        <b-button @click="addIwgHostWithModal()">新增</b-button>
      </div>
      <b-table :items="iwgHostList" :fields="fields">
        <template #cell(action)="row">
          <!-- {{ data }} -->
          <b-button @click="editIwgHostWithModal(row.item)">編輯</b-button>
        </template>
      </b-table>
    </div>
    <b-modal
      id="iwgHostDataModal"
      title="監控server基本資料"
      hide-footer
      v-model="showIwgHostDataModal"
      :centered="true"
      size="lg"
    >
      <div class="batchService">
        <b-form class="pb-2">
          <b-form-group
            label-cols="4"
            content-cols="8"
            label="帳號"
            label-for="username"
          >
            <b-form-input
              id="username"
              v-model="form.username"
              placeholder="userName"
            ></b-form-input>
          </b-form-group>
        </b-form>
        <b-form class="pb-2">
          <b-form-group
            label-cols="4"
            content-cols="8"
            label="密碼"
            label-for="password"
          >
            <b-form-input
              id="password"
              type="password"
              v-model="form.pwd"
              placeholder="password"
            ></b-form-input>
          </b-form-group>
        </b-form>
        <b-form class="pb-2">
          <b-form-group
            label-cols="4"
            content-cols="8"
            label="主機名稱"
            label-for="hostname"
          >
            <b-form-input
              id="hostname"
              v-model="form.hostname"
              placeholder="192.168.1.1"
            ></b-form-input>
          </b-form-group>
        </b-form>
        <b-form class="pb-2">
          <b-form-group
            label-cols="4"
            content-cols="8"
            label="Port號"
            label-for="port"
          >
            <b-form-input
              id="port"
              v-model="form.port"
              placeholder="22"
            ></b-form-input>
          </b-form-group>
        </b-form>
        <b-form class="pb-2">
          <b-form-group
            label-cols="4"
            content-cols="8"
            label="信件收件者"
            label-for="mailReceiver"
          >
            <b-form-input
              id="mailReceiver"
              v-model="form.mailReceiver"
              placeholder="testReceiver@test.com"
            ></b-form-input>
          </b-form-group>
        </b-form>
        <b-form class="pb-2">
          <b-form-group
            label-cols="4"
            content-cols="8"
            label="SMS收件者"
            label-for="smsReceiver"
          >
            <b-form-input
              id="smsReceiver"
              v-model="form.smsReceiver"
              placeholder="0912345678"
            ></b-form-input>
          </b-form-group>
        </b-form>
        <b-form>
          <b-form-group
            label-cols="4"
            content-cols="8"
            id="active"
            label="是否啟用"
            label-for="active"
          >
            <b-form-radio-group id="active" v-model="form.active">
              <b-form-radio value="Y">是</b-form-radio>
              <b-form-radio value="N">否</b-form-radio>
            </b-form-radio-group>
          </b-form-group>
        </b-form>
        <div class="d-flex p-2">
          <div class="justify-content-center">
            <span class="pl-2" v-if="addOrEdit === 'edit'">
              <b-button class="pl-2" @click="editOne()">更新</b-button>
            </span>
            <span class="pl-2" v-if="addOrEdit === 'add'">
              <b-button class="pl-2" @click="addOne()">新增</b-button>
            </span>
            <b-button class="pl-2" @click="cancel()">取消</b-button>
          </div>
        </div>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts">
import axios from "axios";
import { ref, computed, reactive, onMounted } from "vue";
import router from "@/router";
import NotificationService from "@/shared/notification-service";

export default {
  name: "IwgHostList",
  setup() {
    const notificationService = new NotificationService();
    const resultData: any[] = [];
    const iwgHostList = reactive([]);
    const showIwgHostDataModal = ref(false);
    const addOrEdit = ref("");

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

    let formDefault = {
      id: "",
      username: "",
      pwd: "",
      hostname: "",
      port: "",
      mailReceiver: "",
      smsReceiver: "",
      active: "Y",
    };

    let form = reactive(Object.assign({}, formDefault));

    async function findAllIwgHost() {
      let hostList = await axios.post("/find/iwgHosts/all");
      Object.assign(iwgHostList, hostList.data);
      resultData.splice(0, resultData.length, hostList.data);
    }
    async function editOne() {
      console.log('editOne')
      console.log(form)
      await axios.post("/update/iwgHost", form);
      showIwgHostDataModal.value = false;
      await findAllIwgHost();
    }
    async function addOne() {
      console.log('addOne')
      console.log(form)
      await axios.post("/add/iwgHost", form);
      showIwgHostDataModal.value = false;
      await findAllIwgHost();
    }
    function cancel() {
      showIwgHostDataModal.value = false;
    }

    function editIwgHostWithModal(data: any) {
      showIwgHostDataModal.value = true;
      addOrEdit.value = "edit";
      Object.assign(form, data);
    }

    function addIwgHostWithModal() {
      showIwgHostDataModal.value = true;
      addOrEdit.value = "add";
      Object.assign(form, formDefault);
    }

    onMounted(() => {
      findAllIwgHost();
    });

    return {
      iwgHostList,
      fields,
      form,
      showIwgHostDataModal,
      addIwgHostWithModal,
      editIwgHostWithModal,
      editOne,
      addOne,
      cancel,
      addOrEdit,
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
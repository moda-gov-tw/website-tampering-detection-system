<template>
  <div class="text-center">
    <h3 class="text-center pb-5">新增批次服務</h3>
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
            v-model="formDefault.username"
            placeholder="userName"
          ></b-form-input>
        </b-form-group>
      </b-form>
      <b-form class="pb-2">
        <b-form-group
          label-cols="4"
          content-cols="8"
          label="密碼"
          label-for="pwd"
        >
          <b-form-input
            id="pwd"
            type="password"
            v-model="formDefault.pwd"
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
            v-model="formDefault.hostname"
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
            v-model="formDefault.port"
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
            v-model="formDefault.mailReceiver"
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
            v-model="formDefault.smsReceiver"
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
          <b-form-radio-group id="active" v-model="formDefault.active">
            <b-form-radio value="Y">是</b-form-radio>
            <b-form-radio value="N">否</b-form-radio>
          </b-form-radio-group>
        </b-form-group>
      </b-form>
    </div>
    <h3 class="text-center pb-5 pt-5">批次掃描內容</h3>
    <!-- <b-button
      class="ml-2"
      style="background-color: #1aa4b7"
      @click="addCheckadv"
      >新增</b-button
    > -->

    <!-- <div v-for="(item, index) in item">
      <b-tr>
        <b-td>
          <b-form-textarea
            rows="3"
            maxlength="4000"
            v-model="item.detel1"
          ></b-form-textarea>
        </b-td>
        <b-td class="text-center">
          <i-button type="trash" @click="removeCheckadv(index)"></i-button>
        </b-td>
      </b-tr>
    </div> -->
    <div class="batchCotent">
      <b-form-row class="pb-2">
        <b-form-group
          label-cols="4"
          content-cols="8"
          label="檔案名稱"
          label-for="fileName"
        >
          <b-form-input
            id="fileName"
            v-model="formDefault.fileName"
            placeholder="pwc-web.war"
          ></b-form-input>
        </b-form-group>
      </b-form-row>
      <b-form-row class="pb-2">
        <b-form-group
          label-cols="4"
          content-cols="8"
          label="IWG主機中參考檔案的位置"
          label-for="originFileLocation"
        >
          <b-form-input
            id="originFileLocation"
            v-model="formDefault.originFileLocation"
            placeholder="/root/"
          ></b-form-input>
        </b-form-group>
      </b-form-row>
      <b-form-row class="pb-2">
        <b-form-group
          label-cols="4"
          content-cols="8"
          label="IWG要監控的host中的檔案位置"
          label-for="targetFileLocation"
        >
          <b-form-input
            id="targetFileLocation"
            v-model="formDefault.targetFileLocation"
            placeholder=""
          ></b-form-input>
        </b-form-group>
      </b-form-row>
      <b-form-row class="pb-2">
        <b-form-group
          label-cols="4"
          content-cols="8"
          label="IWG主機中參考的目錄位置"
          label-for="originFolder"
        >
          <b-form-input
            id="originFolder"
            v-model="formDefault.originFolder"
            placeholder="C:Users/2106017welcome-content"
          ></b-form-input>
        </b-form-group>
      </b-form-row>
      <b-form-row class="pb-2">
        <b-form-group
          label-cols="4"
          content-cols="8"
          label="IWG要監控的host中的目錄位置"
          label-for="targerFolder"
        >
          <b-form-input
            id="targerFolder"
            v-model="formDefault.targerFolder"
            placeholder="/opt/wildfly-4/welcome-content/"
          ></b-form-input>
        </b-form-group>
      </b-form-row>
    </div>

    <div class="text-center pt-5">
      <b-button class="ml-2" style="background-color: #1aa4b7" @click="toSave"
        >儲存</b-button
      >
      <b-button class="ml-2" style="background-color: #1aa4b7" @click="reset"
        >清除</b-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import axios from "axios";
import { ref, computed, reactive, onMounted } from "vue";
import router from "@/router";
import NotificationService from "@/shared/notification-service";

export default {
  name: "addServer",
  setup() {
    const notificationService = new NotificationService();

    let formDefault = reactive({
      username: "",
      pwd: "",
      hostname: "",
      port: "",
      mailReceiver: "",
      smsReceiver: "",
      active: "Y",
      fileName: "",
      originFileLocation: "",
      targetFileLocation: "",
      originFolder: "",
      targerFolder: "",
    });

    // 表單物件驗證規則
    const rules = ref({
      username: {},
      pwd: {},
      hostname: {},
      port: {},
      mailReceiver: {},
      smsReceiver: {},
      active: {},
      fileName: {},
      originFileLocation: {},
      targetFileLocation: {},
      originFolder: {},
      targerFolder: {},
    });
    const form = reactive(Object.assign({}, formDefault));

    const reset = () => {
      formDefault.username = "";
      formDefault.pwd = "";
      formDefault.hostname = "";
      formDefault.port = "";
      formDefault.mailReceiver = "";
      formDefault.smsReceiver = "";
      formDefault.active = "Y";
      formDefault.fileName = "";
      formDefault.originFileLocation = "";
      formDefault.targetFileLocation = "";
      formDefault.originFolder = "";
      formDefault.targerFolder = "";
    };

    const toSave = () => {
      if (
        formDefault.username === "" ||
        formDefault.pwd === "" ||
        formDefault.hostname === "" ||
        formDefault.port === "" ||
        formDefault.mailReceiver === "" ||
        formDefault.smsReceiver === "" ||
        formDefault.fileName === "" ||
        formDefault.originFileLocation === "" ||
        formDefault.targetFileLocation === "" ||
        formDefault.originFolder === "" ||
        formDefault.targerFolder === ""
      ) {
        notificationService.danger(
          "欄位尚未填寫完畢，請於輸入完畢後再行送出。"
        );
      } else {
        axios
          .post("/create/iwgHosts", formDefault)
          .then((response: any) => {
            notificationService.info("新增成功");
            console.log("then", response);
          })
          .catch((error) => {
            console.log("catch", error);
          });
      }
    };

    // function addCheckadv() {
    //   item.value.push({ detel1: item.value.length + 1 + "." });
    // }

    // function removeCheckadv(index: any) {
    //   item.value.splice(index, 1);
    // }

    return {
      formDefault,
      toSave,
      reset,
      // addCheckadv,
      // removeCheckadv,
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
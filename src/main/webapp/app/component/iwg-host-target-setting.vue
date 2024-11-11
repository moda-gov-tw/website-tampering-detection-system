<template>
  <div>
    <!-- class="text-center" -->
    <div>
      <b-container>
        <div class="justify-content-md-center" style="width: 80%">
          <b-card>
            <div>
              <h4>主機資訊</h4>
              <div>主機:{{ hostData.hostname }}</div>
              <div>port:{{ hostData.port }}</div>
              <div>使用者:{{ hostData.username }}</div>
            </div>
          </b-card>
        </div>
      </b-container>
      <!-- iwgHostTargetFolderList -->
      <b-container>
        <div class="mt-5 justify-content-md-center" style="width: 90%">
          <h4>設定該主機需被監控的資料夾</h4>
          <b-table
            :items="iwgHostTargetFolderList"
            :fields="folderTargetfields"
          >
            <template #cell(action)="row">              
              <b-button @click="openFolderModal(row.item.id)">監控資料夾設定</b-button>
            </template>
          </b-table>
        </div>
      </b-container>
      <b-container>
        <div class="mt-5 justify-content-md-center" style="width: 90%">
          <h4>設定該主機需被監控的檔案</h4>
          <b-table :items="iwgHostTargetFileList" :fields="fileTargetfields">
            <template #cell(action)="row">
              <b-button @click="openFileModal(row.item.id)">監控檔案設定</b-button>
            </template>
          </b-table>
        </div>
      </b-container>      
      <b-modal
        id="showHostFileTargetModal"
        title="設定被監控server的檔案"
        hide-footer
        v-model="showHostFileTargetModal"
        :centered="true"
        size="lg"
      >
        <div class="batchService">
          <b-form class="pb-2">
            <b-form-group label-cols="4" content-cols="8" label="檔名">
              <b-form-input
                v-model="targetFileObj.fileName"
                placeholder=""
              ></b-form-input>
            </b-form-group>
          </b-form>
          <b-form class="pb-2">
            <b-form-group label-cols="4" content-cols="8" label="iwg上檔案位置">
              <b-form-input
                v-model="targetFileObj.originFileLocation"
                placeholder=""
              ></b-form-input>
            </b-form-group>
          </b-form>
          <b-form class="pb-2">
            <b-form-group
              label-cols="4"
              content-cols="8"
              label="該主機上被監控的檔案位置"
            >
              <b-form-input
                v-model="targetFileObj.targetFileLocation"
                placeholder=""
              ></b-form-input>
            </b-form-group>
          </b-form>
          <b-form>
            <b-form-group
              label-cols="4"
              content-cols="8"
              id="active"
              label="是否啟用"
            >
              <b-form-radio-group v-model="targetFileObj.active">
                <b-form-radio value="Y">是</b-form-radio>
                <b-form-radio value="N">否</b-form-radio>
              </b-form-radio-group>
            </b-form-group>
          </b-form>
          <div>
            <b-button class="pl-2" @click="editHostFile()">更新</b-button>
            <b-button class="pl-2" @click="showHostFileTargetModal=false">取消</b-button>
          </div>
        </div>
      </b-modal>
        <b-modal
        id="showHostFolderTargetModal"
        title="設定被監控server的資料夾"
        hide-footer
        v-model="showHostFolderTargetModal"
        :centered="true"
        size="lg"
      >
        <div class="batchService"> 
          <b-form class="pb-2">
            <b-form-group label-cols="4" content-cols="8" label="iwg上資料夾位置">
              <b-form-input
                v-model="targetFolderObj.originFolder"
                placeholder=""
              ></b-form-input>
            </b-form-group>
          </b-form>
          <b-form class="pb-2">
            <b-form-group
              label-cols="4"
              content-cols="8"
              label="該主機上被監控的資料夾位置"
            >
              <b-form-input
                v-model="targetFolderObj.targetFolder"
                placeholder=""
              ></b-form-input>
            </b-form-group>
          </b-form>
          <b-form>
            <b-form-group
              label-cols="4"
              content-cols="8"
              id="active"
              label="是否啟用"
            >
              <b-form-radio-group v-model="targetFolderObj.active">
                <b-form-radio value="Y">是</b-form-radio>
                <b-form-radio value="N">否</b-form-radio>
              </b-form-radio-group>
            </b-form-group>
          </b-form>
          <div>
            <b-button class="pl-2" @click="editHostFolder()">更新</b-button>
            <b-button class="pl-2" @click="showHostFolderTargetModal=false">取消</b-button>
          </div>
        </div>
      </b-modal>
    </div>
  </div>
</template>

<script lang="ts">
import axios from "axios";
import { ref, computed, reactive, onMounted, toRef, watch } from "vue";
import router from "@/router";
import NotificationService from "@/shared/notification-service";

export default {
  nname: "IwgHostsTargetSetting",
  props: {
    id: {
      type: Object,
      required: false,
    },
  },
  setup(props: any) {
    console.log("props", props);
    let hostId = "";
    const idRef: any = toRef(props, "id");
    const hostData = reactive({
      hostname: "",
      port: "",
      username: "",
    });
    const notificationService = new NotificationService();
    const iwgHostTargetFileList: any = reactive([]);
    const iwgHostTargetFolderList: any = reactive([]);

    //modal用
    const showHostFileTargetModal = ref(false);
    const showHostFolderTargetModal = ref(false);

    const targetFileObjDefault = {
      id: "",
      fileName: "",
      originFileLocation: "",
      targetFileLocation: "",
      active: "N",
    };
     const targetFolderObjDefault = {
      id: "",
      originFolder: "",
      targetFolder: "",
      active: "N",
    };
    const targetFileObj = Object.assign({}, targetFileObjDefault);
    const targetFolderObj = Object.assign({}, targetFolderObjDefault);

    const fileTargetfields = [
      {
        key: "fileName",
        label: "檔名",
        sortable: false,
        thStyle: "width:10%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
      {
        key: "originFileLocation",
        label: "iwg上檔案位置",
        sortable: false,
        thStyle: "width:20%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
      {
        key: "targetFileLocation",
        label: "該主機上被監控的檔案位置",
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

    const folderTargetfields = [
      {
        key: "originFolder",
        label: "iwg上資料夾位置",
        sortable: false,
        thStyle: "width:20%",
        thClass: "text-center",
        tdClass: "text-center align-middle",
      },
      {
        key: "targetFolder",
        label: "該主機上被監控的資料夾位置",
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
    //透過hostId找整個對應要監控的target
    async function findIwgHostTargetByHostId(id: string) {
      let hostTargetListResponse = await axios.post(
        "find-targets-by-hostname-and-port",
        { id }
      );
      let hostTargetList: any[] = hostTargetListResponse.data;
      let fileTargetList = hostTargetList.filter((element) => {
        return element.fileName !== null && element.fileName.length > 0;
      });
      iwgHostTargetFileList.splice(
        0,
        iwgHostTargetFileList.length,
        ...fileTargetList
      );
      let folderTargetList = hostTargetList.filter((element) => {
        return element.targetFolder !== null && element.targetFolder.length > 0;
      });
      iwgHostTargetFolderList.splice(
        0,
        iwgHostTargetFolderList.length,
        ...folderTargetList
      );

      console.log(iwgHostTargetFileList);
      console.log(iwgHostTargetFolderList);
      // Object.assign(iwgHostList, hostList.data);
      // resultData.splice(0, resultData.length, hostList.data);
    }

    //透過hostId找host
    async function findHostById(id: string) {
      let hostResponse = await axios.post("/find-by-id/iwgHost", { id });
      Object.assign(hostData, hostResponse.data);
    }
    //透過targetId找target
    async function findHostTargetById(id: string) {
      return await axios.post("/iwg-hosts-target/find-by-id", { id });
    }

    //處理file的部分
    async function openFileModal(id: string) {
      let response: any = await findHostTargetById(id);
      Object.assign(targetFileObj, response.data);
      showHostFileTargetModal.value = true;
    }
    async function editHostFile(){
      let response: any = await axios.post("/iwg-hosts-target/update",targetFileObj);
      console.log(response);
      showHostFileTargetModal.value = false;
      findIwgHostTargetByHostId(idRef.value);
    }
    //處理folder的部分
    async function openFolderModal(id: string) {
      let response: any = await findHostTargetById(id);
      Object.assign(targetFolderObj, response.data);
      showHostFolderTargetModal.value = true;
    }
    async function editHostFolder(){
      let response: any = await axios.post("/iwg-hosts-target/update",targetFolderObj);
      showHostFolderTargetModal.value = false;
      findIwgHostTargetByHostId(idRef.value);
    }


    watch(
      idRef,
      (newValue: string) => {
        console.log(newValue);
        hostId = newValue;
        findIwgHostTargetByHostId(newValue);
        findHostById(newValue);
      },
      { immediate: true }
    );

    return {
      fileTargetfields,
      folderTargetfields,
      iwgHostTargetFileList,
      iwgHostTargetFolderList,
      hostData,
      showHostFileTargetModal,
      showHostFolderTargetModal,
      targetFileObj,
      targetFolderObj,
      editHostFile,
      editHostFolder,
      openFileModal,
      openFolderModal,
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
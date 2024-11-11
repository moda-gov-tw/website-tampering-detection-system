<template>
  <div class="text-center">
    <h3 class="text-center pb-5">批次服務查詢</h3>
    <!-- <b-container fluid>
      <b-row class="my-1" v-for="type in types" :key="type">
        <b-col sm="3">
          <label :for="`type-${type}`"
            ><code>{{ type }}</code
            >:</label
          >
        </b-col>
        <b-col sm="9">
          <b-form-input :id="`type-${type}`" :type="type"></b-form-input>
        </b-col>
      </b-row>
    </b-container> -->

    <!-- <b-form-row> -->
    <!-- <i-form-group-check :label="$t('label.batchServiceCategory') + '：'" :item="$v.batchServiceCategory"> -->
    <!-- <b-form-input></b-form-input> -->
    <!-- </i-form-group-check> -->
    <!-- <i-form-group-check :label="$t('label.batchServiceNo') + '：'" :item="$v.batchServiceNo"> -->
    <!-- <b-form-select :options="queryOptions.ServiceNoOpt" v-model="$v.batchServiceNo.$model"></b-form-select> -->
    <!-- </i-form-group-check> -->
    <!-- </b-form-row> -->
    <div class="searchLog">
      <b-form class="pb-2">
        <b-form-group
          class="col-12"
          label-cols="4"
          content-cols="8"
          id="fieldset-1"
          label="主機名稱"
          label-for="hostname"
        >
          <b-form-input
            id="hostname"
            v-model="formDefault.hostname"
          ></b-form-input>
        </b-form-group>
      </b-form>
      <!-- <b-form class="pb-2">
        <b-form-group
          class="col-12"
          label-cols="4"
          content-cols="8"
          id="fieldset-1"
          label="檔案是否異動"
          label-for="result"
        >
          <b-form-radio-group id="result" v-model="formDefault.result">
            <b-form-radio value="Y">是</b-form-radio>
            <b-form-radio value="N">否</b-form-radio>
          </b-form-radio-group>
        </b-form-group>
      </b-form> -->
    </div>
    <div class="text-center pt-5">
      <b-button class="ml-2" style="background-color: #1aa4b7" @click="toQuery"
        >查詢</b-button
      >
      <b-button class="ml-2" style="background-color: #1aa4b7" @click="rest"
        >清除</b-button
      >
    </div>
  </div>

  <section class="pt-5" v-if="stepVisible">
    <div>
      <b-table
        striped
        hover
        :items="table.data"
        :fields="table.fields"
      >
        <template #cell(index)="row">
          {{ row.index + 1 }}
        </template>
        <template #cell(result)="row">
          {{ row.item.result === "Y" ? "成功" : "失敗" }}
        </template>
        <template #cell(triggerTime)="row">
          {{ formatDate(row.item.triggerTime) }}
        </template>
        <template #cell(finishTime)="row">
          {{ formatDate(row.item.finishTime) }}
        </template>
      </b-table>
    </div>
  </section>
</template>

<script lang="ts">
import { ref, computed, reactive, onMounted } from "vue";
import router from "@/router";
import axios from "axios";

export default {
  name: "searchLog",
  setup() {
    const stepVisible = ref(false);

    const page = ref(1); //當前頁面
    const perPage = ref(10); //每頁顯示的資料

    // 根據當前頁碼和每頁顯示的資料數，計算當前頁面顯示的資料
    const paginatedItems = computed(() => {
      const start = (page.value - 1) * perPage.value;
      const end = start + perPage.value;
      return table.data.slice(start, end);
    });

    // 計算總頁數
    const totalPages = computed(() => {
      return Math.ceil(table.data.length / perPage.value);
    });

    const formDefault = reactive({
      hostname: "",
      targeFileName: "",
      result: "",
    });

    // 表單物件驗證規則
    const rules = {
      hostname: {},
      targeFileName: {},
      result: {},
    };

    const form = reactive(Object.assign(formDefault));
    // const form = reactive(Object.assign({}, formDefault));

    const table = reactive({
      fields: [
        {
          key: "index",
          label: "序號",
          sortable: false,
          thStyle: "width:10%",
          thClass: "text-center",
          tdClass: "text-center align-middle",
        },
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
          key: "triggerTime",
          label: "啟動時間",
          sortable: false,
          thStyle: "width:20%",
          thClass: "text-center",
          tdClass: "text-center align-middle",
        },
        {
          key: "finishTime",
          label: "結束時間",
          sortable: false,
          thStyle: "width:20%",
          thClass: "text-center",
          tdClass: "text-center align-middle",
        },
        {
          key: "result",
          label: "結果",
          sortable: false,
          thStyle: "width:20%",
          thClass: "text-center",
          tdClass: "text-center align-middle",
        },
        {
          key: "targetFilename",
          label: "偵測檔案",
          sortable: false,
          thStyle: "width:20%",
          thClass: "text-center",
          tdClass: "text-center align-middle",
        },
      ],
      data: [],
      totalItems: 0,
    });

    const toAdd = () => {
      router.push({ path: "/add" });
    };

    const toQuery = () => {
      stepVisible.value = true;
      axios
        .post("/find/iwgHostsLogs", formDefault)
        .then((data) => {
          // ele.forEach((e) => {});
          table.data = data.data;
          console.log("table.data", table.data);
        })
        .catch((error) => {
          console.log("catch", error);
        });
      // table.data = [];
      // table.totalItems = 1;
      // table.data.splice(0, table.data.length, ...mockdata);
    };

    //轉換時間
    const formatDate = (value: any) => {
      var date = new Date(value);
      const year = (date.getFullYear() - 1911).toString().padStart(3, "0");
      const month = (date.getMonth() + 1).toString().padStart(2, "0");
      const day = date.getDate().toString().padStart(2, "0");
      const hour = date.getHours().toString().padStart(2, "0");
      const minutes = date.getMinutes().toString().padStart(2, "0");
      return (
        year + "年" + month + "月" + day + "日" + "/" + hour + ":" + minutes
      );
    };

    const rest = () => {
      formDefault.hostname = "";
    };

    const onPageChange = (pageNum: any) => {
      page.value = pageNum;
    };

    return {
      toAdd,
      formDefault,
      stepVisible,
      toQuery,
      table,
      rest,
      paginatedItems,
      totalPages,
      onPageChange,
      formatDate,
    };
  },
};
</script>

<style scoped>
.ml-2 {
  margin-right: 20px;
}
.searchLog {
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}
</style>
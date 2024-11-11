<template>
  <!-- <div class="inp">
    <input type="text" :placeholder="placeholder" v-model="search.content" />
    <button @click="handlerSearch">搜索</button>
  </div> -->
  <div class="text-center">
    <h3 class="text-center pb-5">主機查詢</h3>
    <div class="searchServer">
      <b-form>
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
        :items="paginatedItems"
        :fields="table.fields"
        :editable="true"
        :edit-mode="'inline'"
      >
        <template #cell(index)="row">
          {{ row.index + 1 }}
        </template>
        <template #cell(active)="row">
          {{ row.item.active === "Y" ? "是" : "否" }}
        </template>
        <template #cell(action)="row">
          <b-button
            class="ml-2"
            style="background-color: #1aa4b7"
            @click="toEdit(row.item)"
            >編輯</b-button
          >
        </template>
      </b-table>
      <b-pagination
        v-model="page"
        :total-rows="table.data.length"
        :per-page="perPage"
        align="center"
      />
    </div>
  </section>
</template>

<script lang="ts">
import { ref, computed, reactive, toRef, toRaw } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

export default {
  name: "searchServer",
  props: ["currentComponent"],
  setup(props: any, content: any) {
    const currentComponentprops = toRef(props, "currentComponent");
    console.log(currentComponentprops);

    const stepVisible = ref(false);
    const page = ref(1); //當前頁面
    const perPage = ref(20); //每頁顯示的資料

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
      result: "N",
    });

    // 表單物件驗證規則
    const rules = ref({
      hostname: {},
      result: {},
    });

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
          key: "active",
          label: "是否啟用",
          sortable: false,
          thStyle: "width:20%",
          thClass: "text-center",
          tdClass: "text-center align-middle",
        },
        {
          key: "action",
          label: "功能",
          sortable: false,
          thStyle: "width:20%",
          thClass: "text-center",
          tdClass: "text-center align-middle",
        },
      ],
      data: [],
      totalItems: 0,
    });

    const toQuery = () => {
      stepVisible.value = true;
      axios
        .post("/find/iwgHosts", formDefault)
        .then((data) => {
          // ele.forEach((e) => {});
          table.data = data.data;
        })
        .catch((error) => {
          console.log("catch", error);
        });
    };

    const router = useRouter();

    const toEdit = (ele: any) => {
      const elea = toRaw(ele);
      // router.push({
      //   name: "editServer",
      //   params: { id: JSON.stringify(ele) },
      // });
      content.emit("queryItemt", elea);
      content.emit("status", "editServerVue");
    };

    const rest = () => {
      formDefault.hostname = "";
    };

    return {
      formDefault,
      stepVisible,
      toQuery,
      toEdit,
      table,
      paginatedItems,
      totalPages,
      rest,
      currentComponentprops,
    };
  },
};
</script>

<style scoped>
.searchServer {
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}
.ml-2 {
  margin-right: 20px;
}
</style>
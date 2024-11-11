<template>
  <div>
    <searchServerVue
      v-if="status1 === 'searchServerVue'"
      currentComponent="父組件進來"
      @queryItemt="queryItemt"
      @status="status"
    />
    <editServerVue v-else :searchCon="searchCon" />
    <!-- <router-view></router-view> -->
  </div>
</template>

<script lang="ts">
import { ref, toRef, toRaw, watch } from "vue";
import editServerVue from "./edit-server.vue";
import searchServerVue from "./search-server.vue";
export default {
  name: "searchServerHome",
  components: { searchServerVue, editServerVue },

  setup(props: any, content: any) {
    const searchCon = ref("");
    const status1 = ref("searchServerVue");

    const searchClick = (searchServerVue: any) => {
      searchCon.value = searchServerVue.content;
    };

    const queryItemt = (searchServerVue: any) => {
      searchCon.value = searchServerVue;
    };

    const status = (searchServerVue: any) => {
      status1.value = searchServerVue;
    };

    return {
      searchClick,
      searchCon,
      queryItemt,
      status1,
      status,
    };
  },
};
</script>

<style scoped>
</style>
<template>
  <div>
    <b-modal @hide="hideModal" :header-bg-variant ="level"  v-model="showModal" title="訊息">
      <p class="my-4">{{ message }}</p>
      <div class="text-center"></div>
    </b-modal>
  </div>
</template>
<script lang="ts">
import { ref, watch } from "vue";
import { useStore } from "vuex";

export default {
  name: "notifyModal",
  setup() {
    const message = ref("");
    const showModal = ref(false);
    const level = ref('');
    const store = useStore();

    function hideModal() {
      store.commit("show", false);
    }

    watch(
      () => store.getters.message,
      (newValue) => {
        message.value = newValue;
      }
    );

    watch(
      () => store.getters.isShow,
      (newValue) => {
        showModal.value = newValue;
      }
    );
    watch(
      () => store.getters.level,
      (newValue) => {       
        level.value = newValue;
      }
    );

    return {
      message,
      showModal,
      level,
      hideModal,
    };
  },
};
</script>
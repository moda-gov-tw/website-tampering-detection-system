/* 
* shims-vue.d.ts是為了 typescript 做的定義檔案，因為.vue不是正常的檔案類型，ts不認識vue是幹嘛的，
* 加了這一段可以讓ts認識vue
* 若將這一段刪除，會發現所有的import vue都報錯。
*/
declare module "*.vue" {
    import type { DefineComponent } from "vue";
    const component: DefineComponent<{}, {}, any>;
    export default component;
}
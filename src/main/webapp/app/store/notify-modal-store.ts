import { Module } from 'vuex';
export const NotifyModalStore: Module<any, any> = {
    state: {
        isShow:false,
        message:'',
        level:''
    },
    getters: {
        isShow: state => state.isShow,
        message: state => state.message,
        level: state => state.level,
    },
    //mutation: 改state用的
    mutations: {
        show(state,isShow:boolean){
            state.isShow = isShow;
        },
        changeMessage(state,message){
            state.message = message;
        },
        changeLevel(state,level){
            state.level = level;
        }
    },
}
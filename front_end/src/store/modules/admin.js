import {
    getManagerListAPI,
    getClientListAPI,
    getMarketerListAPI,
    addManagerAPI,
    addMarketerAPI,
    removeUserAPI,
    deleteHotelAPI
} from '@/api/admin'
import { message } from 'ant-design-vue'
import {registerVIPAPI} from "../../api/vip";

const admin = {
    state: {
        managerList: [],
        clientList: [],
        marketerList: [],
        addManagerModalVisible: false,
        addMarketerModalVisible:false,
        amendUserModalVisible: false,
        addManagerParams: {
            email:'',
            password:''
        },
        hotelID: -1,
        addMarketerParams: {
            email:'',
            password:''
        }

    },
    mutations: {
        set_managerList: function(state, data) {
            state.managerList = data
        },
        set_clientList: function(state, data) {
            state.clientList = data
        },
        set_marketerList: function(state, data) {
            state.marketerList = data
        },
        set_addManagerModalVisible: function(state, data) {
            state.addManagerModalVisible = data
        },
        set_addMarketerModalVisible: function(state, data) {
            state.addMarketerModalVisible = data
        },
        set_amendUserModalVisible: function(state, data) {
            state.amendUserModalVisible = data
        },
        set_addManagerParams: function(state, data) {
            state.addManagerParams = {
                ...state.addManagerParams,
                ...data,
            }
        },
        set_addMarketerParams: function(state, data) {
            state.addMarketerParams = {
                ...state.addMarketerParams,
                ...data,
            }
        }
    },
    actions: {
        getManagerList: async({ commit }) => {
            const res = await getManagerListAPI()
            if(res){
                commit('set_managerList', res)
            }
            return res
        },
        getClientList: async({ commit }) => {
            const res = await getClientListAPI()
            if(res){
                commit('set_clientList', res)
            }
            return res
        },
        getMarketerList: async({ commit }) => {
            const res = await getMarketerListAPI()
            if(res){
                commit('set_marketerList', res)
            }
            return res
        },
        addManager: async({ state, commit, dispatch },hotelID) => {
            const res = await addManagerAPI(state.addManagerParams,hotelID)
            if(res) {
                commit('set_addManagerParams',{
                    email:'',
                    password:''
                })
                commit('set_addManagerModalVisible', false)
                message.success('添加成功')
                dispatch('getManagerList')
            }else{
                message.error('添加失败')
            }
        },
        //cpk
        registerVIP:async({state,dispatch,commit},data)=>{
            const res=await registerVIPAPI(data)
            if (res) {
                message.success('注册成功')
            }
        },
        //end
        addMarketer: async({ state, commit, dispatch }) => {
            const res = await addMarketerAPI(state.addMarketerParams)
            if(res) {
                commit('set_addMarketerParams',{
                    email:'',
                    password:''
                })
                commit('set_addMarketerModalVisible', false)
                message.success('添加成功')
                dispatch('getMarketerList')
            }else{
                message.error('添加失败')
            }
        },
        removeUser: async({ state, commit, dispatch },info) => {
            const res = await removeUserAPI(info.id)
            if(res) {
                message.success('删除成功')
                switch (info.userType) {
                    case 'Client':
                        dispatch('getClientList');break;
                    case 'Marketer':
                        dispatch('getMarketerList');break;
                    case 'HotelManager':
                        dispatch('getManagerList');break;
                }
            }else{
                message.error('删除失败')
            }
        },
        deleteHotel:async({ state, commit, dispatch },data) => {
            const res = await deleteHotelAPI(data)
            if(res) {
                message.success('删除成功')
                dispatch('getHotelList')
            }else{
                message.error('删除失败')
            }
            return res
        },
    }
}
export default admin
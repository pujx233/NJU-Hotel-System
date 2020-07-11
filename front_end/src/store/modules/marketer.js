import {
    hotelAllCouponsAPI,
} from '@/api/coupon'
import {
    buyCreditAPI,
    retrieveAbnormalOrdersAPI,
    annulAbnormalOrderAPI
}from '@/api/marketer'
import{
    removeAPI
}from '@/api/coupon'
import { message } from 'ant-design-vue'

const marketer = {
    state: {
        addCreditModalVisible: false,
        addCreditParams: {
            email:'',
            credit: 0
        },
        webCouponList:[],
        addWebCouponModalVisible: false,
        cancelAbOrderModalVisible: false,
        abnormalOrderList: [],
    },
    mutations: {
        set_addCreditModalVisible: function(state, data) {
            state.addCreditModalVisible = data
        },
        set_addCreditParams: function(state, data) {
            state.addCreditParams = {
                ...state.addCreditParams,
                ...data,
            }
        },
        set_webCouponList: function(state, data) {
            state.webCouponList = data
        },
        set_addWebCouponModalVisible: function (state, data) {
            state.addWebCouponModalVisible = data
        },
        set_cancelAbOrderModalVisible: function (state, data) {
            state.cancelAbOrderModalVisible = data
        },
        set_abnormalOrderList: function(state, data) {
            state.abnormalOrderList = data
        },
    },
    actions: {
        addCredit: async({ state, commit, dispatch }) => {
            const res = await buyCreditAPI(state.addCreditParams)
            if(res) {
                commit('set_addCreditParams',{
                    email:'',
                    credit:0
                })
                commit('set_addCreditModalVisible', false)
                message.success('添加成功')
                dispatch('getClientList')
            }else{
                message.error('添加失败')
            }
        },
        getWebCouponList: async({ commit }) => {
            const res = await hotelAllCouponsAPI(-1)
            if(res){
                commit('set_webCouponList', res)
            }
            return res
        },
        getabnormalOrderList: async({ commit }) => {
            const res = await retrieveAbnormalOrdersAPI()
            if(res){
                commit('set_abnormalOrderList', res)
            }
            return res
        },

        annulAbnormalOrder: async({ state, commit, dispatch },data) => {
            const res = await annulAbnormalOrderAPI(data)
            if(res) {
                commit('set_cancelAbOrderModalVisible', false)
                message.success('撤销成功')
                dispatch('getabnormalOrderList')
            }else{
                message.error('撤销失败')
            }
        },

        delWebCoupon: async({ state, commit, dispatch },couponId) => {
            const res = await removeAPI(couponId)
            if(res) {
                message.success('删除成功')
                dispatch('getWebCouponList')
            }else{
                message.error('删除失败')
            }
            return res
        },
    }
}
export default marketer

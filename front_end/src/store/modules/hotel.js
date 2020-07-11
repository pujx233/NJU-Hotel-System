import { message } from 'ant-design-vue'
import store from '@/store'
import {
    getHotelsAPI,
    getHotelByIdAPI
} from '@/api/hotel'
import {
    reserveHotelAPI
} from '@/api/order'
import {
    orderMatchCouponsAPI,
} from '@/api/coupon'
import {updateHotelByIdAPI} from "../../api/hotelManager";
import {getHotelRARAPI} from "../../api/rar";

const hotel = {
    state: {
        hotelList: [
            
        ],
        hotelListParams: {
            pageNo: 0,
            pageSize: 12
        },
        hotelListLoading: true,
        currentHotelId: '',
        currentHotelInfo: {

        },
        orderModalVisible: false,
        evaluateVisible:false,
        currentOrderId:'',
        currentOrderRoom: {

        },
        orderMatchCouponList: [

        ],
        currentHotelrar:[],
        roomList: [],
        roomVisible: false,
        addRoomVisible: false,
    },
    mutations: {
        set_hotelList: function(state, data) {
            state.hotelList = data
        },
        set_hotelListParams: function(state, data) {
            state.hotelListParams = {
                ...state.hotelListParams,
                ...data,
            }
        },
        set_hotelListLoading: function(state, data) {
            state.hotelListLoading = data
        },
        set_currentHotelId: function(state, data) {
            state.currentHotelId = data
        },
        set_currentHotelInfo: function(state, data) {
            state.currentHotelInfo = {
                ...state.currentHotelInfo,
                ...data,
            }
        },
        set_currentHotelrar: function(state, data) {
            state.currentHotelrar=data
        },
        set_orderModalVisible: function(state, data) {
            state.orderModalVisible = data
        },
        set_evaluateVisible: function(state, data) {
            state.evaluateVisible = data
        },
        set_currentOrderId:function(state,data){
            state.currentOrderId=data
        },
        set_currentOrderRoom: function(state, data) {
            state.currentOrderRoom = {
                ...state.currentOrderRoom,
                ...data,
            }
        },
        set_orderMatchCouponList: function(state, data) {
            state.orderMatchCouponList = data
        },
        set_roomList: function (state, data) {
            state.roomList = data
        },
        set_roomVisible: function (state, data) {
            state.roomVisible = data
        },
        set_addRoomVisible: function (state, data) {
            state.addRoomVisible = data
        },
    },

    actions: {
        getRoomList: async ({state, commit},hotelId) => {
            const res = await getHotelByIdAPI(hotelId)
            if(res){
                commit('set_roomList', res.rooms)
            }
            return res
        },
        getHotelList: async({commit, state}) => {
            const res = await getHotelsAPI()
            if(res){
                commit('set_hotelList', res)
                commit('set_hotelListLoading', false)
            }
        },
        getHotelById: async({commit, state}) => {
            const res = await getHotelByIdAPI(state.currentHotelId)
            if(res){
                for(let item in res.rooms){
                    if(res.rooms[item].roomType=="大床房"){
                        res.rooms[item].bedType="大床"
                    }
                    if(res.rooms[item].roomType=="双床房"){
                        res.rooms[item].bedType="双床"
                    }
                    if(res.rooms[item].roomType=="家庭房"){
                        res.rooms[item].bedType="家庭床"
                    }
                   res.rooms[item].key=item
                   res.rooms[item].breakfast="有"
                    res.rooms[item].peopleNum="1~4"
                }
                commit('set_currentHotelInfo', res)
            }
        },
        getHotelrar: async({commit, state}) => {
            const res = await getHotelRARAPI(state.currentHotelId)
            if(res){
                commit('set_currentHotelrar', res)
            }
        },
        addOrder: async({ state, commit }, data) => {
            const res = await reserveHotelAPI(data)
            if(res){
                message.success('预定成功')
                commit('set_orderModalVisible', false)
            }
            return res
        },
        getOrderMatchCoupons: async({ state, commit }, data) => {
            const res = await orderMatchCouponsAPI(data)
            if(res){
                commit('set_orderMatchCouponList', res)
            }
        },
        //陈鹏克
        updateHotel: async({ state, dispatch }, data) => {
            const params = {
                hotelId: state.currentHotelId,
                ...data,
            }
            const res = await updateHotelByIdAPI(params)
            if(res){
                message.success('修改成功')
                dispatch('getHotelList')
            }
        },
        //end
    }
}

export default hotel
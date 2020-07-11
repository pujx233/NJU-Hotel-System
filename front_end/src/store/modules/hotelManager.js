import {
    addRoomAPI,
    removeRoomAPI,
    updateRoomAPI,
    updateRoomBaseAPI
} from '@/api/hotelManager'
import {
    addHotelAPI,
}from '@/api/admin'
import {
    getAllOrdersAPI,
    leaveEarlyAPI,
    solveAbnormalOrderAPI
} from '@/api/order'
import {
    hotelAllCouponsAPI,
    hotelTargetMoneyAPI,
    roomsAPI,
    timeAPI,
    vipAPI
} from '@/api/coupon'
import {message} from 'ant-design-vue'
import {executeOrderAPI} from "../../api/order";
import {updateHotelByIdAPI} from "../../api/hotelManager";

const hotelManager = {
        state: {
            orderList: [],
            addHotelParams: {
                id: 0,
                name: '',
                address: '',
                bizRegion: '',
                hotelStar: '',
                rate: 0,
                hotelService: '',
                description: '',
                phoneNum: '',
                managerId: '',
                rooms: [],
            },
            addHotelModalVisible: false,
            addRoomParams: {
                roomType: '',
                hotelId: '',
                price: '',
                total: 0,
                curNum: 0,
            },
            addRoomModalVisible: false,
            couponVisible: false,
            addCouponVisible: false,
            activeHotelId: 0,
            couponList: [],
            amendRoomModalVisible: false,
            amendRoomNumVisible: false
        },
        mutations: {
            set_orderList: function (state, data) {
                state.orderList = data
            },
            set_amendRoomModalVisible: function (state, data) {
                state.amendRoomModalVisible = data
            },
            set_addHotelModalVisible: function (state, data) {
                state.addHotelModalVisible = data
            },
            set_addHotelParams: function (state, data) {
                state.addHotelParams = {
                    ...state.addHotelParams,
                    ...data,
                }
            },
            set_addRoomModalVisible: function (state, data) {
                state.addRoomModalVisible = data
            },
            set_addRoomParams: function (state, data) {
                state.addRoomParams = {
                    ...state.addRoomParams,
                    ...data,
                }
            },
            set_couponVisible: function (state, data) {
                state.couponVisible = data
            },
            set_activeHotelId: function (state, data) {
                state.activeHotelId = data
            },
            set_couponList: function (state, data) {
                state.couponList = data
            },
            set_addCouponVisible: function (state, data) {
                state.addCouponVisible = data
            },
            set_amendRoomNumVisible: function (state, data) {
                state.amendRoomNumVisible = data
            },
        },
        actions: {
            getAllOrders: async ({state, commit}) => {
                const res = await getAllOrdersAPI()
                if (res) {
                    commit('set_orderList', res)
                }
                return res
            },
            addHotel: async ({state, dispatch, commit}) => {
                console.log(state.addHotelParams)
                const res = await addHotelAPI(state.addHotelParams)
                if (res) {    //返回值ResponseVO.buildSuccess()中内容由true改为hotelId，前端可能需要做相应修改 by tc
                    dispatch('getHotelList')
                    commit('set_addHotelParams', {
                        id: 0,
                        name: '',
                        address: '',
                        bizRegion: '',
                        hotelStar: '',
                        rate: 0,
                        hotelService: '',
                        description: '',
                        phoneNum: '',
                        managerId: '',
                        rooms: [],
                    })
                    commit('set_addHotelModalVisible', false)
                    message.success('添加成功')
                } else {
                    message.error('添加失败')
                }
            },
            addRoom: async ({state, dispatch, commit}) => {
                const res = await addRoomAPI(state.addRoomParams)
                if (res) {
                    commit('set_addRoomModalVisible', false);
                    commit('set_addRoomParams', {
                        roomType: '',
                        hotelId: '',
                        price: '',
                        total: 0,
                        curNum: 0,
                    });
                    message.success('添加成功');
                } else {
                    message.error('添加失败')
                }
                return res
            },
            updateRoomInfo: async ({state, dispatch, commit},data) => {
                const res = await updateRoomBaseAPI(data)
                if (res) {
                    commit('set_amendRoomModalVisible', false);
                    message.success('更新成功');
                } else {
                    message.error('更新失败')
                }
                return res
            },
            updateRoomNumInfo: async ({state, dispatch, commit},data) => {
                const res = await updateRoomAPI(data)
                if (res) {
                    commit('set_amendRoomNumVisible', false);
                    message.success('更新成功');
                } else {
                    message.error('更新失败')
                }
                return res
            },
            checkoutEarly: async ({state, dispatch, commit},orderid) => {
                const res = await leaveEarlyAPI(orderid)
                if (res) {
                    message.success('提前离店成功');
                } else {
                    message.error('提前离店失败')
                }
                return res
            },
            delRoom: async({ state, dispatch }, data) => {
                const res = await removeRoomAPI(data)
                if(res){
                    message.success('删除成功')
                    dispatch('getRoomList',data.hotelId)
                }else{
                    message.error('删除失败')
                }
                return res
            },
            solveAbnormalOrder: async({ state, dispatch }, orderId) => {
                const res = await solveAbnormalOrderAPI(orderId)
                console.log(res,"酒工处理异常订单已被调用")
                if(res){
                    message.success('处理成功')
                    dispatch('getAllOrders')
                }else{
                    message.error('处理失败')
                }
                return res
            },
            getHotelCoupon: async ({state, commit}) => {
                const res = await hotelAllCouponsAPI(state.activeHotelId)
                if (res) {
                    commit('set_couponList', res);
                }
                return res
            },
            //陈鹏克
            executeOrder:async({dispatch},data)=>{
                console.log(data)
                const res= await executeOrderAPI(data)
                if(res){
                    message.success('执行成功')
                    dispatch('getAllOrders')
                }
                else{
                    message.error('执行失败')
                }
            },
            //end
            //陈鹏克
            addHotelCoupon: async ({commit, dispatch}, data) => {
                var res;
                if(data.type == 3)  res = await hotelTargetMoneyAPI(data)
                else if(data.type == 2)  res = await roomsAPI(data)
                else if(data.type == 1)  res = await vipAPI(data)
                else if(data.type==4) res = await timeAPI(data)
                if (res) {
                    dispatch('getHotelCoupon')
                    commit('set_addCouponVisible', false)
                    commit('set_couponVisible', true)
                    message.success('添加策略成功')
                } else {
                    message.error('添加失败');
                }
            }
        }
    }
;
export default hotelManager
import { axios } from '@/utils/request'
const api = {
    orderPre: '/api/order'
}
export function reserveHotelAPI(data) {
    return axios({
        url: `${api.orderPre}/addOrder`,
        method: 'POST',
        data,
    })
}
export function getAllOrdersAPI() {
    return axios({
        url: `${api.orderPre}/getAllOrders`,
        method: 'GET',
    })
}
export function getHotelOrdersAPI(data) {
    return axios({
        url: `${api.orderPre}/${data.hotelId}/getHotelOrders`,
        method: 'GET',
    })
}
export function getUserOrdersAPI(data) {
    return axios({
        url: `${api.orderPre}/${data.userId}/getUserOrders`,
        method: 'GET',
    })
}
export function cancelOrderAPI(orderId) {
    return axios({
        url: `${api.orderPre}/${orderId}/annulBookedOrder`,
        method: 'POST',//改get为post，若后来发现必须得是get，则请把下面三个post也都改成get
    })
}
export function solveAbnormalOrderAPI(orderId) {
    return axios({
        url: `${api.orderPre}/${orderId}/solveAbnormalOrder`,
        method: 'POST',
    })
}
export function executeOrderAPI(orderId) {
    return axios({
        url: `${api.orderPre}/${orderId}/executeOrder`,
        method: 'POST',
    })
}
export function leaveEarlyAPI(orderid) {
    return axios({
        url: `${api.orderPre}/${orderid}/leaveEarly`,
        method: 'POST',
    })
}
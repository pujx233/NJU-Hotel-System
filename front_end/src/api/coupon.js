import { axios } from '@/utils/request'
const api = {
    couponPre: '/api/coupon'
}
export function hotelTargetMoneyAPI(data) {
    return axios({
        url: `${api.couponPre}/targetMoney`,
        method: 'POST',
        data,
    })
}

export function hotelAllCouponsAPI(hotelId) {
    return axios({
        url: `${api.couponPre}/hotelAllCoupons`,
        method: 'GET',
        params: {hotelId: hotelId},
    })
}

export function orderMatchCouponsAPI(params) {
    return axios({
        url: `${api.couponPre}/orderMatchCoupons`,
        method: 'GET',
        params,
    })
}

export function timeAPI(data) {
    return axios({
        url: `${api.couponPre}/time`,
        method: 'POST',
        data,
    })
}

export function roomsAPI(data) {
    return axios({
        url: `${api.couponPre}/rooms`,
        method: 'POST',
        data,
    })
}

export function vipAPI(data) {
    return axios({
        url: `${api.couponPre}/vip`,
        method: 'POST',
        data,
    })
}

export function removeAPI(couponId) {
    return axios({
        url: `${api.couponPre}/${couponId}/remove`,
        method: 'POST',
    })
}

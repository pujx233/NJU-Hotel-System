import { axios } from '@/utils/request'
const api = {
    hotelPre: '/api/hotel'
}
export function addRoomAPI(data) {
    return axios({
        url: `${api.hotelPre}/roomInfo/add`,
        method: 'POST',
        data,
    })
}
export function updateRoomAPI(data) {
    return axios({
        url: `${api.hotelPre}/${data.roomNum}/roomInfo/updateNum`,
        method: 'POST',//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
        data,
    })
}
export function updateRoomBaseAPI(data) {
    return axios({
        url: `${api.hotelPre}/roomInfo/updateBase`,
        method: 'POST',//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
        data,
    })
}
export function removeRoomAPI(data) {
    return axios({
        url: `${api.hotelPre}/roomInfo/remove`,
        method: 'POST',//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
        data,
    })
}
export function updateHotelByIdAPI(data) {
    return axios({
        url: `${api.hotelPre}/${data.hotelId}/detail/update`,
        method: 'POST',
        data,
    })
}
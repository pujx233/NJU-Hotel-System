import { axios } from '@/utils/request'
const api = {
    adminPre: '/api/admin'
}
export function addHotelAPI(data) {//返回值ResponseVO.buildSuccess()中内容由true改为hotelId，前端肯需要做相应修改
    return axios({
        url: `${api.adminPre}/${data.adminId}/addHotel`,
        method: 'POST',
        data,
    })
}
export function deleteHotelAPI(data) {
    return axios({
        url: `${api.adminPre}/${data.hotelId}/${data.adminId}/removeHotel`,
        method: 'POST',//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
    })
}
export function getManagerListAPI(){
    return axios({
        url: `${api.adminPre}/getAllManagers`,
        method: 'GET'//改POST为GET
    })
}
export function addManagerAPI(data) {
    return axios({
        url: `${api.adminPre}/${data.hotelId}/addManager`,
        method: 'POST',
        data
    })
}
export function getMarketerListAPI(){
    return axios({
        url: `${api.adminPre}/getAllMarketers`,
        method: 'GET'//改POST为GET
    })
}
export function addMarketerAPI(data) {
    return axios({
        url: `${api.adminPre}/addMarketer`,
        method: 'POST',
        data
    })
}
export function getClientListAPI(){
    return axios({
        url: `${api.adminPre}/getAllClients`,
        method: 'GET'//改POST为GET
    })
}
export function updateUserInfoAPI(data) {
    return axios({
        url: `${api.adminPre}/${data.userId}/updateUserInfo`,
        method: 'POST',
        data
    })
}
export function removeUserAPI(userId) {
    return axios({
        url: `${api.adminPre}/${userId}/removeUser`,
        method: 'POST',
    })
}

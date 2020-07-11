import { axios } from '@/utils/request'
const api = {
    vipPre: '/api/vip'
}
export function registerVIPAPI(data){
    return axios({
        url: `${api.vipPre}/registerVIP`,
        method: 'POST',
        data,
    })
}
export function getVIPAPI(id){
    return axios({
        url: `${api.vipPre}/${id}/getVIPInfo`,
        method: 'POST'
    })
}
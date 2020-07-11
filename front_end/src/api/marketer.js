import { axios } from '@/utils/request'
const api = {
    marketerPre: '/api/marketer'
}
export function retrieveAbnormalOrdersAPI(){
    return axios({
        url: `${api.marketerPre}/retrieveAbnormalOrders`,
        method: 'GET'
    })
}
export function annulAbnormalOrderAPI(data) {
    return axios({
        url: `${api.marketerPre}/annulAbnormalOrder/${data.orderId}/${data.percent}`,
        method: 'POST',//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
    })
}
export function buyCreditAPI(data){
    return axios({
        url: `${api.marketerPre}/buyCredit/${data.email}/${data.credit}`,
        method: 'POST',//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
    })
}




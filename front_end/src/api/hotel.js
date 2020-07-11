import { axios } from '@/utils/request'
const api = {
    hotelPre: '/api/hotel'//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
}
export function getHotelsAPI() {
    return axios({
        url: `${api.hotelPre}/all`,
        method: 'GET',
    })
}
export function getHotelByIdAPI(param) {
    return axios({
        url: `${api.hotelPre}/${param}/detail/retrieve`,
        method: 'GET',
    })
}

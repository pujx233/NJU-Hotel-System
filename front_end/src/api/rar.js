import { axios } from '@/utils/request'
const api = {
    rarPre: '/api/rar'
}
export function rateAndReviewAPI(data){
    return axios({
        url: `${api.rarPre}/rateAndReview`,
        method: 'POST',//详细接口格式见相应controller，前端人员有修改controller的权力，调整至接口相符合为止
        data,
    })
}
export function getHotelRARAPI(hotelId) {
    return axios({
        url: `${api.rarPre}/${hotelId}/getHotelRAR`,
        method: 'GET',
    })
}
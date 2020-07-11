const getters = {
  //user
  token: state => state.user.token,
  userId: state => state.user.userId,
  userInfo: state => state.user.userInfo,
  VIPInfo: state => state.user.VIPInfo,
  userOrderList: state => state.user.userOrderList,
  hotelListLoading: state => state.hotel.hotelListLoading,
  hotelList: state => state.hotel.hotelList,
  currentHotelInfo: state => state.hotel.currentHotelInfo,
  currentHotelId: state => state.hotel.currentHotelId,
  orderModalVisible: state => state.hotel.orderModalVisible,
  currentOrderRoom: state => state.hotel.currentOrderRoom,
  orderMatchCouponList: state => state.hotel.orderMatchCouponList,
  evaluateVisible:state => state.hotel.evaluateVisible,
  currentOrderId:state=>state.hotel.currentOrderId,
  currentHotelrar:state=>state.hotel.currentHotelrar,
  //admin
  managerList: state => state.admin.managerList,
  clientList: state => state.admin.clientList,
  marketerList: state => state.admin.marketerList,
  addManagerModalVisible: state => state.admin.addManagerModalVisible,
  addMarketerModalVisible: state => state.admin.addMarketerModalVisible,
  amendUserModalVisible: state => state.admin.amendUserModalVisible,
  addManagerParams: state => state.admin.addManagerParams,
  //hotelManager
  orderList: state => state.hotelManager.orderList,
  addHotelModalVisible: state => state.hotelManager.addHotelModalVisible,
  // changeHotelVisible:state=>state.hotelManager.changeHotelVisible,
  addRoomModalVisible: state => state.hotelManager.addRoomModalVisible,
  couponVisible: state => state.hotelManager.couponVisible,
  addCouponVisible: state => state.hotelManager.addCouponVisible,
  activeHotelId: state => state.hotelManager.activeHotelId,
  couponList: state => state.hotelManager.couponList,
  amendRoomModalVisible: state => state.hotelManager.amendRoomModalVisible,
  amendRoomNumVisible: state => state.hotelManager.amendRoomNumVisible,
  //hotel
  roomList: state => state.hotel.roomList,
  roomVisible: state => state.hotel.roomVisible,
  addRoomVisible: state => state.hotel.addRoomVisible,
  //marketer
  addCreditModalVisible: state => state.marketer.addCreditModalVisible,
  webCouponList: state => state.marketer.webCouponList,
  addWebCouponModalVisible: state => state.marketer.addWebCouponModalVisible,
  cancelAbOrderModalVisible: state => state.marketer.cancelAbOrderModalVisible,
  abnormalOrderList: state => state.marketer.abnormalOrderList,
  }

  
  export default getters
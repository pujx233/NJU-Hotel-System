<template>
    <a-layout>
        <a-layout-content>
            <div class="hotelDetailCard">
                <h1>
                    {{ currentHotelInfo.title }}
                </h1>
                <div class="hotel-info">
                    <a-card style="width: 240px">
                        <img
                            alt="example"
                            src="@/assets/cover.jpeg"
                            slot="cover"
                            referrerPolicy="no-referrer"
                            />
                    </a-card>
                    <div class="info">
                        <div class="items" v-if="currentHotelInfo.name">
                            <span class="label">酒店名称：</span>
                            <span class="value">{{ currentHotelInfo.name }}</span>
                        </div>
                        <div class="items" v-if="currentHotelInfo.address">
                            <span class="label">地址:</span>
                            <span class="value">{{ currentHotelInfo.address }}</span>
                        </div>
                        <div class="items" v-if="currentHotelInfo.rate">
                            <span class="label">评分:</span> 
                            <span class="value">{{ currentHotelInfo.rate }}</span>
                        </div>
                        <div class="items" v-if="currentHotelInfo.hotelStar">
                            <span class="label">星级:</span> 
                            <a-rate style="font-size: 15px" :value="currentHotelInfo.rate" disabled allowHalf/>
                        </div>
                        <div class="items" v-if="currentHotelInfo.description">
                            <span class="label">酒店简介:</span> 
                            <span class="value">{{ currentHotelInfo.description }}</span>
                        </div>
                    </div>
                </div>
                <a-divider></a-divider>
                <a-tabs>
                    <a-tab-pane tab="房间信息" key="1">
                        <RoomList :rooms="currentHotelInfo.rooms"></RoomList>
                    </a-tab-pane>
                    <a-tab-pane tab="酒店详情" key="2">
                        <a-form :form="form" style="margin-top: 30px" >
                            <a-form-item label="名称" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <span>{{ currentHotelInfo.name}}</span>
                            </a-form-item>
                            <a-form-item label="商圈" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <span>{{ currentHotelInfo.bizRegion }}</span>
                        </a-form-item>
                            <a-form-item label="酒店设施" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <a-select
                                        mode="multiple"
                                        :default-value="hotelServicefacilities"
                                        style="width: 100%"
                                        placeholder="至少选择一项"
                                        @change="handleChange"
                                        v-if="modify"

                                >
                                    <a-select-option v-for="(item) in facilities" :key="item">
                                        {{ item }}
                                    </a-select-option>
                                </a-select>
                                <span v-else>{{ currentHotelInfo.hotelService }}</span>
                            </a-form-item>
                            <a-form-item label="星级" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <span>{{ currentHotelInfo.hotelStar }}</span>
                            </a-form-item>
                            <a-form-item label="电话" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <a-input
                                        placeholder="请填写电话"
                                        v-decorator="['phoneNum', { rules: [{ required: false, message: '请输入电话' }] }]"
                                        v-if="modify"
                                />
                                <span v-else>{{ currentHotelInfo.phoneNum }}</span>
                            </a-form-item>
                            <a-form-item label="评分" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <span>{{ currentHotelInfo.rate }}</span>
                            </a-form-item>
                            <a-form-item label="地址" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <a-input
                                        placeholder="请填写地址"
                                        v-decorator="['address', { rules: [{ required: false, message: '请输入地址' }] }]"
                                        v-if="modify"
                                />
                                <span v-else>{{ currentHotelInfo.address }}</span>
                            </a-form-item>
                            <a-form-item label="简介" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                                <a-input
                                        placeholder="请填写简介"
                                        v-decorator="['description', { rules: [{ required: false, message: '请输入简介' }] }]"
                                        v-if="modify"
                                />
                                <span v-else>{{ currentHotelInfo.description }}</span>
                            </a-form-item>
                            <a-form-item :wrapper-col="{ span: 8, offset: 4 }" v-if="currentHotelInfo.managerId==userId&&modify==false&&userInfo.userType=='HotelManager'">
                                <a-button type="primary" @click="modifyInfo">
                                    修改信息
                                </a-button>
                            </a-form-item>
                            <a-form-item :wrapper-col="{ span: 12, offset: 5 }" v-if="modify">
                                <a-button type="primary" @click="saveModify">
                                    保存
                                </a-button>
                                <a-button type="default" style="margin-left: 30px" @click="cancelModify">
                                    取消
                                </a-button>
                            </a-form-item>
                        </a-form>
                    </a-tab-pane>
                    <a-tab-pane tab="评论" key="3">
                        <RarList :rars="currentHotelrar"></RarList>
                    </a-tab-pane>
                </a-tabs>
            </div>
        </a-layout-content>
    </a-layout>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'
import RoomList from './components/roomList'
import RarList from './components/rarList'
export default {
    inject:['reload'],
    name: 'hotelDetail',
    components: {
        RarList,
        RoomList,
    },
    data() {
        return {
            hotelService:"",
            hotelServicefacilities:[],
            modify: false,
            form: this.$form.createForm(this, { name: 'coordinated' }),
            facilities: ['Wifi','热水','叫醒服务'],
            rooms:[]
        }
    },
    computed: {
        ...mapGetters([
            'currentHotelInfo',
            'userId',
            'userInfo',
            'currentHotelrar'
        ])
    },
    mounted() {
        this.set_currentHotelId(Number(this.$route.params.hotelId))
        this.getHotelById()
        this.getHotelrar()
        console.log(this.currentHotelrar)
        this.hotelServicefacilities = this.currentHotelInfo.hotelService.split(",")
    },
    beforeRouteUpdate(to, from, next) {
        this.set_currentHotelId(Number(to.params.hotelId))
        this.getHotelById()
        next()
    },
    methods: {
        ...mapMutations([
            'set_currentHotelId',
        ]),
        ...mapActions([
            'getHotelById',
            'updateHotel',
            'getHotelrar'
        ]),
        handleChange(value) {
            this.hotelService = `${value}`;
            console.log(this.hotelService)
        },
        modifyInfo() {
            setTimeout(() => {
                console.log(this.currentHotelInfo)
                this.form.setFieldsValue({
                    'hotelService':this.currentHotelInfo.hotelService,
                    'phoneNum': this.currentHotelInfo.phoneNum,
                    'address': this.currentHotelInfo.address,
                    'description': this.currentHotelInfo.description,
                })
            }, 0)
            this.modify = true
        },
        cancelModify() {
            this.modify = false
        },
        saveModify() {
            this.set_currentHotelId(this.currentHotelInfo.id)
            this.form.validateFields((err, values) => {
                const data = {
                    name: this.currentHotelInfo.name,
                    bizRegion: this.currentHotelInfo.bizRegion,
                    hotelService: this.hotelService,
                    hotelStar: this.currentHotelInfo.hotelStar,
                    phoneNum: this.form.getFieldValue('phoneNum'),
                    rate: this.currentHotelInfo.rate,
                    address: this.form.getFieldValue('address'),
                    description: this.form.getFieldValue('description'),
                }
                this.updateHotel(data).then(() => {
                    this.modify = false
                    this.currentHotelInfo.hotelService=this.hotelService
                    this.hotelServicefacilities=this.currentHotelInfo.hotelService.split(",")
                    this.currentHotelInfo.address=this.form.getFieldValue('address')
                    this.currentHotelInfo.phoneNum=this.form.getFieldValue('phoneNum')
                    this.currentHotelInfo.description=this.form.getFieldValue('description')
                })
            });
        },
    }
}
</script>
<style scoped lang="less">
    .hotelDetailCard {
        padding: 50px 50px;
    }
    .hotel-info {
        display: flex;
        align-items: stretch;
        justify-content: flex-start;
        .info{
            padding: 10px 0;
            display: flex;
            flex-direction: column;
            margin-left: 20px;
            .items {
                display: flex;
                align-items: center;
                margin-bottom: 10px;
                .label{
                    margin-right: 10px;
                    font-size: 18px;
                }
                .value {
                    margin-right: 15px
                }
            }
        }
    }
</style>
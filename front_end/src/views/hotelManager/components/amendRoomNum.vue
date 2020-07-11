<template>
    <a-modal
            :visible="amendRoomNumVisible"
            title="修改余房数量"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="变化数量" >
                <a-input-number id="inputNumber" v-model="value"  @change="onChange" />
            </a-form-item>
            <a-form-item label="" >
                <span class="prompt">正数代表增加，负数代表减少</span>
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'amendRoomNum',
        props:['record'],
        data() {
            return {
                formItemLayout: {
                    labelCol: {
                        xs: { span: 12 },
                        sm: { span: 6 },
                    },
                    wrapperCol: {
                        xs: { span: 24 },
                        sm: { span: 16 },
                    },
                },
                value: 1,
            }
        },
        computed: {
            ...mapGetters([
                'amendRoomNumVisible',
                'activeHotelId',
            ])
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'amendRoomNum' });
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_amendRoomNumVisible',
                'set_roomVisible'
            ]),
            ...mapActions([
                'updateRoomNumInfo'
            ]),
            cancel() {
                this.set_amendRoomNumVisible(false)
                this.set_roomVisible(true)
            },
            onChange(value) {
            },
            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            hotelId: this.activeHotelId,
                            roomType: this.record.roomType,
                            roomNum: this.value,
                        }
                        console.log("提交的数据",data)
                        this.updateRoomNumInfo(data).then(res=>{
                            this.set_amendRoomNumVisible(false)
                            this.set_roomVisible(true)
                            this.$emit('handle-submit')
                        })

                    }
                });
            },
        }
    }
</script>
<style>
    .prompt{
        color: grey;
        font-size: small;
        margin-left: 50px;
    }
</style>
<template>
    <a-modal
            :visible="evaluateVisible"
            title="评价酒店"
            cancelText="取消"
            okText="确认"
            @cancel="cancelevaluate"
            @ok="handleSubmit"
    >
        <a-form :form="form">
            <a-form-item v-bind="formItemLayout" label="评分">
                <a-select
                        v-decorator="[
                        'rate',
                        { rules: [{ required: true, message: '请选择评分' }] },
                    ]"
                        placeholder="请选择评分"
                        @change="changerate"
                >
                    <a-select-option :value="1">
                        1
                    </a-select-option>
                    <a-select-option :value="2">
                        2
                    </a-select-option>
                    <a-select-option :value="3">
                        3
                    </a-select-option>
                    <a-select-option :value="4">
                        4
                    </a-select-option>
                    <a-select-option :value="5">
                        5
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="评论">
                <a-textarea
                        v-decorator="[
                                                     'comment',
                        { rules: [{required: false, message: '请填写评论', }] }
                        ]"
                        :auto-size="{ minRows: 2, maxRows: 6 }"
                />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'evaluate',
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
            }
        },
        computed: {
            ...mapGetters([
                'evaluateVisible',
                'currentOrderId',
            ]),

        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'evaluate' });
        },
        methods: {
            ...mapMutations([
                'set_evaluateVisible'
            ]),
            ...mapActions([
                'rateandreview',
            ]),
            changerate(){

            },
            cancelevaluate() {
                this.set_evaluateVisible(false)
                this.form.resetFields()
            },
            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            rate: this.form.getFieldValue('rate'),
                            orderId:this.currentOrderId,

                            review:this.form.getFieldValue('comment')
                        }
                        this.rateandreview(data)
                        this.set_evaluateVisible(false)
                        this.form.resetFields()
                    }
                });
            },
        },
    }
</script>
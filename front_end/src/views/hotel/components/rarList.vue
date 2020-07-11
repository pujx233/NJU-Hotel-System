<template>
    <div class="rar-list">
        <div class="list">
            <a-table
                    :columns="columns"
                    :dataSource="rars"
            >
            </a-table>
        </div>
    </div>
</template>
<script>
    import { mapGetters, mapActions, mapMutations } from 'vuex'
    const columns = [
        {
            title: '评分',
            dataIndex: 'rate',
            key: 'rate',
        },
        {
            title: '评价',
            dataIndex: 'comment',
            key: 'comment',
        },
        {
            title: '评论时间',
            dataIndex: 'createDate',
            key: 'createDate',
        },
    ];
    export default {
        name:'roomList',
        props: {
            rars: {
                type: Array
            }
        },
        data() {
            return {
                columns,
            }
        }
    }
</script>
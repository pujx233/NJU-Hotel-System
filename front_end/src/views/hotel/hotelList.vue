<template>
    <div class="hotelList">
    <a-layout>
        <a-cascader :options="options" placeholder="筛选条件" style="margin-left: 23px;width: 200px" @change="onChange" />
        <a-layout-content style="min-width: 800px">

          <a-spin :spinning="hotelListLoading">
              <div class="card-wrapper">
                <HotelCard :hotel="item" v-for="item in activeHotellist" :key="item.index" @click.native="jumpToDetails(item.id)"></HotelCard>
                <div v-for="item in emptyBox" :key="item.name" class="emptyBox ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
                </div>
                <a-pagination showQuickJumper :total="hotelList.totalElements" :defaultCurrent="1" @change="pageChange"></a-pagination>
            </div>
          </a-spin>
      </a-layout-content>
    </a-layout>
  </div>
</template>
<script>
import HotelCard from './components/hotelCard'
import { mapGetters, mapActions, mapMutations } from 'vuex'

export default {
  name: 'home',
  components: {
    HotelCard
  },
  data(){
    return{
        options: [
            {
                value: 'bizRegion',
                label: '商圈',
                children: [
                    {
                        value: '西单',
                        label: '西单',
                    },
                    {
                        value: '王府井',
                        label: '王府井',
                    },
                    {
                        value: '大栅栏',
                        label: '大栅栏',
                    },

                ],
            },
            {
                value: 'rate',
                label: '评分',
                children: [
                    {
                        value: '1',
                        label: '1分以上',
                    },
                    {
                        value: '2',
                        label: '2分以上',
                    },
                    {
                        value: '3',
                        label: '3分以上',
                    },
                    {
                        value: '4',
                        label: '4分以上',
                    },
                    {
                        value: '5',
                        label: '5分',
                    },
                ],
            },
            {
                value: 'hotelStar',
                label: '星级',
                children: [
                    {
                        value: '三星级',
                        label: '三星级',
                    },
                    {
                        value: '四星级',
                        label: '四星级',
                    },
                    {
                        value: '五星级',
                        label: '五星级',
                    }
                ],
            }
        ],
        activeHotellist:[],
        emptyBox: [{ name: 'box1' }, { name: 'box2'}, {name: 'box3'}]
    }
  },
  async mounted() {
    await this.getHotelList()
      this.activeHotellist=this.hotelList
  },
  computed: {
    ...mapGetters([
      'hotelList',
      'hotelListLoading'
    ])
  },
  methods: {
    ...mapMutations([
      'set_hotelListParams',
      'set_hotelListLoading'
    ]),
    ...mapActions([
      'getHotelList'
    ]),
      onChange(value) {
          console.log(value);
          this.activeHotellist=[];
          if(value==""){
              this.activeHotellist=this.hotelList;
          }
          if(value[0]=="bizRegion"){
              for(let item in this.hotelList){
                  if(this.hotelList[item].bizRegion==value[1]){
                      this.activeHotellist.push(this.hotelList[item]);
                  }
              }
          }
          if(value[0]=="rate"){
              for(let item in this.hotelList){
                  if(this.hotelList[item].rate>=value[1]){
                      this.activeHotellist.push(this.hotelList[item]);
                  }
              }
          }
          if(value[0]=="hotelStar"){
              for(let item in this.hotelList){
                  if(this.hotelList[item].hotelStar==value[1]){
                      this.activeHotellist.push(this.hotelList[item]);
                  }
              }
          }

      },
    pageChange(page, pageSize) {
      const data = {
        pageNo: page - 1
      }
      this.set_hotelListParams(data)
      this.set_hotelListLoading(true)
      this.getHotelList()
    },
    jumpToDetails(id){
        console.log(id)
      this.$router.push({ name: 'hotelDetail', params: { hotelId: id }})
    }
  }
}
</script>
<style scoped lang="less">
  .hotelList {
    text-align: center;
    padding: 50px 0;
    .emptyBox {
      height: 0;
      margin: 10px 10px
    }
    .card-wrapper{
      display: flex;
      justify-content: space-around;
      flex-wrap: wrap;
      flex-grow: 3;
      min-height: 800px
    }
    .card-wrapper .card-item {
      margin: 30px;
      position: relative;
      height: 188px;
    }
  }
</style>
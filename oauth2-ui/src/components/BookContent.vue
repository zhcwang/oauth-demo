<template>
  <div style="background-color: #ececec; padding: 20px">
    <div>
      <a-input-search
          v-model:value="searchValue"
          placeholder="input search text"
          style="width: 200px"
          @search="onSearch"
      />
      <a-tag color="blue" style="margin:2px 10px; line-height: 26px; vertical-align: center;">Total: {{ total }}</a-tag>
    </div>
    <a-row :gutter="32">
      <a-col v-for="book in books" v-bind:key="book.id" :span="6">
        <a-card class="book_content" hoverable @click="showDetail(book)">
          <template #cover>
            <img :src="book.image">
          </template>
          <a-card-meta :title="book.name" class="book_meta">
            <template #description>
              <p class="book_description">{{ book.description }}</p>
            </template>
          </a-card-meta>
        </a-card>
      </a-col>
    </a-row>
    <a-drawer
        :title=this.selected.name
        placement="right"
        :closable="true"
        v-model:visible="detailVisible"
        :after-visible-change="afterVisibleChange"
    >
      <a-comment>
        <template #author><a>{{ this.selected.createdBy }}</a></template>
        <template #content>
          <p>
            {{ this.selected.reason }}
          </p>
        </template>
        <template #datetime>
          <a-tooltip :title="moment().format('YYYY-MM-DD HH:mm:ss')">
            <span>{{ moment().fromNow() }}</span>
          </a-tooltip>
        </template>
      </a-comment>
    </a-drawer>
  </div>
</template>

<script>
import {defineComponent, ref} from "vue";
import axios from "axios";
import moment from "moment";
import config from "../config"

export default defineComponent({
  components: {},
  name: 'BookContent',

  data() {
    return {
      books: [
        // {
        //   "id": 1,
        //   "image": "https://t7.baidu.com/it/u=355704943,1318565630&fm=193&f=GIF",
        //   "title": "Beyond Earth",
        //   "description": "This is a book about universe12121212121213321312312312313123123131231232131231231232"
        // }
      ],
      total: 0,
      searchValue: ref(''),
      pageSize: 8,
      pageNumber: -1,
      detailVisible: false,
      selected: {},
    }
  },
  methods: {
    onSearch(searchValue) {
      this.total = 0
      this.pageNumber = -1
      this.searchValue = searchValue
      this.fetchNextPage((res) => {
        this.total = res.data.total
        this.books = res.data.books
        this.pageNumber = this.pageNumber + 1
      })
    },
    scrollBottom() {
      let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      let clientHeight = document.documentElement.clientHeight;
      let scrollHeight = document.documentElement.scrollHeight;
      if (Math.ceil(scrollTop + clientHeight) >= Math.floor(scrollHeight)) {
        this.fetchNextPage(this.onFetchSuccess)
      }
    },
    onFetchSuccess(res) {
      if (res.data.books.length > 0) {
        this.total = res.data.total
        this.books = this.books.concat(res.data.books)
        this.pageNumber = this.pageNumber + 1
      }
    },
    fetchNextPage(successCallback) {
      let params = {
        "pageSize": this.pageSize,
        "pageNum": this.pageNumber + 1,
      }
      if (this.searchValue) {
        params['bookName'] = this.searchValue
      }
      axios({
        method: 'get',
        url: `${config.app.apiSeverUrl}/api/books?${new URLSearchParams(params)}`,
      }).then(res => {

        successCallback(res)
      }).catch(e => {
        return Promise.reject(e)
      })
    },
    showDetail(book) {
      this.detailVisible = true
      this.selected = book
    },
    afterVisibleChange(bool) {
      console.log('visible', bool);
    },
    moment,
  },

  created() {
    window.addEventListener('scroll', this.scrollBottom);
    this.fetchNextPage(this.onFetchSuccess)
  }
});
</script>

<style>
.book_content {
  margin-top: 10px;
}

.book_content img {
  height: 200px;
}

.book_meta {
  height: 50px;
}

.book_description {
  height: 30px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}


</style>

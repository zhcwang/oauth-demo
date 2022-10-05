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
        <a-card class="book_content" hoverable>
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
  </div>
</template>

<script>
import {defineComponent, ref} from "vue";
import axios from "axios";

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
      pageNumber: 0,
    }
  },
  methods: {
    onSearch(searchValue) {
      alert(searchValue)
    },
    scrollBottom() {
      let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      let clientHeight = document.documentElement.clientHeight;
      let scrollHeight = document.documentElement.scrollHeight;
      if (Math.ceil(scrollTop + clientHeight) >= Math.floor(scrollHeight)) {
        this.fetchNextPage()
      }
    },
    fetchNextPage() {
      let params = {
        "pageSize": this.pageSize,
        "pageNum": this.pageNumber,
      }
      if (this.searchValue) {
        params['bookName'] = this.searchValue
      }
      axios({
        method: 'get',
        url: `http://localhost:8085/api/books?${new URLSearchParams(params)}`,
      }).then(res => {
        if (res.data.books.length > 0) {
          this.total = res.data.total
          this.books = this.books.concat(res.data.books)
          this.pageNumber = this.pageNumber + 1
        }
        console.log(this.pageNumber)
      }).catch(e => {
        return Promise.reject(e)
      })
    },
  },

  created() {
    window.addEventListener('scroll', this.scrollBottom);
    this.fetchNextPage()
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

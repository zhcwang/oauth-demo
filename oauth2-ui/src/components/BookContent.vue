<template>
  <div style="background-color: #ececec; padding: 20px">
    <div>
      <a-input-search
          v-model:value="searchValue"
          placeholder="input search text"
          style="width: 200px"
          @search="onSearch"
      />
    </div>
    <a-row :gutter="32">
      <a-col :span="6" v-for="book in books" v-bind:key="book.id">
        <a-card hoverable class="book_content">
          <template #cover>
            <img :src="book.image">
          </template>
          <a-card-meta class="book_meta" :title="book.title">
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
        {
          "id": 1,
          "image": "https://t7.baidu.com/it/u=355704943,1318565630&fm=193&f=GIF",
          "title": "Beyond Earth",
          "description": "This is a book about universe12121212121213321312312312313123123131231232131231231232"
        },
        {
          "id": 2,
          "image": " https://t7.baidu.com/it/u=4198287529,2774471735&fm=193&f=GIF",
          "title": "Beyond Earth",
          "description": "This is a book about universe"
        },
        {
          "id": 3,
          "image": "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF",
          "title": "Beyond Earth",
          "description": "This is a book about universe"
        },
        {
          "id": 4,
          "image": "https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png",
          "title": "Beyond Earth",
          "description": "This is a book about universe"
        },
        {
          "id": 5,
          "image": "https://t7.baidu.com/it/u=3911840071,2534614245&fm=193&f=GIF",
          "title": "Beyond Earth",
          "description": "This is a book about universe"
        },
        {
          "id": 6,
          "image": "https://t7.baidu.com/it/u=2374506090,1216769752&fm=193&f=GIF",
          "title": "Beyond Earth",
          "description": "This is a book about universe"
        }
      ],
      searchValue: ref('')
    }
  },
  methods: {
    logout() {
      alert('quit')
    },
    onSearch(searchValue) {
      alert(searchValue)
    },
  },
  created() {
    axios({
      method: 'get',
      url: "http://localhost:8085/api/books?pageNum=0&pageSize=10",
    }).then(res => {
      console.log("resp" + JSON.stringify(res))
      this.books = res.data
    })
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

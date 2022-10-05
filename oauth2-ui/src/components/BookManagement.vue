<template>
  <div v-show="addMode === true">
    <BookEdit @bookSaved="bookSaved" @bookCanceled="bookCanceled"></BookEdit>
  </div>
  <div v-show="addMode === false">
    <div class="management_book_operation">
      <a-button type="primary" class="btn_add_book" @click="handleAddBook">Add Book</a-button>
    </div>
    <a-table
        :columns="columns"
        :row-key="record => record.id"
        :data-source="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
    >
      <template #name="{ record }">{{ record.name }}</template>
      <template #image="{ record }">
        <a-image :src=record.image></a-image>
      </template>
      <!--      <template #image="{ record }"><a :href=record.image target="_blank">{{ record.image }}</a></template>-->
      <template #expandedRowRender="{ record }">
        <p style="margin: 0">
          {{ record.reason }}
        </p>
      </template>
      <template #action="{ record }">
        <!--        <a @click="handleDeleteBook(record)">Delete</a>-->
        <a @click="showModal(record)">Delete</a>
        <a-modal
            title="Confirmation"
            v-model:visible="visible"
            :confirm-loading="confirmLoading"
            @ok="handleOk()"
        >
          <p>Sure to delete the resource?</p>
        </a-modal>
      </template>
    </a-table>
  </div>


</template>
<script>

import {usePagination} from 'vue-request';
import {computed, defineComponent, ref} from 'vue';
import BookEdit from "@/components/BookEdit";
import axios from "axios";

const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    sorter: true,
    width: '20%',
    slots: {
      customRender: 'name',
    },
  },
  {
    title: 'Description',
    dataIndex: 'description',
    sorter: false,
    width: '20%',
    slots: {
      customRender: 'description',
    },
  },
  {
    title: 'Cover',
    dataIndex: 'image',
    sorter: false,
    width: '20%',
    slots: {
      customRender: 'image',
    },
  },
  {
    title: 'Created Time',
    dataIndex: 'createdOn',
    sorter: true,
    width: '20%',
    slots: {
      customRender: 'createdOn',
    },
  },
  {
    title: 'Action',
    dataIndex: '',
    key: 'x',
    width: '20%',
    slots: {
      customRender: 'action',
    },
  },
];

const queryData = params => {
  let queryParams = Object.assign({}, params)
  queryParams.page = queryParams.page - 1
  for (let param in queryParams) {
    if (queryParams[param] === undefined
        || queryParams[param] === null
        || queryParams[param] === ""
    ) {
      delete queryParams[param];
    }
  }
  return axios({
    method: 'get',
    url: `http://localhost:8085/api/books/_my?${new URLSearchParams(queryParams)}`,
  });
};

export default defineComponent({
  components: {BookEdit},
  setup() {
    const total = ref(10)
    const {
      data: dataSource,
      run,
      loading,
      current,
      pageSize,
    } = usePagination(queryData, {
      formatResult: res => {
        let books = res.data.books
        for (let book of books) {
          book.createdOn = new Date(book.createdOn).toJSON().replace(/T|Z|(\.\D{3})/g, " ").trim();
        }
        total.value = res.data.total
        return books;
      },
      pagination: {
        currentKey: 'page',
        pageSizeKey: 'results',
      },
    });
    const dataSourceRef = ref(dataSource)
    const pagination = computed(() => ({
      total: total.value,
      current: current.value,
      pageSize: pageSize.value,
    }));
    let addMode = ref(false)
    const sortFieldRef = ref(null)
    const sortOrderRef = ref(null)

    const handleTableChange = (pag, filters, sorter) => {
      sortFieldRef.value = sorter.field
      sortOrderRef.value = sorter.order
      run({
        results: pag.pageSize,
        page: pag?.current,
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...filters,
      });
    };

    function handleDeleteBook(record) {
      axios({
        method: 'delete',
        url: `http://localhost:8085/api/books/${record.id}`,
      }).then(() => {
        reloadTableData()
      }).catch(e => {
        return Promise.reject(e)
      });

    }

    function handleAddBook() {
      addMode.value = !addMode.value
    }

    function reloadTableData() {
      let params = {
        "results": pageSize.value,
        "page": current.value - 1,
      }
      if (sortFieldRef.value && sortOrderRef.value) {
        params['sortField'] = sortFieldRef.value
        params['sortOrder'] = sortOrderRef.value
      }
      axios({
        method: 'get',
        url: `http://localhost:8085/api/books/_my?${new URLSearchParams(params)}`,
      }).then(res => {
        dataSourceRef.value = res.data.books
        total.value = res.data.total
      }).catch(e => {
        return Promise.reject(e)
      });
    }

    function bookSaved(e) {
      console.log('receive bookSaved', e)
      addMode.value = false


      reloadTableData();
    }

    function bookCanceled(e) {
      console.log('receive booCanceled', e)
      addMode.value = false
    }

    const visible = ref(false);
    const confirmLoading = ref(false);
    const toDelete = ref(0)
    const showModal = (record) => {
      visible.value = true;
      toDelete.value = record.id
    };
    const handleOk = () => {
      confirmLoading.value = true;
      axios({
        method: 'delete',
        url: `http://localhost:8085/api/books/${toDelete.value}`,
      }).then(() => {
        confirmLoading.value = false;
        visible.value = false;
        reloadTableData()
      }).catch(e => {
        confirmLoading.value = false;
        return Promise.reject(e)
      });
    };

    return {
      dataSource,
      pagination,
      loading,
      addMode,
      columns,
      handleTableChange,
      handleDeleteBook,
      handleAddBook,
      bookSaved,
      bookCanceled,
      showModal,
      handleOk,
      confirmLoading,
      visible
    };
  },
});
</script>


<style>
.btn_add_book {
  float: right;
  margin: 10px 10px;

}
</style>
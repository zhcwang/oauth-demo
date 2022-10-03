<template>
  <div v-show="addMode === true">
    <BookEdit></BookEdit>
  </div>
  <div v-show="addMode === false">
    <div class="management_book_operation">
      <a-button type="primary" class="btn_add_book" @click="handleAddBook">Add Book</a-button>
    </div>
    <a-table
        :columns="columns"
        :row-key="record => record.login.uuid"
        :data-source="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
    >
      <template #name="{ text }">{{ text.first }} {{ text.last }}</template>
      <template #expandedRowRender="{ record }">
        <p style="margin: 0">
          {{ record.name }}
        </p>
      </template>
      <template #action="{ record }">
        <a @click="handleDeleteBook(record)">Delete</a>
      </template>
    </a-table>
  </div>


</template>
<script>

import {usePagination} from 'vue-request';
import {computed, defineComponent, ref} from 'vue';
import BookEdit from "@/components/BookEdit";

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
    title: 'Reason',
    dataIndex: 'reason',
    sorter: false,
    width: '20%',
    slots: {
      customRender: 'reason',
    },
  },
  {
    title: 'Created',
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
  return `https://randomuser.me/api?noinfo&${new URLSearchParams(params)}`;
};

export default defineComponent({
  components: {BookEdit},
  setup() {
    const {
      data: dataSource,
      run,
      loading,
      current,
      pageSize,
    } = usePagination(queryData, {
      formatResult: res => res.results,
      pagination: {
        currentKey: 'page',
        pageSizeKey: 'results',
      },
    });
    const pagination = computed(() => ({
      total: 200,
      current: current.value,
      pageSize: pageSize.value,
    }));
    let addMode = ref(false)

    const handleTableChange = (pag, filters, sorter) => {
      run({
        results: pag.pageSize,
        page: pag?.current,
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...filters,
      });
    };

    function handleDeleteBook(record) {
      this.dataSource = this.dataSource.filter(item => item.email !== record.email);
    }

    function handleAddBook() {
      console.log(addMode)
      addMode.value = !addMode.value
    }

    return {
      dataSource,
      pagination,
      loading,
      addMode,
      columns,
      handleTableChange,
      handleDeleteBook: handleDeleteBook,
      handleAddBook,
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
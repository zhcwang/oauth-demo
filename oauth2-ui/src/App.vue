<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible>
      <div class="logo"/>
      <a-menu theme="dark" v-model:selectedKeys="selectedKeys" mode="inline">
        <a-menu-item key="/books" @click="changeMenu('books')">
          <file-outlined/>
          <span>Books</span>
        </a-menu-item>
        <a-menu-item key="/management" @click="changeMenu('management')">
          <user-outlined/>
          <span>Management</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0">
        <UserProfile msg="UserProfile"></UserProfile>
      </a-layout-header>
      <a-layout-content style="margin: 0 16px">
        <div :style="{ background: '#fff', minHeight: '360px', marginTop: '16px' }">
          <router-view></router-view>
        </div>
      </a-layout-content>
      <a-layout-footer>
        <page-footer></page-footer>
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script>
import {FileOutlined, UserOutlined,} from '@ant-design/icons-vue';
import {defineComponent, ref} from 'vue';
import UserProfile from "@/components/Profile";
import PageFooter from "@/components/Footer";

export default defineComponent({
  components: {
    PageFooter,
    UserProfile,
    UserOutlined,
    FileOutlined,
  },

  data() {
    return {
      collapsed: ref(false),
      selectedKeys: ref([this.$router.path]),
    };
  },

  methods: {
    changeMenu(route) {

      this.$router.push(route)
    },
  }

});

</script>

<style>
#components-layout-demo-side .logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.3);
}

.site-layout .site-layout-background {
  background: #fff;
}

[data-theme='dark'] .site-layout .site-layout-background {
  background: #141414;
}
</style>
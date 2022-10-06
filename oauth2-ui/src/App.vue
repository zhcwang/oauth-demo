<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible>
      <div class="logo">
      </div>
      <a-menu
          mode="inline"
          theme="dark"
          v-model:selectedKeys="selectedKeys"
      >
        <a-menu-item key="/books">
          <file-outlined/>
          <span><router-link to="/books" style="padding-left: 10px"><span style="color: white">Books</span></router-link></span>
        </a-menu-item>
        <a-menu-item key="/management">
          <user-outlined/>
          <span><router-link to="/management" style="padding-left: 10px"><span style="color: white">Management</span></router-link></span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0">
        <UserProfile></UserProfile>
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
import {defineComponent, reactive, ref, toRefs} from 'vue';
import UserProfile from "@/components/Profile";
import PageFooter from "@/components/Footer";

export default defineComponent({
  components: {
    PageFooter,
    UserProfile,
    UserOutlined,
    FileOutlined,
  },

  setup() {
    const state = reactive({
      collapsed: ref(false),
      selectedKeys: ['/books'],
    });


    return {
      ...toRefs(state),
    };
  },

  created() {
    let key;
    if ("/" === location.pathname) {
      key = '/books'
    } else {
      key = location.pathname
    }
    this.selectedKeys = [key]
  }

});

</script>

<style>

.logo {
  background: url("assets/book.png");
  background-repeat: no-repeat;
  background-position: center;
  height: 64px;
}

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

/*scroll bar style*/
::-webkit-scrollbar {
  width: 5px;
  height: 14px;
}

::-webkit-scrollbar-thumb {
  background-color: #b6b6b6;
}
</style>
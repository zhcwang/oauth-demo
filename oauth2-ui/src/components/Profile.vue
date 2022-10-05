<template>
  <div class="profile">
    <a-popover placement="left">
      <template #content>
        <p>{{ userName }}</p>
        <p @click="logout">Logout</p>
      </template>
      <a-avatar style="background-color: #87d068; margin-right: 20px">
        <template #icon>
          <UserOutlined/>
        </template>
      </a-avatar>

    </a-popover>
  </div>

</template>

<script>
import {UserOutlined} from '@ant-design/icons-vue';
import {defineComponent} from 'vue';
import jwtDecode from 'jwt-decode'
import {message} from "ant-design-vue";

export default defineComponent({
  components: {
    UserOutlined,
  },
  name: 'UserProfile',
  data() {
    return {
      userName: ""
    }
  },
  methods: {
    logout() {
      // Should call auth server for logout, here simplify this process, clear cookies and tokens
      //localStorage.removeItem("Authorization")
      //localStorage.removeItem("refresh_token")
     
      console.log( this.$cookies.get('JSESSIONID'))
      console.log( document.cookie)
      message.info('You have been successful logout!');
    }
  },
  created() {
    let token = localStorage.getItem("Authorization");
    const decoded = jwtDecode(token);
    this.userName = decoded.sub
  }
});

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.profile {
  float: right;

}
</style>

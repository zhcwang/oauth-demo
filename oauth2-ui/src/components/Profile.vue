<template>
  <div class="profile">
    <a-popover placement="left">
      <template #content>
        <p>{{userName}}</p>
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
      alert('quit')
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

import {createApp} from 'vue'
import App from './App.vue'

import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

import router from "@/router";

import VueCookies from 'vue-cookies'

const app = createApp(App)
app.config.productionTip = false;
app.use(Antd)
app.use(router)
app.use(VueCookies)
app.mount('#app')

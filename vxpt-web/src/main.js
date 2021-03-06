import { createApp } from 'vue'
import App from './App.vue';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';


import router  from './assets/router.js';

const app = createApp(App);

app.use(Antd).use(router).mount('#app');


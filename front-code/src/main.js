import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import pinia from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

// Create Vue app
const app = createApp(App)

// Use plugins
app.use(router)
app.use(pinia)
app.use(Antd)

// Mount the app
app.mount('#app')

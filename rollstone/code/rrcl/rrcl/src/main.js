import iView from 'iview'
import 'iview/dist/styles/iview.css'
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/index'
import VueParticles from 'vue-particles' //登录注册背景
import $ from 'jquery'
import Axios from 'axios'
import go from 'gojs'
import 'vue2-animate/dist/vue2-animate.min.css'
import VideoPlayer from 'vue-video-player'
require('video.js/dist/video-js.css')
require('vue-video-player/src/custom-theme.css')
Vue.use(VideoPlayer)
Vue.prototype.$axios = Axios
import domain from './domain.js';
global.domain = domain;
Axios.defaults.withCredentials = true
Axios.defaults.baseURL = '/api'
Axios.defaults.headers.post['Content-Type'] = 'application/json';
Vue.use(VueParticles)
Vue.use(iView);
Vue.config.productionTip = false;
/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    components: { App },
    template: '<App/>'
})
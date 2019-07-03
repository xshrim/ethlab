import Vue from 'vue';
import Router from 'vue-router';
import iView from 'iview';
import 'iview/dist/styles/iview.css';
import store from '../store/index'
Vue.use(iView);
Vue.use(Router)

const router = new Router({
    mode: 'history',
    base: '/dist/',
    routes: [{ path: '/', redirect: '/Login' },
        {
            path: '/Login',
            component: resolve => require(['../components/Login.vue'], resolve),
            children: [
                { path: '/', redirect: '/userLogin' },
                {
                    path: '/emailRegistered',
                    name: 'emailRegistered',
                    component: resolve => require(['../components/eRegistered.vue'], resolve)
                },
                {
                    path: '/phoneRegistered',
                    name: 'phoneRegistered',
                    component: resolve => require(['../components/pRegistered.vue'], resolve)
                },
                {
                    path: '/userLogin',
                    component: resolve => require(['../components/userLogin.vue'], resolve)
                }
            ]
        },
        {
            path: '/Error',
            name: 'Error',
            meta: { requireAuth: true },
            component: resolve => require(['../components/pages/Error.vue'], resolve)
        },
        {
            path: '/Home',
            meta: { requireAuth: true },
            component: resolve => require(['../components/Home.vue'], resolve),
            children: [{
                    path: '',
                    component: resolve => require(['../components/pages/rMain.vue'], resolve)
                },
                {
                    path: '/Main',
                    name: "Main",
                    component: resolve => require(['../components/pages/rMain.vue'], resolve)
                },
                {
                    path: '/Uploading',
                    name: "Uploading",
                    component: resolve => require(['../components/pages/rUploading.vue'], resolve)
                },
                {
                    path: '/Monitoring',
                    name: 'Monitoring',
                    component: resolve => require(['../components/pages/rMonitoring.vue'], resolve)
                },
                {
                    path: '/Impower',
                    component: resolve => require(['../components/pages/rImpower.vue'], resolve),
                    children: [{
                            path: '/Impowervideo',
                            component: resolve => require(['../components/details/rImpowervideo.vue'], resolve)
                        },
                        {
                            path: '/Myvideo',
                            component: resolve => require(['../components/details/rMyvideo.vue'], resolve)
                        },
                        {
                            path: '',
                            component: resolve => require(['../components/details/rMyvideo.vue'], resolve)
                        }
                    ]
                },
                {
                    path: '/Chain',
                    name: 'Chain',
                    component: resolve => require(['../components/pages/rChain.vue'], resolve)
                },
                {
                    path: '/Record',
                    name: 'Record',
                    component: resolve => require(['../components/pages/rRecord.vue'], resolve)
                },
                {
                    path: '/Behavior',
                    name: 'Behavior',
                    component: resolve => require(['../components/pages/rBehavior.vue'], resolve)
                },
                {
                    path: '/ChainDetails',
                    name: 'ChainDetails',
                    component: resolve => require(['../components/pages/rChainDetails.vue'], resolve),
                },
                {
                    path: '/BlockTrade',
                    name: 'BlockTrade',
                    component: resolve => require(['../components/details/rBlcokTrade.vue'], resolve)
                },
                {
                    path: '/ApplyList',
                    name: 'ApplyList',
                    component: resolve => require(['../components/pages/rApplyList.vue'], resolve)
                },
                {
                    path: '/Information',
                    name: 'Information',
                    meta: { requireAuth: true },
                    component: resolve => require(['../components/pages/rInformation.vue'], resolve)
                },
                {
                    path: '/ChangeInformation',
                    name: 'ChangeInformation',
                    meta: { requireAuth: true },
                    component: resolve => require(['../components/pages/rChangeInformation.vue'], resolve)
                },
                {
                    path: '/PersonInfor',
                    name: 'PersonInfor',
                    meta: { requireAuth: true },
                    component: resolve => require(['../components/pages/rPersonInfor.vue'], resolve)
                },
                {
                    path: '/Level2',
                    name: 'Level2',
                    meta: { requireAuth: true },
                    component: resolve => require(['../components/details/Level2.vue'], resolve)
                },
                {
                    path: '/Level5',
                    name: 'Level5',
                    meta: { requireAuth: true },
                    component: resolve => require(['../components/details/Level5.vue'], resolve)
                },
                {
                    path: '/Level6',
                    name: 'Level6',
                    meta: { requireAuth: true },
                    component: resolve => require(['../components/details/Level6.vue'], resolve)
                }
            ]
        },
    ]
});
//注册全局钩子用来拦截导航
// router.beforeEach((to, from, next) => {
//     // iView.Spin.show();
//     if (to.matched.some(res => res.meta.requireAuth)) { // 判断该路由是否需要登录权限
//         if (store.state.token) { // 通过vuex state获取当前的token是否存在
//             next();
//         } else {
//             next({
//                 path: '/login',
//                 query: { redirect: to.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
//             })
//         }
//     } else {
//         next();
//     }
// })
router.afterEach(() => {
    // iView.Spin.hide();
})
export default router;
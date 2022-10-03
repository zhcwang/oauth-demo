import {createRouter, createWebHistory} from 'vue-router'
import {message} from 'ant-design-vue';
import {v4 as uuidv4} from 'uuid';
import axios from "axios";

const routes = [
    {
        path: '/books',
        name: 'All books',
        component: () => import('@/components/BookContent')
    },
    {
        path: '/management',
        name: 'Book management',
        component: () => import('@/components/BookManagement')
    }
]

const router = createRouter({
    //history: createWebHashHistory(),  //hash
    history: createWebHistory(),  //history
    routes
})

function isAuthorizationCodeResponse(toPath) {
    if (toPath.query) {
        let cacheKey = toPath.query.state;
        if (toPath.query.code && cacheKey) {
            return true;
        }
    }
    return false;
}

function validateAuthorizationCode(toPath) {
    let cacheKey = toPath.query.state;
    let redirectUrl = localStorage.getItem(cacheKey);
    if (localStorage.getItem(cacheKey)) {
        localStorage.removeItem(cacheKey)
        return redirectUrl;
    } else {
        message.error('Invalidate authorization code.');
    }

}

axios.interceptors.request.use(function (config) {
    // Do something before request is sent
    config.headers['Authorization'] = 'Bearer ' + localStorage.getItem("Authorization");
    console.log(JSON.stringify(config))
    return config;

}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
    // Do something with response data
    return response;
}, function (error) {
    // TODO: Request exception handler
    return Promise.reject(error);
});

const OAUTH_SERVER_ADDR = "http://localhost:10380/oauth"
const OAUTH_SERVER_REDIRECT_URI = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/books";


router.beforeEach(async (to, from, next) => {
    // http://localhost:8088/books?code=737141455d274ef4a19114a20b0fc485&state=123456
    if (isAuthorizationCodeResponse(to)) {
        let redirectUrl = validateAuthorizationCode(to)
        if (!redirectUrl) {
            return;
        }
        let authorizationCode = to.query.code;
        let tokenUrl = OAUTH_SERVER_ADDR + "/token?" +
            "client_id=book_share" +
            "&grant_type=authorization_code" +
            "&redirect_uri=" + OAUTH_SERVER_REDIRECT_URI +
            "&code=" + authorizationCode;
        // http://localhost:10380/oauth/token?client_id=book_share&grant_type=authorization_code&redirect_uri=http://localhost:8088/books&code=80830773dda144ca926b497bd297583b
        await axios
            .post(tokenUrl)
            .then(response => {
                if (response.status === 200 && response.data.access_token) {
                    localStorage.setItem('Authorization', response.data.access_token)
                    localStorage.setItem('refresh_token', response.data.refresh_token)
                    window.location.href = redirectUrl
                } else if (response.data.message) {
                    message.error(response.data.message)
                } else {
                    message.error("Unknown error in fetching token.")
                }
            }).catch(function (err) {
                message.error(err)
            });
    } else {
        const token = localStorage.getItem('Authorization');
        if (token === null || token === '') {
            message.error('Please login first.');

            // redirect url must be equals to http://localhost:8088/books
            let state = uuidv4();

            let url = OAUTH_SERVER_ADDR + "/authorize?client_id=book_share&response_type=code" +
                "&state=" + state +
                "&redirect_uri=" + OAUTH_SERVER_REDIRECT_URI;
            // cache state
            localStorage.setItem(state, window.location.href)

            setTimeout(function () {
                window.location.href = url
            }, 3000);
        } else {
            next();
        }
    }
});


export default router;

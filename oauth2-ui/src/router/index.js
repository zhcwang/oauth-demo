import {createRouter, createWebHistory} from 'vue-router'
import {message} from 'ant-design-vue';
import {v4 as uuidv4} from 'uuid';
import axios from "axios";
import config from "../config"

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/components/BookContent')
    },
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
    return config;

}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
    // Do something with response data
    return response;
}, function (error) {
    let status = error.response.status
    console.error(error.message)
    if (status === 400) {
        message.error('Bad Request.');
    } else if (status === 401) {
        localStorage.removeItem("Authorization")
        localStorage.removeItem("refresh_token")
        redirectToLoginPage()
    } else if (status === 403) {
        message.error('No Permissions.');
    } else if (status >= 500) {
        message.error('Server Error.');
    }
    return Promise.reject(error);
});

function redirectToLoginPage() {
    message.error('Please login first.');
    let state = uuidv4();

    let url = config.app.authServerUrl + "/authorize?client_id=" + config.app.clientId + "&response_type=code" +
        "&state=" + state +
        "&redirect_uri=" + config.app.redirectUrl;
    // cache state
    localStorage.setItem(state, window.location.href)

    setTimeout(function () {
        window.location.href = url
    }, 3000);
}

router.beforeEach(async (to, from, next) => {


// http://localhost:8088/books?code=737141455d274ef4a19114a20b0fc485&state=123456
    if (isAuthorizationCodeResponse(to)) {
        let redirectUrl = validateAuthorizationCode(to)
        if (!redirectUrl) {
            return;
        }
        let authorizationCode = to.query.code;
        let tokenUrl = config.app.authServerUrl + "/token?" +
            "client_id=" + config.app.clientId +
            "&grant_type=authorization_code" +
            "&redirect_uri=" + config.app.redirectUrl +
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
            // redirect url must be equals to http://localhost:8088/books
            redirectToLoginPage();
        } else {
            next();
        }
    }
});


export default router;

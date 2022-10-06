<template>
  <a-form class="book_form"
          ref="formRef"
          :model="formState"
          :rules="rules"
          :label-col="labelCol"
          :wrapper-col="wrapperCol">
    <a-form-item ref="name" label="Book Name" name="name">
      <a-input v-model:value="formState.name"/>
    </a-form-item>
    <a-form-item ref="description" label="Book Description" name="description">
      <a-input v-model:value="formState.description"/>
    </a-form-item>
    <a-form-item ref="reason" label="Recommend Reason" name="reason">
      <a-textarea v-model:value="formState.reason"/>
    </a-form-item>
    <a-form-item ref="image" label="Front Cover" name="image">
      <a-upload
          v-model:file-list="fileList"
          name="avatar"
          list-type="picture-card"
          class="avatar-uploader"
          :show-upload-list="false"
          :multiple="false"
          :before-upload="beforeUpload"
          :customRequest="selfUpload" ,
      >
        <img v-if="imageUrl" :src="imageUrl" alt="avatar" style="height: 100px; width: 100px;"/>
        <div v-else>
          <loading-outlined v-if="loading"></loading-outlined>
          <plus-outlined v-else></plus-outlined>
          <div class="ant-upload-text">Upload</div>
        </div>
      </a-upload>
    </a-form-item>

    <a-form-item :wrapper-col="{ span: 14, offset: 10 }" style="padding-bottom: 20px">
      <a-button type="primary" @click="submit" style="margin-right: 20px">Create</a-button>
      
      <a-button type="error" @click="cancel">Cancel</a-button>
    </a-form-item>
  </a-form>

</template>

<script>
import {defineComponent, reactive, ref, toRaw} from 'vue';
import {LoadingOutlined, PlusOutlined} from '@ant-design/icons-vue';
import {message} from 'ant-design-vue';
import axios from "axios";
import config from "../config"

// import axios from "axios";


// parent child communication https://blog.csdn.net/bhq1711617151/article/details/119279151
export default defineComponent({
  components: {
    LoadingOutlined,
    PlusOutlined,
  },

  data() {
    return {};
  },

  setup(props, context) {
    const formRef = ref();
    const imageUrl = ref('');

    const formState = reactive({
      name: undefined,
      description: undefined,
      reason: undefined,
      image: imageUrl
    });

    const fileList = ref([]);
    const loading = ref(false);

    const beforeUpload = file => {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';

      if (!isJpgOrPng) {
        message.error('You can only upload JP(E)G/PNG file!');
      }

      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isLt2M) {
        message.error('Image must smaller than 2MB!');
      }

      return isJpgOrPng && isLt2M;
    };

    const rules = {
      name: [
        {
          required: true,
          message: 'Please input Book name',
          trigger: 'blur',
        }
      ],
      image: [
        {
          required: true,
          message: 'Please upload a frond cover',
          trigger: 'blur',
        }
      ]
    }

    const resetForm = () => {
      formRef.value.resetFields();
    };

    function convertBase64UrlToBlob (base64) {
      let urlData = base64.dataURL
      let type = base64.type
      let bytes
      if(urlData.split(',').length>1) {
        bytes = window.atob(urlData.split(',')[1])
      }else{
        bytes = window.atob(urlData)
      }
      let ab = new ArrayBuffer(bytes.length)
      let ia = new Uint8Array(ab)
      for (let i = 0; i < bytes.length; i++) {
        ia[i] = bytes.charCodeAt(i)
      }
      return new Blob([ab], { type: type })
    }

    const submit = () => {
      formRef.value
          .validate()
          .then(() => {
            let formData = toRaw(formState);
            axios({
              method: 'post',
              url: `${config.app.apiSeverUrl}/api/books/`,
              //headers: {'content-type': 'application/x-www-form-urlencoded'},
              headers: {'content-type': 'multipart/form-data'},
              data: {
                "name": formData.name,
                "description": formData.description,
                "reason": formData.reason,
                "image": convertBase64UrlToBlob({
                  "dataURL": formData.image.value,
                  "type": "image/png"
                })
              }
            }).then(res => {
              resetForm()
              context.emit('bookSaved', res.data)
            }).catch(e => {
              return Promise.reject(e)
            });
          })
          .catch(error => {
            console.log('Unable to create a book', error);
            message.error('Unable to create a book.');
          });

    };

    const cancel = () => {
      resetForm()
      context.emit('bookCanceled', null)
    };

    const selfUpload = ({file}) => {
      const base64 = new Promise(resolve => {
        const fileReader = new FileReader();
        fileReader.readAsDataURL(file);
        fileReader.onload = () => {
          resolve(fileReader.result);
        }
      });
      base64.then((result) => {
        imageUrl.value = result;
        loading.value = false;
      }).catch(error => {
        loading.value = false;
        message.error('upload error', error);
      });

    }

    return {
      formRef,
      labelCol: {
        span: 6,
        style: {}
      },
      wrapperCol: {
        span: 14,
      },
      formState,
      rules,
      submit,
      cancel,
      resetForm,
      fileList,
      loading,
      imageUrl,
      beforeUpload,
      selfUpload,
    };
  },
});

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.book_form {
  padding-top: 20px;

}
</style>

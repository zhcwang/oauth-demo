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
    <a-form-item label="Book Description">
      <a-input v-model:value="formState.description"/>
    </a-form-item>
    <a-form-item label="Recommend Reason">
      <a-input v-model:value="formState.reason"/>
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

    <a-form-item :wrapper-col="{ span: 14, offset: 10 }">
      <a-button type="primary" @click="onSubmit">Create</a-button>
    </a-form-item>
  </a-form>

</template>

<script>
import {defineComponent, reactive, ref, toRaw} from 'vue';
import {LoadingOutlined, PlusOutlined} from '@ant-design/icons-vue';
import {message} from 'ant-design-vue';

export default defineComponent({
  components: {
    LoadingOutlined,
    PlusOutlined,
  },

  setup() {
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

    const onSubmit = () => {
      formRef.value
          .validate()
          .then(() => {
            console.log('values', formState, toRaw(formState));
            alert(JSON.stringify(toRaw(formState)))
          })
          .catch(error => {
            console.log('error', error);
          });

    };

    const selfUpload = ({file}) => {
      console.log(file, 'action, file');
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
        style:{
          
        }
      },
      wrapperCol: {
        span: 14,
      },
      formState,
      rules,
      onSubmit,
      resetForm,
      fileList,
      loading,
      imageUrl,
      beforeUpload,
      selfUpload
    };
  },
});

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.book_form{
 padding-top: 20px ;
  
}
</style>

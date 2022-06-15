<script lang="ts">
import { defineComponent, reactive } from "vue";
import { notification } from "ant-design-vue";
import axios from "../assets/axios";
import moment from "moment";
import md5 from "js-md5";
import router from "../assets/router";

interface FormState {
  email: string;
  pwd: string;
  remember: boolean;
}
export default defineComponent({
  setup() {
    const formState = reactive<FormState>({
      email: "",
      pwd: "",
      remember: true,
    });
    const onFinish = (values: any) => {
      let df = moment().format("YYYYMMDDHH");
      let pwd = md5(formState.pwd + df);
      axios
        .post("/user/login", { pwd: pwd, email: formState.email })
        .then((res) => {
          console.log(res);
          if (res["code"] != 200) {
            notification.open({
              message: "登录失败",
              type: "error",
              description: "请检查账号和密码是否错误",
            });
          } else {
            window.sessionStorage.setItem("token", res.data);
            router.push("/torrent");
          }
        });
      console.log("Success:", values);
    };

    const onFinishFailed = (errorInfo: any) => {
      console.log("Failed:", errorInfo);
    };

    return {
      formState,
      onFinish,
      onFinishFailed,
    };
  },
});
</script>

<template>
  <a-layout>
    <a-layout-header> </a-layout-header>
    <a-layout-content>
      <a-card title="vxpt登录" :bordered="false">
        <a-form
          :model="formState"
          name="basic"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 16 }"
          autocomplete="off"
          @finish="onFinish"
          @finishFailed="onFinishFailed"
        >
          <a-form-item
            label="邮箱"
            name="email"
            :rules="[{ required: true, message: 'Please input your email!' }]"
          >
            <a-input v-model:value="formState.email" />
          </a-form-item>

          <a-form-item
            label="密码"
            name="pwd"
            :rules="[
              { required: true, message: 'Please input your password!' },
            ]"
          >
            <a-input-password v-model:value="formState.pwd" />
          </a-form-item>

          <a-form-item name="remember" :wrapper-col="{ offset: 4, span: 16 }">
            <a-checkbox v-model:checked="formState.remember"
              >记住密码</a-checkbox
            >
          </a-form-item>

          <a-form-item :wrapper-col="{ offset: 4, span: 16 }">
            <a-button type="primary" html-type="submit">登录</a-button>
          </a-form-item>
        </a-form>
      </a-card>
    </a-layout-content>
    <a-layout-footer> ssss </a-layout-footer>
  </a-layout>
</template>

<style scoped>
.ant-layout-content {
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}
.ant-card {
  border-radius: 10px;
  width: 400px;
}
</style>

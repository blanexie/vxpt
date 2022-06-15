<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import { notification } from "ant-design-vue";
import type { SelectProps } from "ant-design-vue";

import axios from "../assets/axios";
import moment from "moment";
import router from "../assets/router";
import bencode from "bencode";

interface Label {
  id: Number;
  type: String; //标签分组, 比如: 码率标签,  分级标签, 来源标签当
  name: String; //具体标签名称
  description: String;
}


interface FormState {
  title: String; // 标题
  cover: String; //封面
  category: Label; //分类
  labels: Label[];
  content: String; //markdown 文本描述
  //  torrents: Array<TorrentDTO>;
  remember: Boolean;
}
export default defineComponent({
  setup() {
    const category: Label[] = reactive([]);
    const labels = reactive([]);
    const formState = reactive<FormState>({
      title: "",
      cover: "",
      category: null,
      labels: [],
      content: "",
      remember: true,
    });

    onMounted(() => {
      axios.get("/resource/labels").then((res) => {
        console.log(res);

        for (let lb of res.data) {
          if (lb.type == "category") {
            category.push(lb);
          } else {
            labels.push({
              label: lb.name,
              value: lb.id
            })
          }
        }
      });
    });

    const onFinish = (values: any) => {
      console.log("Success:", values);
    };

    const onFinishFailed = (errorInfo: any) => {
      console.log("Failed:", errorInfo);
    };
    return {
      category,
      labels,
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
      <a-card style="width: 1200px">
        <a-form :model="formState" name="basic" :label-col="{ span: 3 }" :wrapper-col="{ span: 16 }" autocomplete="off" @finish="onFinish" @finishFailed="onFinishFailed">
          <a-form-item label="标题" name="title" :rules="[{ required: true, message: 'Please input your title!' }]">
            <a-input v-model:value="formState.title" placeholder="title" />
          </a-form-item>
          <a-form-item label="分类" name="category" :rules="[
            { required: true, message: 'Please input your category!' },
          ]">
            <a-select ref="select" v-model:value="formState.category">
              <a-select-option v-for="(item, i) in category" :key="i" :value="item.id">{{ item.name }}</a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="标签" name="labels" :rules="[{ required: true, message: 'Please input your 标签!' }]">
            <a-select v-model:value="formState.labels" mode="multiple" placeholder="Please select" :options="labels"></a-select>
          </a-form-item>

          <a-form-item label="封面" name="cover" :rules="[
            { required: true, message: 'Please input your cover!' },
          ]">

            <a-input-password v-model:value="formState.cover" />

          </a-form-item>

          <a-form-item label="上传" name="password" :rules="[
            { required: true, message: 'Please input your password!' },
          ]">
            <a-input-password v-model:value="formState.cover" />
          </a-form-item>

          <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
            <a-checkbox v-model:checked="formState.remember">Remember me</a-checkbox>
          </a-form-item>

          <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
            <a-button type="primary" html-type="submit">Submit</a-button>
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

.ant-table-wrapper {
  width: 1280px;
}
</style>

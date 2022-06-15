<script lang="ts">
import { defineComponent, reactive } from "vue";
import { notification } from "ant-design-vue";
import axios from "../assets/axios";
import moment from "moment";
import router from "../assets/router";

const columns = [
  {
    title: "封面",
    dataIndex: "name",
  },
  {
    title: "标题",
    dataIndex: "age",
    width: 150,
  },
  {
    title: "上传时长",
    dataIndex: "address",
  },
  {
    title: "大小",
    dataIndex: "address",
  },
  {
    title: "下载中",
    dataIndex: "address",
  },
  {
    title: "做种中",
    dataIndex: "address",
  },
  {
    title: "下完数",
    dataIndex: "address",
  },
];

const data = [...Array(100)].map((_, i) => ({
  key: i,
  name: `Edward King ${i}`,
  age: 32,
  address: `London, Park Lane no. ${i}`,
}));

export default defineComponent({
  setup() {




    return {
      data,
      columns,
    };
  },
});
</script>

<template>
  <a-layout>
    <a-layout-header> </a-layout-header>
    <a-layout-content>
      <a-table
        :columns="columns"
        :data-source="data"
        :pagination="{ pageSize: 20 }"
      />
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
  width: 1200px;
}
</style>

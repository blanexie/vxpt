<template>
  <el-row>
    <el-col :span="8"></el-col>
    <el-col :span="8">
      <el-card class="box-card">
        <div v-for="o in 4" :key="o" class="text item">{{ 'List item ' + o }}</div>
      </el-card>
    </el-col>
    <el-col :span="8"></el-col>
  </el-row>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login"
}
</script>

<style scoped>

</style>
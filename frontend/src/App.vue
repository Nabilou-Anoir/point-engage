<!-- src/App.vue -->
<template>
  <div id="app">
    <!-- Si la route n'est pas '/role', on affiche Topbar et Sidebar -->
    <div v-if="showLayout">
      <Topbar />
      <div class="layout">
        <Sidebar />
        <div class="main-content">
          <router-view />
        </div>
      </div>
    </div>
    <!-- Si la route est '/role', on affiche seulement le contenu de la page -->
    <div v-else>
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Topbar from '@/components/Topbar.vue'
import Sidebar from '@/components/Sidebar.vue'

const route = useRoute()
// On suppose que la page RoleSelection est accessible via '/role'
const showLayout = computed(() => route.path !== '/role')
</script>

<style>
#app {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  height: 100vh;
}

.layout {
  display: flex;
  height: 100%;
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: #fff;
}
</style>

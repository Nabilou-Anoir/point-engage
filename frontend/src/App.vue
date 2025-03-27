<template>
  <div id="app">
    <!-- Afficher le Topbar uniquement si on n'est pas sur la page Login -->
    <Topbar v-if="showHeader" />
    <div class="layout">
      <!-- Afficher le Sidebar uniquement si on n'est pas sur la page Login -->
      <Sidebar v-if="showSidebar" />
      <!-- Zone de contenu : router-view -->
      <div class="main-content" :class="{ 'full-width': !showSidebar }">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import Sidebar from "@/components/Sidebar.vue";
import Topbar from "@/components/Topbar.vue";

// Récupérer la route active
const route = useRoute();

// Définir les noms de routes pour lesquelles Topbar/Sidebar ne doivent pas s'afficher
const excludedRoutes = ["Login"]; // Ajoutez "Register" ou autres si nécessaire

// Calculer si la Sidebar et le Topbar doivent être affichés
const showSidebar = computed(() => !excludedRoutes.includes(route.name));
const showHeader = computed(() => !excludedRoutes.includes(route.name));
</script>

<style>
#app {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Layout principal : sidebar + contenu */
.layout {
  display: flex;
  flex: 1;
  margin-top: 80px; /* Espace sous la Topbar */
}

/* Style de la Sidebar */
.sidebar {
  width: 250px;
  height: calc(100vh - 80px);
  position: fixed;
  top: 80px;
  left: 0;
  background-color: #6a3fa0;
  z-index: 900;
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
  overflow-y: auto;
  padding: 10px;
}

/* Style de la zone de contenu */
.main-content {
  flex: 1;
  margin-left: 250px;
  padding: 20px;
  background-color: #f5f5f5;
  transition: margin-left 0.3s ease;
}

/* Plein écran lorsque la Sidebar n'est pas affichée */
.main-content.full-width {
  margin-left: 0;
}
</style>

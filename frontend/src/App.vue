<template>
  <div id="app">
    <!-- Topbars dynamiques selon la route -->
    <TopbarEtudiant v-if="isEtudiant && showLayout" />
    <TopbarDirecteur v-else-if="isDirecteur && showLayout" />
    <TopbarReferent v-else-if="isReferent && showLayout" />
    <TopbarScolarite v-else-if="isScolarite && showLayout" />

    <div class="layout">
      <!-- Sidebars uniquement pour Etudiant et Directeur -->
      <SidebarEtudiant v-if="isEtudiant && showLayout" />
      <SidebarDirecteur v-else-if="isDirecteur && showLayout" />

      <!-- Contenu principal plein écran (si pas de sidebar) -->
      <div class="main-content" :class="{ 'full-width': !hasSidebar }">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";

// Import des composants par acteur
import SidebarEtudiant from "@/components/SidebarEtudiant.vue";
import TopbarEtudiant from "@/components/TopbarEtudiant.vue";

import SidebarDirecteur from "@/components/Sidebar.vue";
import TopbarDirecteur from "@/components/Topbar.vue";

import TopbarReferent from "@/components/TopbarReferent.vue";
import TopbarScolarite from "@/components/TopbarScolarite.vue";

const route = useRoute();

const excludedRoutes = ["Login", "Register"];
const showLayout = computed(() => !excludedRoutes.includes(route.name));

// Conditions par acteur selon ta configuration précise des routes
const isEtudiant = computed(() => route.path.startsWith("/etudiant"));
const isDirecteur = computed(() => route.path.startsWith("/directeur"));
const isReferent = computed(() => route.path.startsWith("/referent"));
const isScolarite = computed(() => route.path.startsWith("/scolarite"));

// const isDirecteur = computed(() => route.name.startsWith("AccueilDirecteur"));
// const isReferent = computed(() => route.name.startsWith("AccueilReferent"));
// const isScolarite = computed(() => route.name.startsWith("AccueilScolarite"));

// Sidebar uniquement pour Etudiant et Directeur
const hasSidebar = computed(() => (isEtudiant.value || isDirecteur.value) && showLayout.value);
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

.layout {
  display: flex;
  flex: 1;
  margin-top: 80px;
}

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

.main-content {
  flex: 1;
  margin-left: 250px;
  padding: 20px;
  background-color: #f5f5f5;
  transition: margin-left 0.3s ease;
}

/* Plein écran dynamique quand il n'y a pas de Sidebar */
.main-content.full-width {
  margin-left: 0;
}
</style>

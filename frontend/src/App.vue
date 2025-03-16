<template>
  <div id="app">
    <!-- Si la route est '/role', on affiche uniquement le contenu sans Topbar ni Sidebar -->
    <div v-if="!showLayout">
      <router-view />
    </div>

    <!-- Sinon, on affiche le layout complet -->
    <div v-else>
      <Topbar />
      <div class="layout">
        <!-- Sidebar affichée selon la route -->
        <Sidebar v-if="showSidebar" />
        
        <!-- Contenu principal -->
        <div class="main-content" :class="{ 'full-width': !showSidebar }">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import Sidebar from "@/components/sidebar.vue";  // Harmonisation avec le nom standard
import Topbar from "@/components/Topbar.vue";    // Harmonisation avec le nom standard

const route = useRoute();

// Détermine si l'on affiche le layout complet (Topbar + Sidebar)
const showLayout = computed(() => route.path !== "/role");

// Détermine si la Sidebar doit être affichée
const showSidebar = computed(() => {
  return (
    route.name !== "ProfilView" &&
    route.name !== "HistoriqueView" &&
    route.name !== "ModifierReferentielView"
  );
});
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

/* Layout principal */
.layout {
  display: flex;
  flex: 1;
  margin-top: 80px; /* Espace sous la Topbar */
}

/* Sidebar */
.sidebar {
  width: 250px;
  height: calc(100vh - 80px); /* Ajuste la hauteur pour éviter le débordement */
  position: fixed;
  top: 80px;
  left: 0;
  background-color: #6a3fa0;
  z-index: 900;
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
  overflow-y: auto;
  margin-top: 50px;
  padding: 10px;
}

/* Contenu principal */
.main-content {
  flex: 1;
  margin-left: 250px; /* Décalage pour la Sidebar */
  padding: 20px;
  background-color: #f5f5f5;
  transition: margin-left 0.3s ease;
}

/* Mode plein écran (quand la Sidebar est masquée) */
.main-content.full-width {
  margin-left: 0;
}
</style>

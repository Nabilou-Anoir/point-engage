<template>
  <div id="app">
    <!-- Vérifie si l'utilisateur est un étudiant et s'il n'est PAS sur la page login -->
    <template v-if="isEtudiant && !isLoginPage">
      <TopbarEtudiant />
      <div class="layout">
        <SidebarEtudiant />
        <div class="main-content">
          <router-view v-slot="{ Component }">
            <component :is="Component" v-if="Component" />
            <p v-else>❌ Aucun composant trouvé</p>
          </router-view>
        </div>
      </div>
    </template>

    <!-- Si ce n'est pas un étudiant, afficher uniquement le contenu -->
    <template v-else>
      <div class="layout">
        <div class="main-content">
          <router-view v-slot="{ Component }">
            <component :is="Component" v-if="Component" />
            <p v-else>❌ Aucun composant trouvé</p>
          </router-view>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import SidebarEtudiant from "@/components/SidebarEtudiant.vue";
import TopbarEtudiant from "@/components/TopbarEtudiant.vue";

const isEtudiant = ref(false);
const isLoginPage = ref(false);
const route = useRoute();

// Vérifier si l'utilisateur connecté est un étudiant
const checkUserRole = () => {
  const loggedInUser = sessionStorage.getItem("loggedInUser");
  console.log("🔍 Vérification de l'utilisateur connecté :", loggedInUser);

  if (loggedInUser) {
    const user = JSON.parse(loggedInUser);
    console.log("👤 Rôle de l'utilisateur :", user.role?.name);

    // Vérifie si l'utilisateur a bien le rôle "ROLE_ETUDIANT"
    isEtudiant.value = user.role?.name === "ROLE_ETUDIANT";
  } else {
    isEtudiant.value = false;
  }
};

// Vérifier si l'on est sur la page de connexion
const checkLoginPage = () => {
  isLoginPage.value = route.path === "/login";
};

// Vérifie les changements de route pour voir si on passe sur login
watch(route, () => {
  checkLoginPage();
  checkUserRole(); // Vérifier à chaque changement de route si l'utilisateur est un étudiant
});

// Vérifier au démarrage si l'utilisateur est déjà connecté
onMounted(() => {
  checkUserRole();
  checkLoginPage();
});
</script>

<style>
.layout {
  display: flex;
  height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px;
}
</style>
